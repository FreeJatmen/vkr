package com.example.vkr.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.vkr.R
import com.example.vkr.ui.utilities.VacancyAdapter
import com.example.vkr.ui.utilities.OnVacancyClickListener
import com.example.vkr.ui.retrofit.RetrofitClient.apiService
import com.example.vkr.ui.retrofit.Vacancies
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VacanciesPlaceFragment : Fragment(), OnVacancyClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VacancyAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_place, container, false)

        recyclerView = view.findViewById(R.id.place_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        swipeRefreshLayout = view.findViewById(R.id.place_swipe_refresh)
        swipeRefreshLayout.setOnRefreshListener {
            fetchAllVacancies()
        }

        fetchAllVacancies()

        searchView = view.findViewById(R.id.place_search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchVacancies(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchAllVacancies()

    }

    private fun searchVacancies(query: String) {
        swipeRefreshLayout.isRefreshing = true
        CoroutineScope(Dispatchers.IO).launch {
            val searchResults = apiService.searchVacancies(query)
            println("Search response body: $searchResults")
            val vacanciesList = searchResults.body() ?: emptyList()

            withContext(Dispatchers.Main) {
                adapter = VacancyAdapter(vacanciesList, this@VacanciesPlaceFragment)
                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun fetchAllVacancies() {
        swipeRefreshLayout.isRefreshing = true
        CoroutineScope(Dispatchers.IO).launch {
            val viewAllVacancies = apiService.viewAllVacancies()
            println("Response body: $viewAllVacancies")
            val vacanciesList = viewAllVacancies.body()?: emptyList()

            withContext(Dispatchers.Main) {
                adapter = VacancyAdapter(vacanciesList, this@VacanciesPlaceFragment)
                recyclerView.adapter = adapter
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onItemClick(vacancy: Vacancies) {
        val intent = Intent(requireContext(), VacancyPlaceActivity::class.java).apply {
            putExtra("id", vacancy.rowId)
        }
        startActivity(intent)
    }
}