package com.example.client.ui.home

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.client.R
import com.example.client.databinding.FragmentHomeBinding
import com.example.client.programLogic.bean.Others
import com.example.client.programLogic.netSever.Client
import com.example.client.ui.component.ContextToBrief
import com.example.client.ui.component.PostAdapter
import com.example.client.ui.component.PostItem
import com.google.android.material.tabs.TabLayout


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    companion object{
        const val ARG_USER = "user"
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recyclerViewPosts: RecyclerView
    private lateinit var PostAdapter: PostAdapter
    private lateinit var PsotList: List<PostItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val tabLayout: TabLayout = binding.tabLayout
        tabLayout.addTab(tabLayout.newTab().setText("分享"))
        tabLayout.addTab(tabLayout.newTab().setText("吐槽"))
        tabLayout.addTab(tabLayout.newTab().setText("求助"))
        tabLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val t = when (it.text.toString()) {
                        "分享" -> 1
                        "吐槽" -> 2
                        "求助" -> 3
                        else -> 1 // 默认值
                    }
                    PsotList = fetchPostsFromServer(t)
                    PostAdapter = PostAdapter(PsotList)
                    recyclerViewPosts.adapter = PostAdapter
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        recyclerViewPosts = binding.recyclerView
        recyclerViewPosts.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        recyclerViewPosts.layoutManager=LinearLayoutManager(requireContext())

        PsotList=fetchPostsFromServer(1)
        PostAdapter = PostAdapter(PsotList)
        recyclerViewPosts.adapter = PostAdapter
        return root
    }

    private fun fetchPostsFromServer(t: Int): List<PostItem> {
        // 在此处通过网络请求或其他方式获取帖子列表数据
        // 将数据转换为PostItem对象的列表并返回
        val posts = ArrayList<PostItem>()
        val type = "POST_TYPE$t"
        val thread = Thread {
            val response = Client.getPostListForType(type)
            if (response.success == true) {
                for (i in response.postList) {
                    val responseOwner = Client.getUserInfo2(i.owner)
                    val len = responseOwner.userInfo?.avatarUrl!!.length
                    val tmp = PostItem(
                        i.title,
                        Others(
                            responseOwner.userInfo.nickname ?: "无昵称",
                            responseOwner.userInfo.username,
                            Uri.parse(responseOwner.userInfo.avatarUrl.substring(2, len - 2))
                        ),
                        Uri.parse(i.images?.get(0) ?: ""),
                        ContextToBrief(i.content),
                        i.date,
                        i.id
                    )
                    println(i.title)
                    println(i.owner)
                    posts.add(tmp)
                }
            }
        }
        thread.start()
        thread.join()
        return posts
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}