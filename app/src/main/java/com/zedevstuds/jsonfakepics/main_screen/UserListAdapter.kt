package com.zedevstuds.jsonfakepics.main_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zedevstuds.jsonfakepics.databinding.ItemUserBinding
import com.zedevstuds.jsonfakepics.model.User

class UserListAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    var userList = listOf<User>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.itemView.setOnClickListener {
            onClickListener.onClick(user)
        }
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    // ViewHolder
    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userNameTextView.text = user.username
        }
    }


    class OnClickListener(val clickListener: (user: User) -> Unit) {
        fun onClick(user: User) {
            clickListener(user)
        }
    }

}