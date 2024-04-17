package com.example.musicstream

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicstream.Adapter.SongsListAdapter
import com.example.musicstream.databinding.ActivitySongsListBinding
import com.example.musicstream.models.CategoryModel

class SongsListActivity : AppCompatActivity() {

    companion object{
        lateinit var category1 : CategoryModel
    }
    private lateinit var binding: ActivitySongsListBinding
    private lateinit var songsListAdapter: SongsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nameTextView.text = category1.name
        Glide.with(binding.coverImageView).load(category1.coverUrl)
            .apply(
                RequestOptions().transform(RoundedCorners(32)))
            .into(binding.coverImageView)

        setupSongsListRecyclerView()
    }

    private fun setupSongsListRecyclerView(){
        songsListAdapter = SongsListAdapter(category1.songs)
        binding.songsListRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.songsListRecyclerView.adapter = songsListAdapter
    }
}