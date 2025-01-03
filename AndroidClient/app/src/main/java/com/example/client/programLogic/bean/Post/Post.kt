package com.example.client.programLogic.bean.Post

class Post {
    lateinit var id: String
    lateinit var date: String
    var title: String
    var content: String
    lateinit var likeNum: Integer
    lateinit var PostType: String
    var images: ArrayList<String> ? =null
    var owner: String
    val reviews: List<String>? = null

    constructor(title: String, content: String, ownerUsername: String) {
        this.title = title
        this.content = content
        this.owner = ownerUsername
    }

}