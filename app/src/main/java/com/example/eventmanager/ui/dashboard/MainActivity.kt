package com.example.eventmanager.ui.dashboard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eventmanager.data.model.Event
import com.example.eventmanager.data.repository.AuthRepository
import com.example.eventmanager.databinding.ActivityMainBinding
import com.example.eventmanager.ui.auth.LoginActivity
import com.example.eventmanager.ui.event.AddEditEventActivity
import com.example.eventmanager.ui.event.EventAdapter
import com.example.eventmanager.ui.event.EventViewModel
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding
    private val vm: EventViewModel by viewModels()
    private lateinit var adapter: EventAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b =
            ActivityMainBinding.inflate(layoutInflater); setContentView(b.root)
        adapter = EventAdapter({ event -> openEdit(event) }, { event -> confirmDelete(event) })
        b.recyclerEvents.layoutManager = LinearLayoutManager(this); b.recyclerEvents.adapter =
            adapter
        b.btnAdd.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddEditEventActivity::class.java
                )
            )
        }
        b.btnLogout.setOnClickListener {
            AuthRepository().logout(); startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        ); finish()
        }
        vm.events.observe(this) { events ->
            adapter.submitList(events); updateStats(events); updateChart(
            events
        )
        }
        vm.message.observe(this) { it?.let { Toast.makeText(this, it, Toast.LENGTH_LONG).show() } }
        vm.startListening()
    }

    override fun onResume() {
        super.onResume(); if (::b.isInitialized) vm.startListening()
    }

    private fun openEdit(e: Event) {
        startActivity(Intent(this, AddEditEventActivity::class.java).apply {
            putExtra(
                AddEditEventActivity.EXTRA_ID,
                e.id
            ); putExtra(
            AddEditEventActivity.EXTRA_TITLE,
            e.title
        ); putExtra(
            AddEditEventActivity.EXTRA_DESC,
            e.description
        ); putExtra(
            AddEditEventActivity.EXTRA_LOCATION,
            e.location
        ); putExtra(AddEditEventActivity.EXTRA_TIME, e.dateTime?.toDate()?.time ?: 0L)
        })
    }

    private fun confirmDelete(e: Event) {
        AlertDialog.Builder(this).setTitle("Delete event?").setMessage(e.title)
            .setNegativeButton("Cancel", null).setPositiveButton("Delete") { _, _ ->
                vm.delete(e.id) { ok ->
                    if (!ok) Toast.makeText(
                        this,
                        "Delete failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }.show()
    }

    private fun updateStats(events: List<Event>) {
        val now = System.currentTimeMillis();
        val upcoming = events.count { (it.dateTime?.toDate()?.time ?: 0L) >= now }; b.tvTotal.text =
            "Total\n${events.size}"; b.tvUpcoming.text = "Upcoming\n$upcoming"; b.tvPast.text =
            "Past\n${events.size - upcoming}"
    }

    private fun updateChart(events: List<Event>) {
        val counts = IntArray(12); events.forEach { e ->
            e.dateTime?.toDate()?.let { d ->
                val c = Calendar.getInstance().apply { time = d }; counts[c.get(Calendar.MONTH)]++
            }
        };
        val entries = (0..11).map { BarEntry(it.toFloat(), counts[it].toFloat()) };
        val set = BarDataSet(entries, "Events"); b.chart.data =
            BarData(set); b.chart.description.isEnabled = false; b.chart.axisRight.isEnabled =
            false; b.chart.xAxis.granularity = 1f; b.chart.invalidate()
    }
}
