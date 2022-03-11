package com.example.nikeshop.feature.product.comment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikeshop.R
import com.example.nikeshop.data.DataClass.Comment

class CommentAdapter(val showAll: Boolean) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    var commentList = ArrayList<Comment>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindComment(commentList[position])

    override fun getItemCount(): Int {
        return if (commentList.size > 3 && !showAll) 3 else commentList.size

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentTitleTv = itemView.findViewById<TextView>(R.id.commentTitleTv)
        val commentDateTv = itemView.findViewById<TextView>(R.id.commentDateTv)
        val commentContentTv = itemView.findViewById<TextView>(R.id.commentContentTv)
        val authorCommentTv = itemView.findViewById<TextView>(R.id.authorCommentTv)
        fun bindComment(comment: Comment) {
            authorCommentTv.text = comment.author.email
            commentTitleTv.text = comment.title
            commentDateTv.text = comment.date
            commentContentTv.text = comment.content
        }
    }
}