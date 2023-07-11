package com.apex.codeassesment.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apex.codeassesment.data.model.User

class UserRVAdapter: RecyclerView.Adapter<UserRVAdapter.ViewHolder>() {
    lateinit var clickListener: (User)->Unit
    var users:List<User> = arrayListOf()

    inner class ViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
        fun hold(user:User) {
            val textView = view.findViewById<TextView>(android.R.id.text1)
            textView.text = user.name?.first.plus(" ").plus(user.name?.last)
            view.setOnClickListener {
                clickListener(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1,null,false)
        return ViewHolder(view)
    }

    override fun getItemCount() = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.hold(users[position])
    }
}