package com.example.mythicalcode.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.example.mythicalcode.Models.Problem

class FirebaseService {

    private val db = FirebaseFirestore.getInstance()

    fun getProblems(callback: (List<Problem>) -> Unit) {
        db.collection("Problem")
            .get()
            .addOnSuccessListener { result ->
                val problemList = mutableListOf<Problem>()
                for (document in result) {
                    val name = document.getString("name") ?: ""
                    val problem = Problem(name)
                    problemList.add(problem)
                }
                callback(problemList)
            }
            .addOnFailureListener { exception ->
                // Handle error
                Log.e("FirebaseService", "Error getting documents: ", exception)
            }
    }
}
