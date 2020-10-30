package com.zedevstuds.jsonfakepics.photos_screen

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.jsonfakepics.TAG
import com.zedevstuds.jsonfakepics.databinding.ItemPhotoBinding
import com.zedevstuds.jsonfakepics.getImageBitmap
import com.zedevstuds.jsonfakepics.model.Photo
import kotlinx.coroutines.*

class PhotoListAdapter(private val imageSetter: ImageSetter) : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

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
    inner class PhotoViewHolder(private val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.photoTitleTextView.text = photo.title
            CoroutineScope(Dispatchers.Main).launch {
                binding.progressBar.visibility = View.VISIBLE
                binding.photoImageView.setImageBitmap(downloadImageByUrl(photo.url))
                binding.progressBar.visibility = View.GONE
            }

//            // Загружаем изображение по URL и устанавливаем его в ImageView
//           imageSetter.setImage(binding.photoImageView, binding.progressBar, photo.url)
        }

        // Загружает изображение по URL и возвращает его как Bitmap
        private suspend fun downloadImageByUrl(url: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                Log.d(TAG, "downloadImageByUrl: ")
                getImageBitmap(url)
            }
        }

    }

    class ImageSetter(val imageSetter: (view: ImageView, progressBar: View, url: String) -> Unit) {
        fun setImage(view: ImageView, progressBar: View, url: String) {
            imageSetter(view, progressBar, url)
        }
    }

}