package com.zedevstuds.jsonfakepics.main_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.zedevstuds.jsonfakepics.R
import com.zedevstuds.jsonfakepics.utils.USER_ID_BUNDLE
import com.zedevstuds.jsonfakepics.databinding.FragmentMainBinding
import com.zedevstuds.jsonfakepics.utils.LoadingStatus

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
            bundle.putLong(USER_ID_BUNDLE, it.id)
            this.findNavController().navigate(R.id.action_mainFragment_to_photosFragment, bundle)
        })
        binding.usersResView.adapter = adapter
        binding.usersResView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.users.observe(viewLifecycleOwner, Observer {
            adapter.userList = it
        })

        // Наблюдаем за изменением статуса загрузки
        viewModel.status.observe(viewLifecycleOwner, Observer {
            when(it) {
                LoadingStatus.LOADING -> {
                    binding.mainProgressBar.visibility = View.VISIBLE
                    binding.mainErrorTextView.visibility = View.GONE
                }
                LoadingStatus.DONE -> {
                    binding.mainProgressBar.visibility = View.GONE
                    binding.mainErrorTextView.visibility = View.GONE
                }
                LoadingStatus.ERROR -> {
                    binding.mainProgressBar.visibility = View.GONE
                    binding.mainErrorTextView.visibility = View.VISIBLE
                }
                else -> {
                    binding.mainProgressBar.visibility = View.GONE
                    binding.mainErrorTextView.visibility = View.VISIBLE
                }
            }
        })
        viewModel.getUsers()

        return binding.root
    }

}