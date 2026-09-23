package com.example.eventmanager.ui.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.eventmanager.data.model.Event
import com.example.eventmanager.databinding.ActivityAddEditEventBinding
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddEditEventActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_ID = "id";
        const val EXTRA_TITLE = "title";
        const val EXTRA_DESC = "desc";
        const val EXTRA_LOCATION = "location";
        const val EXTRA_TIME = "time"
    }

    private lateinit var b: ActivityAddEditEventBinding
    private val vm: EventViewModel by viewModels()
    private val cal = Calendar.getInstance()
    private var editingId = ""
    private val fmt = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFmt = SimpleDateFormat("hh:mm a", Locale.getDefault())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState); b =
            ActivityAddEditEventBinding.inflate(layoutInflater); setContentView(b.root)
        editingId = intent.getStringExtra(EXTRA_ID).orEmpty();
        val oldTime = intent.getLongExtra(EXTRA_TIME, 0L); if (editingId.isNotBlank()) {
            b.tvHeader.text =
                "Edit Event"; b.etTitle.setText(intent.getStringExtra(EXTRA_TITLE)); b.etDescription.setText(
                intent.getStringExtra(EXTRA_DESC)
            ); b.etLocation.setText(intent.getStringExtra(EXTRA_LOCATION)); if (oldTime > 0) cal.time =
                Date(oldTime)
        }
        refreshButtons(); b.btnDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    cal.set(Calendar.YEAR, y); cal.set(
                    Calendar.MONTH,
                    m
                ); cal.set(Calendar.DAY_OF_MONTH, d); refreshButtons()
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }; b.btnTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, h, min ->
                    cal.set(Calendar.HOUR_OF_DAY, h); cal.set(
                    Calendar.MINUTE,
                    min
                ); cal.set(
                    Calendar.SECOND,
                    0
                ); refreshButtons()
                },
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                false
            ).show()
        }
        b.btnSave.setOnClickListener { save() }
    }

    private fun refreshButtons() {
        b.btnDate.text = "Date: ${fmt.format(cal.time)}"; b.btnTime.text =
            "Time: ${timeFmt.format(cal.time)}"
    }

    private fun save() {
        val title = b.etTitle.text.toString().trim(); if (title.isBlank()) {
            b.etTitle.error = "Title is required"; return
        }; if (cal.timeInMillis < System.currentTimeMillis() - 60000) {
            toast("Date/time cannot be in the past"); return
        };
        val event = Event(
            editingId,
            title,
            b.etDescription.text.toString().trim(),
            Timestamp(cal.time),
            b.etLocation.text.toString().trim()
        );
        val done =
            { ok: Boolean -> if (ok) finish() else toast("Unable to save event") }; if (editingId.isBlank()) vm.add(
            event,
            done
        ) else vm.update(event, done)
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}
