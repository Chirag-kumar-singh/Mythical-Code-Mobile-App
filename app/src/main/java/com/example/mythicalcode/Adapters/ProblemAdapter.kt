package com.example.mythicalcode.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mythicalcode.R
import com.example.mythicalcode.Models.Problem


class ProblemAdapter(
    private val problems: List<Problem>,
    private val onSolveClick: (Problem) -> Unit
) : RecyclerView.Adapter<ProblemAdapter.ProblemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProblemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_problem, parent, false)
        return ProblemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProblemViewHolder, position: Int) {
        val problem = problems[position]
        holder.bind(problem, onSolveClick)
    }

    override fun getItemCount(): Int = problems.size

    class ProblemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(problem: Problem, onSolveClick: (Problem) -> Unit) {
            itemView.findViewById<TextView>(R.id.problemName).text = problem.name
            itemView.findViewById<Button>(R.id.solveButton).setOnClickListener {
                onSolveClick(problem)
            }
        }
    }
}
