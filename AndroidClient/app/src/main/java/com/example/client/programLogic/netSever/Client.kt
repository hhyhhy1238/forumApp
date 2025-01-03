package com.example.client.programLogic.netSever

import com.example.client.programLogic.bean.Post.PostListResponse
import com.example.client.programLogic.bean.Post.PostResponse
import com.example.client.programLogic.bean.Post.PostResponseFailedReason
import com.example.client.programLogic.bean.User.User
import com.example.client.programLogic.bean.User.UserInfoResponse
import com.example.client.programLogic.bean.User.UserResponse
import com.example.client.programLogic.bean.UserInfo
import com.example.client.programLogic.bean.review.ReviewResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

object Client {
    private val client by lazy {
        OkHttpClient.Builder().connectTimeout(6000, TimeUnit.SECONDS)
            .readTimeout(6000, TimeUnit.SECONDS)
            .writeTimeout(6000, TimeUnit.SECONDS)
            .build()
    }


    private val gson by lazy {
        Gson()
    }
    fun login(username: String,password: String): UserResponse {
        val requestBody =
            gson.toJson(User(username, password)).toRequestBody(jsonMediaType.toMediaType())
        val request = Request.Builder().url("$serverUrl/userInfo/login").post(requestBody).build()
        val userResponse = client.newCall(request).execute()
        return gson.fromJson(userResponse.body?.string(), UserResponse::class.java)
    }


    fun addUser(user: User): UserResponse {
        val requestBody = gson.toJson(user).toRequestBody(jsonMediaType.toMediaType())
        val request = Request.Builder().url("$serverUrl/user/add").post(requestBody).build()
        val userResponse = client.newCall(request).execute()
        val UserResponse =
            gson.fromJson(userResponse.body?.string(), UserResponse::class.java)
        return UserResponse
    }

    fun getUserInfo1(username: String): UserInfo {

        val request = Request.Builder().url("$serverUrl/UserDetails/query/$username").get().build()
        val userResponse = client.newCall(request).execute()
        return gson.fromJson(userResponse.body?.string(), UserInfo::class.java)
    }

    fun getUserInfo2(username: String): UserResponse {

        val request = Request.Builder().url("$serverUrl/userInfo/query/$username").get().build()
        val userResponse = client.newCall(request).execute()
        return gson.fromJson(userResponse.body?.string(), UserResponse::class.java)
    }

    fun updateUserInfo(
        username: String,
        nickname: String,
        avatar: File,
        birthday: String,
        gender: String,
        signature: String
    ):UserInfoResponse{
        val avatarRequestBody = avatar.asRequestBody(MultipartBody.FORM)
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("nickname", nickname)
            .addFormDataPart("avatar", avatar.name, avatarRequestBody)
            .addFormDataPart("birthday", birthday)
            .addFormDataPart("gender", gender)
            .addFormDataPart("signature", signature)
            .build()
        val request =
            Request.Builder().url("$serverUrl/UserDetails/update").post(multipartBody).build()
        val userInfoResponse = client.newCall(request).execute()

        return gson.fromJson(userInfoResponse.body?.string(), UserInfoResponse::class.java)
    }

    fun getPost(postId: String?): PostResponse {
        if (postId == null) {
            return PostResponse(false, PostResponseFailedReason.POST_DOES_NOT_EXIST, null)
        }
        val request = Request.Builder().url("$serverUrl/post/query/$postId").get().build()
        val postResponse = client.newCall(request).execute()
        val remotePostResponse =
            gson.fromJson(postResponse.body?.string(), PostResponse::class.java)
        return remotePostResponse
    }

    fun getPostListForType(type: String): PostListResponse {
        val request = Request.Builder().url("$serverUrl/samePostTypeList/query/$type").get().build()
        val postListResponse = client.newCall(request).execute()

        return gson.fromJson(postListResponse.body?.string(), PostListResponse::class.java)
    }

    fun getPostComment(reviewId: String): ReviewResponse {
        val request = Request.Builder().url("$serverUrl/review/query/$reviewId").get().build()
        val reviewResponse = client.newCall(request).execute()

        return gson.fromJson(reviewResponse.body?.string(), ReviewResponse::class.java)
    }
//
//    fun addReview(
//        id: String,
//        targetPost: String,
//        username: String,
//        content: String,
//        images: List<File>
//    ): ReviewResponse {
//        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("id", id)
//            .addFormDataPart("targetPost", targetPost)
//            .addFormDataPart("username", username)
//            .addFormDataPart("content", content)
//        if (images.isEmpty()) {
//            multipartBodyBuilder.addFormDataPart("images", "", "".toRequestBody(null))
//        }
//        for (image in images) {
//            val currentImageRequestBody = image.asRequestBody(MultipartBody.FORM)
//            multipartBodyBuilder.addFormDataPart("images", image.name, currentImageRequestBody)
//        }
//        val multipartBody = multipartBodyBuilder.build()
//        val request = Request.Builder().url("$serverUrl/review/add").post(multipartBody).build()
//        val reviewResponse = client.newCall(request).execute()
//        val remoteReviewResponse =
//            gson.fromJson(reviewResponse.body?.string(), ReviewResponse::class.java)
////        if (remoteReviewResponse.success!!) {
////            try {
////                cacheRepository.deletePost(Post(targetPost, "", "", "", "", listOf(), 0, listOf()))
////            } catch (e: Exception) {
////                Log.d("Client", "被捕获的异常: $e")
////            }
////        }
//        return remoteReviewResponse
//    }
//
//    fun addPost(
//        id: String,
//        owner: String,
//        title: String,
//        content: String,
//        images: List<File>
//    ): PostResponse {
//        val multipartBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
//            .addFormDataPart("id", id)
//            .addFormDataPart("owner", owner)
//            .addFormDataPart("title", title)
//            .addFormDataPart("content", content)
//        if (images.isEmpty()) {
//            multipartBodyBuilder.addFormDataPart("images", "", "".toRequestBody(null))
//        }
//        for (image in images) {
//            val currentImageRequestBody = image.asRequestBody(MultipartBody.FORM)
//            multipartBodyBuilder.addFormDataPart("images", image.name, currentImageRequestBody)
//        }
//        val multipartBody = multipartBodyBuilder.build()
//        val request = Request.Builder().url("$serverUrl/post/add").post(multipartBody).build()
//        val postResponse = client.newCall(request).execute()
//        val remotePostResponse =
//            gson.fromJson(postResponse.body?.string(), PostResponse::class.java)
////        if (remotePostResponse.success!!) {
////            try {
////                cacheRepository.deleteUserInfo(UserInfo(owner, "", "", null, listOf(), listOf()))
////            } catch (e: Exception) {
////                Log.d("Client", "被捕获的异常: $e")
////            }
////        }
//        return remotePostResponse
//    }
//
//    fun like(actionRequest: ActionRequest): ActionResponse {
//        val requestBody = gson.toJson(actionRequest).toRequestBody(jsonMediaType.toMediaType())
//        val request = Request.Builder().url("$serverUrl/action/like").post(requestBody).build()
//        val actionResponse = client.newCall(request).execute()
//        return gson.fromJson(actionResponse.body?.string(), ActionResponse::class.java)
//    }
//
//    private fun favor(username: String, targetPost: String, isCancelFavor: Boolean): ActionResponse {
//        val requestBody =
//            gson.toJson(
//                ActionRequest(true, targetPost, null, username)
//            ).toRequestBody(
//                jsonMediaType.toMediaType()
//            )
//        Log.d("Client", "post: ${gson.toJson(
//            ActionRequest(true, targetPost, "", username)
//        )}")
//        val request = Request.Builder()
//            .url("$serverUrl/action/" + if (isCancelFavor) "cancelFavor" else "favor")
//            .post(requestBody).build()
//        val actionResponse = client.newCall(request).execute()
//        return gson.fromJson(actionResponse.body?.string(), ActionResponse::class.java)
//    }
//
//    fun favor(username: String, targetPost: String): ActionResponse {
//        return favor(username, targetPost, false)
//    }
//
//    fun cancelFavor(username: String, targetPost: String): ActionResponse {
//        return favor(username, targetPost, true)
//    }
}