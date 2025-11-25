package com.example.lab4.ui.home
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab4.databinding.ItemHomeScheduleBinding
import com.example.lab4.model.ScheduleResponse
import android.util.Log
import android.view.View
import com.example.lab4.R
class HomeScheduleAdapter(private val onItemClick: (Long) -> Unit, private val onItemLongClick: (Long) -> Unit) :
    ListAdapter<ScheduleResponse, HomeScheduleAdapter.ViewHolder>
        (DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val binding = ItemHomeScheduleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClick(item.id)
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClick(item.id)
            true
        }
    }
    class ViewHolder(private val binding: ItemHomeScheduleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ScheduleResponse) {
            binding.tvTitle.text = item.habit?.name ?: "Unknown Habit"
            binding.tvTime.text = "${item.startTime} - ${item.endTime}"

        }
    }
    class DiffCallback : DiffUtil.ItemCallback<ScheduleResponse>() {
        override fun areItemsTheSame(oldItem: ScheduleResponse, newItem:
        ScheduleResponse): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: ScheduleResponse,
                                        newItem: ScheduleResponse): Boolean {
            return oldItem == newItem
        }
    }
}

