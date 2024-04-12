package com.example.musicstream.Adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.musicstream.SongsListActivity
import com.example.musicstream.databinding.CategoryItemRecyclerRowBinding
import com.example.musicstream.models.CategoryModel

class Category (private val categoryList: List<CategoryModel>) : RecyclerView.Adapter<Category.MyViewHolder>(){

    class MyViewHolder(private val binding : CategoryItemRecyclerRowBinding) :
        RecyclerView.ViewHolder(binding.root){
            //bind the data with views
            fun bindData(category: CategoryModel){
                binding.nameTextView.text = category.name
                Glide.with(binding.coverImageView).load(category.coverUrl).apply(
                    RequestOptions().transform(RoundedCorners(32))
                ).into(binding.coverImageView)

                //Start SongsList Activity
                val context = binding.root.context
                binding.root.setOnClickListener{
                    SongsListActivity.category1 = category
                    context.startActivity(Intent(context,SongsListActivity::class.java))
                }
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Category.MyViewHolder {
        val binding = CategoryItemRecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Category.MyViewHolder, position: Int) {
        holder.bindData(categoryList[position])
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}