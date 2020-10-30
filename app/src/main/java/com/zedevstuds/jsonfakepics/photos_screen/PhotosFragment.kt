package com.zedevstuds.jsonfakepics.photos_screen

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zedevstuds.jsonfakepics.LoadingStatus
import com.zedevstuds.jsonfakepics.TAG
import com.zedevstuds.jsonfakepics.USER_ID
import com.zedevstuds.jsonfakepics.databinding.FragmentPhotosBinding
import com.zedevstuds.jsonfakepics.getImageBitmap
import kotlinx.coroutines.*

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(layoutInflater, container, false)
        // Получаем id выбранного пользхователя
        val userId = arguments?.getLong(USER_ID)

        // На вход адаптера передаем объект класса ImageSetter для установки изображения в ImageView
        // элементов списка RecyclerView
        val adapter = PhotoListAdapter(PhotoListAdapter.ImageSetter { view, progressBar, url ->
           // setImageByUrl(view, progressBar, url)
        })
        binding.photosResView.adapter = adapter

        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

        // Наблюдаем за изменением списка фото
        viewModel.userPhotos.observe(viewLifecycleOwner, Observer {
            adapter.photoList = it
        })
        // Наблюдаем за изменением статуса загрузки
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when(it) {
                LoadingStatus.LOADING -> binding.mainProgressBar.visibility = View.VISIBLE
                LoadingStatus.DONE -> binding.mainProgressBar.visibility = View.GONE
                else -> binding.mainProgressBar.visibility = View.GONE
            }
        })
        viewModel.getUserPhotosss(userId)
        return binding.root
    }

//    // Устанавливает изображение в ImageView
//    private fun setImageByUrl(view: ImageView, progressBar: View, url: String) {
//        CoroutineScope(Dispatchers.Main).launch {
//            progressBar.visibility = View.VISIBLE
//            view.setImageBitmap(downloadImageByUrl(url))
//            progressBar.visibility = View.GONE
//        }
//    }
//
//    // Загружает изображение по URL и возвращает его как Bitmap
//    private suspend fun downloadImageByUrl(url: String): Bitmap? {
//        return withContext(Dispatchers.IO) {
//            Log.d(TAG, "downloadImageByUrl: ")
//            getImageBitmap(url)
//        }
//    }

}