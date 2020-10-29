package com.zedevstuds.jsonfakepics.main_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.zedevstuds.jsonfakepics.R
import com.zedevstuds.jsonfakepics.TAG
import com.zedevstuds.jsonfakepics.USER_ID
import com.zedevstuds.jsonfakepics.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        val adapter = UserListAdapter(UserListAdapter.OnClickListener {
            val bundle = Bundle()
            bundle.putLong(USER_ID, it.id)
            this.findNavController().navigate(R.id.action_mainFragment_to_photosFragment, bundle)
        })
        binding.usersResView.adapter = adapter
        binding.usersResView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.userList = it
        })
        viewModel.getUsers()

        return binding.root
    }

}