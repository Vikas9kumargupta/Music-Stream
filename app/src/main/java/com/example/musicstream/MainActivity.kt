package com.example.musicstream

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicstream.Adapter.Category
import com.example.musicstream.Adapter.SectionSongListAdapter
import com.example.musicstream.Authentication.LoginActivity
import com.example.musicstream.databinding.ActivityMainBinding
import com.example.musicstream.models.CategoryModel
import com.example.musicstream.models.SongModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.core.View
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var categoryAdapter : Category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCategories()
        setupSections("section_1",binding.section1MainLayout,binding.section1Title,binding.section1RecyclerView)
        setupSections("section_2",binding.section2MainLayout,binding.section2Title,binding.section2RecyclerView)
        setupSections("section_3",binding.section3MainLayout,binding.section3Title,binding.section3RecyclerView)
        setupMostlyPlayed("mostly_played",binding.mostlyPlayedMainLayout,binding.mostlyPlayedTitle,binding.mostlyPlayedRecyclerView)

        binding.optionBtn.setOnClickListener{
            showPopupMenu()
        }

    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(this,binding.optionBtn)
        val inflator = popupMenu.menuInflater
        inflator.inflate(R.menu.option_menu,popupMenu.menu)
        popupMenu.show()
        popupMenu.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.logout ->{
                    logout()
                    true
                }
            }
            false
        }
    }

    private fun logout() {
        MyExoPlayer.getInstance()?.release()
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this,LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        showPlayerView()
    }

    //Categories
    private fun getCategories(){
        FirebaseFirestore.getInstance().collection("category").get().addOnSuccessListener {
            val categoryList = it.toObjects(CategoryModel::class.java)
            setupCategoryRecyclerView(categoryList)
        }
    }
    private fun setupCategoryRecyclerView(categoryList: List<CategoryModel>){
        categoryAdapter = Category(categoryList)
        binding.categoriesRV.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.categoriesRV.adapter = categoryAdapter
    }

    //Sections
    private fun setupSections(id: String,mainLayout : RelativeLayout,titleView : TextView, recyclerView: RecyclerView,){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                val section = it.toObject(CategoryModel::class.java)
                section?.apply {
                    mainLayout.visibility = android.view.View.VISIBLE
                    titleView.text = name
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                    recyclerView.adapter = SectionSongListAdapter(songs)
                    mainLayout.setOnClickListener {
                        SongsListActivity.category1 = section
                        startActivity(Intent(this@MainActivity,SongsListActivity::class.java))
                    }
                }
            }
    }

    private fun showPlayerView(){
        //nsamsns
        binding.playerView.setOnClickListener{
            startActivity(Intent(this,PlayerActivity::class.java))
        }
        MyExoPlayer.getCurrentSong()?.let {
            binding.playerView.visibility = android.view.View.VISIBLE
            binding.songTitleTextView.text = "Now Playing : " + it.title
            Glide.with(binding.songCoverImageView).load(it.coverUrl)
                .apply(
                    RequestOptions().transform(RoundedCorners(32))
                ).into(binding.songCoverImageView)
        }?:run{
            binding.playerView.visibility = android.view.View.GONE
        }
    }

    private fun setupMostlyPlayed(id: String, mainLayout : RelativeLayout, titleView : TextView, recyclerView: RecyclerView,){
        FirebaseFirestore.getInstance().collection("sections")
            .document(id)
            .get().addOnSuccessListener {
                //get most played songs
                FirebaseFirestore.getInstance().collection("songs")
                    .orderBy("count",Query.Direction.DESCENDING)
                    .limit(5)
                    .get().addOnSuccessListener {songListSnapshot->
                        val songsModelList = songListSnapshot.toObjects<SongModel>()
                        val songsIdList = songsModelList.map{
                            it.id
                        }.toList()
                        val section = it.toObject(CategoryModel::class.java)
                        section?.apply {
                            section.songs = songsIdList
                            mainLayout.visibility = android.view.View.VISIBLE
                            titleView.text = name
                            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.HORIZONTAL,false)
                            recyclerView.adapter = SectionSongListAdapter(songs)
                            mainLayout.setOnClickListener {
                                SongsListActivity.category1 = section
                                startActivity(Intent(this@MainActivity,SongsListActivity::class.java))
                            }
                        }
                    }
            }
    }
}