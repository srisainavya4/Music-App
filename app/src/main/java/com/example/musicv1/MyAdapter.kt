package com.example.musicv1

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import java.io.ByteArrayOutputStream

//class MyAdapter(private val songlist: ArrayList<MyPicks>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.categories_items, parent, false)
////        return MyViewHolder(itemView)
//        return MyViewHolder(itemView, songlist)
//    }
//
//    override fun getItemCount(): Int {
//        return songlist.size
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val currentItem =  songlist[position]
////        holder.titleImage.setImageResource(currentItem.titleImage)
//        holder.titleImage.setImageDrawable(currentItem.titleImage)
//    }
//    class MyViewHolder(itemView: View, private val songlist: ArrayList<MyPicks>) : RecyclerView.ViewHolder(itemView){
//        val titleImage: AppCompatImageView= itemView.findViewById(R.id.idTVCategory)
//
//        // this is the new part
//        init {
//            titleImage.setOnClickListener {
//                val context = itemView.context
//                val currentItem = songlist[adapterPosition]
//                val intent = Intent(context, NextActivity::class.java)
//                intent.putExtra("songTitle", currentItem.title)
//                intent.putExtra("songUrl", currentItem.songUrl)
//                context.startActivity(intent)
//            }
//        }
//        // this is the new part
//    }
//}

class MyAdapter(private val songlist: ArrayList<MyPicks>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.latest_categories_items, parent, false)
        return MyViewHolder(itemView, songlist)
    }

    override fun getItemCount(): Int {
        return songlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem =  songlist[position]
        holder.titleImage.setImageDrawable(currentItem.titleImage)
        val bitmap = (holder.titleImage.drawable as BitmapDrawable).bitmap

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NextActivity::class.java)
            intent.putExtra("songTitle", currentItem.songTitle)
            intent.putExtra("songUrl", currentItem.songURL)
//            intent.putExtra("titleImage", byteArray)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View, private val songlist: ArrayList<MyPicks>) : RecyclerView.ViewHolder(itemView){
        val titleImage: AppCompatImageView= itemView.findViewById(R.id.idLatestCategory)
    }
}
