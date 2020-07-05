package com.test.tiket.ardanil.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.test.tiket.ardanil.R
import com.test.tiket.ardanil.model.ItemsItem
import kotlinx.android.synthetic.main.recycler_view_item.view.*
import java.util.*

class UserAdapter :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private var users: List<ItemsItem?>? = ArrayList<ItemsItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(users?.get(position), position)
    }

    override fun getItemCount(): Int {
        return if (users != null){
            users!!.size
        }else{
            0
        }
    }

    fun setItem(users: List<ItemsItem?>?) {
        this.users = users
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var user: ItemsItem? = null
        private var itemPosition = 0

        fun bind(user: ItemsItem?, position: Int) {
            this.user = user
            itemPosition = position
            itemView.textView1.text = user?.login

            Glide.with(itemView)
                .load(user?.avatar_url)
                .centerCrop()
                .placeholder(R.drawable.placeholder_profile)
                .into(itemView.imageView1)
        }
    }

}