package com.example.musicstream

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicstream.databinding.ActivityPlayerBinding
import com.google.firebase.firestore.core.View




class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private lateinit var exoPlayer: ExoPlayer
    private var playerListener = object :  Player.Listener{
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            showGif(isPlaying)
        }
    }
    @OptIn(UnstableApi::class) override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoPlayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = title
            binding.songSubtitleTextView.text = subtitle
            Glide.with(binding.songCoverImageView).load(coverUrl)
                .circleCrop()
                .into(binding.songCoverImageView)
            Glide.with(binding.songGifImageView).load(R.drawable.media_playing)
                .circleCrop()
                .into(binding.songGifImageView)
            exoPlayer = MyExoPlayer.getInstance()!!
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerListener)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerListener)
    }
    fun showGif(show: Boolean) {
        if (show) {
            binding.songGifImageView.visibility = android.view.View.VISIBLE
        } else {
            binding.songGifImageView.visibility = android.view.View.INVISIBLE
        }
    }
}