import android.util.Log
import com.example.mythicalcode.data.CodeRequest
import com.example.mythicalcode.data.CodeResponse
import com.example.mythicalcode.data.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class CodeRepository {

    // Function to send the request
    suspend fun sendCodeRequest(lang: String, code: String, input: String): String = withContext(Dispatchers.IO) {
        try {
            // Send the request and get the response
            val response = apiService.runCode(CodeRequest(lang, code, input))

            // Log the entire response for debugging
            Log.d("CodeRepository", "API Response: $response")

            if (response.success) {
                // Handle success
                response.output ?: "No output available"
            } else {
                // Handle error from response
                val errorMsg = response.error?.error?.stderr ?: "Unknown error occurred"
                Log.e("CodeRepository", "Error: $errorMsg")
                "Error: $errorMsg"
            }
        } catch (e: HttpException) {
            // Handle HTTP error specifically
            val errorBody = e.response()?.errorBody()?.string()

            // Try to parse the error body if it exists
            val errorMsg = if (errorBody != null) {
                try {
                    val parsedError = Gson().fromJson(errorBody, CodeResponse::class.java)
                    parsedError.error?.error?.stderr ?: "Unknown error occurred"
                } catch (jsonException: JsonSyntaxException) {
                    "Failed to parse error response"
                }
            } else {
                "Unknown error occurred"
            }

            Log.e("CodeRepository", "HTTP Exception occurred: ${e.message}, Error: $errorMsg")
            "HTTP Exception: ${e.message}, Error: $errorMsg"
        } catch (e: Exception) {
            // Handle general exceptions
            Log.e("CodeRepository", "Exception occurred: ${e.message}", e)
            "Exception: ${e.message}"
        }
    }
}
