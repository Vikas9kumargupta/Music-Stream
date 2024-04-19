package com.example.musicstream

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.musicstream.models.SongModel
import com.google.firebase.firestore.FirebaseFirestore

object MyExoPlayer {
    private var exoplayer : ExoPlayer? = null
    private var currentSong : SongModel? = null

    fun getCurrentSong() : SongModel?{
        return currentSong
    }
    fun getInstance() : ExoPlayer?{
        return exoplayer
    }

    private fun updateCount(){
        currentSong?.id?.let { id->
            FirebaseFirestore.getInstance().collection("songs")
                .document(id)
                .get().addOnSuccessListener {
                    var latestCount = it.getLong("count")
                    if(latestCount==null){
                        latestCount = 1L
                    }else{
                        latestCount += 1
                    }
                    FirebaseFirestore.getInstance().collection("songs")
                        .document(id)
                        .update(mapOf("count" to latestCount))
                }
        }
    }
    fun startPlaying(context: Context, song: SongModel){

        if(exoplayer == null)
             exoplayer = ExoPlayer.Builder(context).build()

        if(currentSong != song){
          //Its a new Song so start Playing
            currentSong = song

            updateCount()
            currentSong?.url?.apply {
                val mediaItem = MediaItem.fromUri(this)
                exoplayer?.setMediaItem(mediaItem)
                exoplayer?.prepare()
                exoplayer?.play()

            }
        }

    }
}