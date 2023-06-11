package com.example.busschedule

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.busschedule.database.schedule.Schedule
import com.example.busschedule.databinding.BusStopItemBinding
import java.text.SimpleDateFormat
import java.util.*

class BusStopAdapter(private val onItemClicked : (Schedule) -> (Unit)) : ListAdapter<Schedule, BusStopAdapter.BusStopViewHolder>(DiffCallBack){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusStopViewHolder {
        val viewHolder = BusStopViewHolder(
        BusStopItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        )
        viewHolder.itemView.setOnClickListener{
            val position = viewHolder.adapterPosition
            onItemClicked(getItem(position))
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: BusStopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // an object that helps the ListAdapter determine which items in the new and old lists are
    // different when updating the list
    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<Schedule>() {
            // checks if ids are the same
            override fun areItemsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem.id == newItem.id
            }

            // checks if all properties are the same
            override fun areContentsTheSame(oldItem: Schedule, newItem: Schedule): Boolean {
                return oldItem == newItem
            }
        }
    }

    class BusStopViewHolder(private var binding: BusStopItemBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")
        fun bind(schedule: Schedule) {
            binding.stopNameTextView.text = schedule.stopName
            binding.arrivalTimeTextView.text = SimpleDateFormat(
                "h:mm a").format(Date(schedule.arrivalTime.toLong() * 1000))
        }
    }
}