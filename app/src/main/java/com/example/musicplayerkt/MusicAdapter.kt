package com.example.musicplayerkt

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
class MusicAdapter(private val list: List<Song>, private val onItemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return MusicViewHolder(view)
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name: TextView

        init {
            tv_name = view.findViewById(R.id.tv_nameOfSong)
            view.setOnClickListener {
                val song = list[layoutPosition]
                onItemClickListener.onItemClick(song)
            }
        }

        fun bind(song: Song) {
            tv_name.text = song.name
        }
    }

    interface OnItemClickListener {
        fun onItemClick(song: Song)
    }
}
