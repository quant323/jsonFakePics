package com.zedevstuds.jsonfakepics.photos_screen

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.jsonfakepics.BitmapCache
import com.zedevstuds.jsonfakepics.TAG
import com.zedevstuds.jsonfakepics.databinding.ItemPhotoBinding
import com.zedevstuds.jsonfakepics.getImageFromNetwork
import com.zedevstuds.jsonfakepics.model.Photo
import kotlinx.coroutines.*

class PhotoListAdapter(private val imageSetter: PhotoListAdapter.ImageSetter) : RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

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
            var image: Bitmap?
            return withContext(Dispatchers.IO) {
                image = BitmapCache.getBitmapFromMemCache(url)
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

//            return withContext(Dispatchers.IO) {
//                Log.d(TAG, "downloadImageByUrl: ")
//
//                getImageFromNetwork(url)
//            }
        }
    }

    // TODO: 30.10.2020 Delete this class?
    class ImageSetter(val imageSetter: (view: ImageView, progressBar: View, url: String) -> Unit) {
        fun setImage(view: ImageView, progressBar: View, url: String) {
            imageSetter(view, progressBar, url)
        }
    }

}