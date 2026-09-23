package com.example.eventmanager.ui.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eventmanager.data.model.Event
import com.example.eventmanager.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter(private val onEdit: (Event) -> Unit, private val onDelete: (Event) -> Unit) :
    RecyclerView.Adapter<EventAdapter.VH>() {
    private var items = emptyList<Event>()
    private val format = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    fun submitList(list: List<Event>) {
        items = list; notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        VH(ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])
    inner class VH(private val b: ItemEventBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(e: Event) {
            b.tvTitle.text = e.title; b.tvDescription.text = e.description; b.tvLocation.text =
                if (e.location.isBlank()) "Location not specified" else "📍 ${e.location}"; b.tvDate.text =
                e.dateTime?.toDate()?.let(format::format)
                    ?: ""; b.btnEdit.setOnClickListener { onEdit(e) }; b.btnDelete.setOnClickListener {
                onDelete(
                    e
                )
            }
        }
    }
}
