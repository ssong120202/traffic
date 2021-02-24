package com.example.myapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.service.TrafficRepository
import kotlinx.android.synthetic.main.traffic_fragment.*


class TrafficFragment : Fragment() {
    private lateinit var trafficViewModel: TrafficViewModel
    private lateinit var trafficRepository: TrafficRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            trafficViewModel = ViewModelProvider(this, TrafficViewModel.Factory(it.application)).get(TrafficViewModel::class.java)
            trafficRepository = TrafficRepository.getInstance(it.application)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.traffic_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchService()
        observeViewModel()
    }

    private fun fetchService() {
        context?.let {
            trafficViewModel.fetchService(it)
        }
    }

    private fun observeViewModel() {
        trafficRepository.trafficInfoList.observe(viewLifecycleOwner, {
            val mAdapter = TrafficAdapter(trafficViewModel.getSortedList(it))
            traffic_info_recycler_view.apply {
                adapter = mAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
        })
    }
}