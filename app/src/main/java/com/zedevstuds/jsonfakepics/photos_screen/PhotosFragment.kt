package com.zedevstuds.jsonfakepics.photos_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zedevstuds.jsonfakepics.utils.LoadingStatus
import com.zedevstuds.jsonfakepics.utils.USER_ID_BUNDLE
import com.zedevstuds.jsonfakepics.databinding.FragmentPhotosBinding

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(layoutInflater, container, false)
        // Получаем переданный id пользхователя
        val userId = arguments?.getLong(USER_ID_BUNDLE)

        val adapter = PhotoListAdapter()
        binding.photosResView.adapter = adapter

        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

        // Наблюдаем за изменением списка фото
        viewModel.userPhotos.observe(viewLifecycleOwner, Observer {
            adapter.photoList = it
        })
        // Наблюдаем за изменением статуса загрузки
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when(it) {
                LoadingStatus.LOADING -> {
                    binding.photosProgressBar.visibility = View.VISIBLE
                    binding.photosErrorTextView.visibility = View.GONE
                }
                LoadingStatus.DONE -> {
                    binding.photosProgressBar.visibility = View.GONE
                    binding.photosErrorTextView.visibility = View.GONE
                }
                LoadingStatus.ERROR -> {
                    binding.photosProgressBar.visibility = View.GONE
                    binding.photosErrorTextView.visibility = View.VISIBLE
                }
                else -> {
                    binding.photosProgressBar.visibility = View.GONE
                    binding.photosErrorTextView.visibility = View.VISIBLE
                }
            }
        })
        viewModel.getUserPhotos(userId)
        return binding.root
    }

}