package com.example.vkr.ui.utilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vkr.R
import com.example.vkr.ui.retrofit.Resume
import com.example.vkr.ui.retrofit.Vacancies

class ResumeAdapter(private val resume: List<Resume>,
                     private val itemClickListener: OnResumeClickListener
) :
    RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder>() {


    class ResumeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textResumeName: TextView = itemView.findViewById(R.id.textResumeName)
        private val textName: TextView = itemView.findViewById(R.id.textName)
        private val textSurname: TextView = itemView.findViewById(R.id.textSurname)

        fun bind(resume: Resume, clickListener: OnResumeClickListener) {
            textResumeName.text = resume.resumeName
            textName.text = resume.name
            textSurname.text = resume.surname
            itemView.setOnClickListener {
                clickListener.onItemClick(resume)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.resume_layout, parent, false)
        return ResumeViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val resume = resume[position]
        holder.bind(resume, itemClickListener)
    }

    override fun getItemCount() = resume.size
}