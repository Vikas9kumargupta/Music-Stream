package com.example.musicstream

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicstream.models.SongModel

object MyExoPlayer {
    private var exoplayer : ExoPlayer? = null
    private var currentSong : SongModel? = null

    fun getCurrentSong() : SongModel?{
        return currentSong
    }
    fun getInstance() : ExoPlayer?{
        return exoplayer
    }

    fun startPlaying(context: Context, song: SongModel){
        if(exoplayer == null)
             exoplayer = ExoPlayer.Builder(context).build()

        if(currentSong != song){
          //Its a new Song so start Playing
            currentSong = song

            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoplayer?.setMediaItem(mediaItem)
                exoplayer?.prepare()
                exoplayer?.play()

            }
        }

    }
}