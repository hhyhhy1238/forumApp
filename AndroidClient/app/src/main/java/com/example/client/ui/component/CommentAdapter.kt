package com.example.client.ui.component

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R

class CommentAdapter (private val commentList: List<CommentItem>):RecyclerView.Adapter<CommentAdapter.CommentViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val CommentItem = commentList[position]
        holder.commentTextViewNickname.text = CommentItem.commenterNickname
        holder.commentTime.text = CommentItem.commentTime
        holder.commentContent.text = CommentItem.commentContent
        holder.commentImageViewAvatar.setImageURI(CommentItem.commenterAdapter)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentImageViewAvatar: ImageView = itemView.findViewById(R.id.commentImageViewAvatar)
        val commentTextViewNickname: TextView = itemView.findViewById(R.id.commentTextViewNickname)
        val commentTime: TextView = itemView.findViewById(R.id.commentTextViewTime)
        val commentContent: TextView = itemView.findViewById(R.id.commentTextViewContent)

    }
}