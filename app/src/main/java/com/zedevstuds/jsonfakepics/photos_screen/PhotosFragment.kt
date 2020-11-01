package com.zedevstuds.jsonfakepics.photos_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zedevstuds.jsonfakepics.R
import com.zedevstuds.jsonfakepics.utils.USER_ID_BUNDLE
import com.zedevstuds.jsonfakepics.databinding.FragmentPhotosBinding
import com.zedevstuds.jsonfakepics.utils.isNetworkAvailable
import com.zedevstuds.jsonfakepics.utils.setProgressViews
import com.zedevstuds.jsonfakepics.utils.showToast

class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PhotosViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotosBinding.inflate(layoutInflater, container, false)
        // Получаем переданный id пользователя
        val userId = arguments?.getLong(USER_ID_BUNDLE)

        val adapter = PhotoListAdapter()
        binding.photosResView.adapter = adapter

        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)

        // Наблюдаем за изменением списка фото
        viewModel.userPhotos.observe(viewLifecycleOwner, Observer {
            adapter.photoList = it
        })
        // Наблюдаем за изменением статуса загрузки и устанавливаем видимость элементов состояния загрузки
        viewModel.status.observe(viewLifecycleOwner, Observer {
            setProgressViews(it, binding.photosProgressBar, binding.photosErrorTextView)
        })

        // Если сетевое подключение доступно - запрашиваем список фотографий пользователя
        if (isNetworkAvailable(this.requireContext()))
            viewModel.getUserPhotos(userId)
        else showToast(this.requireContext(), getString(R.string.network_not_available))

        return binding.root
    }

}