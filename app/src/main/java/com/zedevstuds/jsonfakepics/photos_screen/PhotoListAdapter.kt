package com.zedevstuds.jsonfakepics.photos_screen

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.jsonfakepics.R
import com.zedevstuds.jsonfakepics.databinding.ItemPhotoBinding
import com.zedevstuds.jsonfakepics.getImageBitmap
import com.zedevstuds.jsonfakepics.model.Photo
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

class PhotoListAdapter : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    var photoList = listOf<Photo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(ItemPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = photoList[position]
        holder.bind(photo)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    // ViewHolder
    class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photoTitleTextView.text = photo.title
            CoroutineScope(Dispatchers.Main).launch {
                binding.photoImageView.setImageBitmap(download(photo.url))
            }
        }

        private suspend fun download(url: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                getImageBitmap(url)
            }
        }
    }

}