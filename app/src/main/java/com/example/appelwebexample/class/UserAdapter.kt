package com.example.appelwebexample.`class`

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.appelwebexample.R
import java.util.*

class UserAdapter(  private var users: List<User>,
                    private val onItemClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), Filterable {

    private var filteredUsers: List<User> = users

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = filteredUsers[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { onItemClick(user) }
    }

    override fun getItemCount() = filteredUsers.size

    fun updateUsers(users: List<User>) {
        this.users = users
        this.filteredUsers = users
        notifyDataSetChanged()
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.tvItem1)

        fun bind(user: User) {
            Glide.with(itemView.context)
                .load(user.avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
            nameTextView.text = "${user.firstname} ${user.lastname}"
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val query = constraint.toString().toLowerCase(Locale.getDefault())
                val filteredList = if (query.isEmpty()) {
                    users
                } else {
                    users.filter {
                        it.firstname.toLowerCase(Locale.getDefault()).contains(query)
                    }
                }
                return FilterResults().apply {
                    values = filteredList
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredUsers = results?.values as List<User>
                notifyDataSetChanged()
            }
        }
    }
}