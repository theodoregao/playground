package shun.gao.infinitetimerlist.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import shun.gao.infinitetimerlist.R
import java.util.concurrent.TimeUnit

internal class MyListAdapter : RecyclerView.Adapter<TimerViewHolder>() {

    private var size = 0
    private var visibleItems = mapOf<Int, TimerItem?>()

    fun updateList(size: Int?, visibleItems: Map<Int, TimerItem?>?) {
        size?.let { this.size = it }
        visibleItems?.let { this.visibleItems = it }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimerViewHolder {
        val timerView = LayoutInflater.from(parent.context).inflate(R.layout.timer,  parent, false)
        return TimerViewHolder(timerView)
    }

    override fun onBindViewHolder(holder: TimerViewHolder, position: Int) {
        getItemByPosition(position)?.let { holder.bind(it) }
    }

    override fun getItemCount() = size

    private fun getItemByPosition(position: Int) = visibleItems[position]
}

internal class TimerViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    private val nameText = view.findViewById<TextView>(R.id.timer_name_txt)
    private val timerText = view.findViewById<TextView>(R.id.timer_time_txt)

    fun bind(timer : TimerItem) {
        nameText.text = "Timer ${timer.id}"
        timerText.text = generateTimeDiffString(System.currentTimeMillis() - timer.startTime)
    }
}

private fun generateTimeDiffString(millis: Long) = String.format(
    "%02d:%02d.%03d",
    TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
    TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1),
    TimeUnit.MILLISECONDS.toMillis(millis) % TimeUnit.SECONDS.toMillis(1)
)
