package com.example.android.ecommerceadmin.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.ecommerceadmin.databinding.ImageItemBinding

class AddProductImageAdapter(val list:ArrayList<Uri>) : RecyclerView.Adapter<AddProductImageAdapter.AddPrductImageViewHolder>() {

    inner class AddPrductImageViewHolder(val binding : ImageItemBinding ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddPrductImageViewHolder {
        val binding = ImageItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddPrductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddPrductImageViewHolder, position: Int) {
        holder.binding.itemImg.setImageURI(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}