package com.nguyen.jpmorgan.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nguyen.jpmorgan.R
import com.nguyen.jpmorgan.databinding.ItemDayBinding
import com.nguyen.jpmorgan.model.Day
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class DaysAdapter(private val days: List<Day>) : RecyclerView.Adapter<DaysAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(day: Day) {
            binding.day.text = getDay(adapterPosition).split(",")[0]
            binding.image.setImageResource(getResource(day.weather[0].id))
            binding.temperature.text = "${day.temp.max.roundToInt()}\u00B0"
        }

        private fun getResource(id: Int): Int {
            return when (id) {
                in 200..232 -> R.drawable.ic_thunderstorm
                in 300..321 -> R.drawable.ic_shower_rain
                in 500..531 -> R.drawable.ic_rain
                in 600..622 -> R.drawable.ic_snow
                in 701..781 -> R.drawable.ic_mist
                800 -> R.drawable.ic_clear_sky
                801 -> R.drawable.ic_few_clouds
                802 -> R.drawable.ic_scattered_clouds
                803, 804 -> R.drawable.ic_broken_clouds
                else -> -1
            }
        }

        private fun getDay(position: Int): String {
            val calendar = GregorianCalendar()
            calendar.add(GregorianCalendar.DATE, position)
            val dayFormat = SimpleDateFormat("EEEE")
            val day = dayFormat.format(calendar.time)
            val dateFormat = SimpleDateFormat("MMMM dd")
            val date = dateFormat.format(calendar.time)
            return "$day, $date"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDayBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(days[position])

    override fun getItemCount() = days.size
}