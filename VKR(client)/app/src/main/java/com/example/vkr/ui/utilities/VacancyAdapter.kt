package com.example.vkr.ui.utilities
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vkr.R
import com.example.vkr.ui.retrofit.Vacancies

class VacancyAdapter(private val vacancies: List<Vacancies>,
                     private val itemClickListener: OnVacancyClickListener
) :
    RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder>() {


    class VacancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewName: TextView = itemView.findViewById(R.id.textResumeName)
        private val textViewPayment: TextView = itemView.findViewById(R.id.textName)
        private val textViewExpertise: TextView = itemView.findViewById(R.id.textSurname)
        private val textViewEmployment: TextView = itemView.findViewById(R.id.textViewEmployment)

        fun bind(vacancy: Vacancies, clickListener: OnVacancyClickListener) {
            textViewName.text = vacancy.name
            textViewExpertise.text = vacancy.expertise
            textViewEmployment.text = vacancy.employment
            textViewPayment.text = vacancy.payment
            itemView.setOnClickListener {
                clickListener.onItemClick(vacancy)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.vacancy_layout, parent, false)
        return VacancyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = vacancies[position]
        holder.bind(vacancy, itemClickListener)
    }

    override fun getItemCount() = vacancies.size
}