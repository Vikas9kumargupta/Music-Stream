package com.example.musicstream.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicstream.MyExoPlayer
import com.example.musicstream.PlayerActivity
import com.example.musicstream.SongsListActivity
import com.example.musicstream.databinding.SongListItemRecyclerRowBinding
import com.example.musicstream.models.SongModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class SongsListAdapter(private val songIdList: List<String>) : RecyclerView.Adapter<SongsListAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: SongListItemRecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){
        //bind data with view
        fun bindData(songId : String){
//            binding.songTitleTextView.text = songId
            FirebaseFirestore.getInstance().collection("songs")
                .document(songId).get().addOnSuccessListener { it ->
                    val song = it.toObject((SongModel::class.java))
                    song?.apply {
                        binding.songTitleTextView.text = title
                        binding.songSubtitleTextView.text = subtitle
                        Glide.with(binding.songCoverImageView).load(coverUrl)
                            .apply(
                            RequestOptions().transform(RoundedCorners(32))
                        )
                            .into(binding.songCoverImageView)
                        binding.root.setOnClickListener {
                            MyExoPlayer.startPlaying(binding.root.context,song)
                            it.context.startActivity(Intent(it.context,PlayerActivity::class.java))
                        }
                    }
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SongListItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return songIdList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(songIdList[position])
    }
}