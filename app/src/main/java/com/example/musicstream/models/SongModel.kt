package com.example.musicstream.models

import android.icu.text.CaseMap.Title

data class SongModel(
    val id : String,
    val title: String,
    val subtitle: String,
    val url : String,
    val coverUrl : String,
){
    constructor() : this("","","","","")
}
