package com.example.client.programLogic.bean.Post

data class PostListResponse(
    val success: Boolean,
    val postList: List<Post>
)