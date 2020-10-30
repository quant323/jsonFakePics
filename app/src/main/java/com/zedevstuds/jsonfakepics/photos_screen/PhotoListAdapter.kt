package com.zedevstuds.jsonfakepics.photos_screen

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.jsonfakepics.utils.BitmapCache
import com.zedevstuds.jsonfakepics.utils.TAG
import com.zedevstuds.jsonfakepics.databinding.ItemPhotoBinding
import com.zedevstuds.jsonfakepics.utils.getImageFromNetwork
import com.zedevstuds.jsonfakepics.model.Photo
import kotlinx.coroutines.*

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
                binding.progressBar.visibility = View.VISIBLE
                binding.photoImageView.setImageBitmap(downloadImageByUrl(photo.url))
                binding.progressBar.visibility = View.GONE
            }
        }

        // Загружает изображение по URL и возвращает его как Bitmap
        private suspend fun downloadImageByUrl(url: String): Bitmap? {
            return withContext(Dispatchers.IO) {
                // Сперва ищем изображение в кэше
                var image = BitmapCache.getBitmapFromMemCache(url)
                // Если изображения нет в кэше - загружаем его из сети и помещем в кэш
                if (image != null) {
                    Log.d(TAG, "get image from CACHE")
                    return@withContext image
                }
                else {
                    image = getImageFromNetwork(url)
                    Log.d(TAG, "get image from NETWORK")
                    BitmapCache.addBitmapToMemoryCache(url, image!!)
                    return@withContext image
                }
            }
        }
    }

}