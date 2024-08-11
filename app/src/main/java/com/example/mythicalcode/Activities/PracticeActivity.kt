package com.example.mythicalcode.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mythicalcode.R
import com.example.mythicalcode.adapters.ProblemAdapter
import com.example.mythicalcode.services.FirebaseService
import com.google.android.material.bottomnavigation.BottomNavigationView

class PracticeActivity : AppCompatActivity() {

    private lateinit var adapter: ProblemAdapter
    private val firebaseService = FirebaseService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        val recyclerView = findViewById<RecyclerView>(R.id.problemsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch problems from Firebase
        firebaseService.getProblems { problemList ->
            adapter = ProblemAdapter(problemList) { problem ->
                // Handle solve button click
//                val intent = Intent(this, SolveProblemActivity::class.java)
//                intent.putExtra("problemName", problem.name)
//                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_code -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_practice -> {
                    val intent = Intent(this, PracticeActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_battle -> {
                    Toast.makeText(this, "Battle", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }
}

