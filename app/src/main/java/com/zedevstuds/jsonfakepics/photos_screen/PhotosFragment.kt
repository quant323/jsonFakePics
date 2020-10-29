package com.zedevstuds.jsonfakepics.photos_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.zedevstuds.jsonfakepics.TAG
import com.zedevstuds.jsonfakepics.USER_ID
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

        val userId = arguments?.getLong(USER_ID)

        val adapter = PhotoListAdapter()
        binding.photosResView.adapter = adapter

        viewModel = ViewModelProvider(this).get(PhotosViewModel::class.java)
        viewModel.userPhotos.observe(viewLifecycleOwner, Observer {
            adapter.photoList = it
        })
        viewModel.getUserPhotosss(userId)

        return binding.root
    }

}