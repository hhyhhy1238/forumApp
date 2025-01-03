package com.example.client.ui.dashboard

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.client.databinding.FragmentDashboardBinding
import com.example.client.programLogic.bean.Post.Post
import com.example.client.ui.MainActivity

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var postCategory: Spinner
    private lateinit var textTitle: EditText
    private lateinit var textCintent: EditText
    private lateinit var addButton: ImageButton
    private lateinit var deleteButton: ImageButton
    private lateinit var postButton: Button

    private val binding get() = _binding!!


    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        initView()
        initEvent()

        val root: View = binding.root
        return root
    }

    private fun initView() {
        postButton = binding.postButton
        postCategory = binding.postCategory
        textTitle = binding.textTitle
        textCintent = binding.textContent
        addButton = binding.addImageButton
        deleteButton = binding.deleteImageButton
    }

    @RequiresExtension(extension = Build.VERSION_CODES.R, version = 2)
    private fun initEvent(){
        addButton.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_PICK_IMAGES)
            startActivityForResult(intent, 1002)
            onActivityResult(1002,1002,intent)
        }

        postButton.setOnClickListener {
            val post = Post(textTitle.text.toString(),textCintent.toString(),MainActivity.userName)
            if (true){
                Toast.makeText(context, "发帖成功", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(context, "发帖失败", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            // Handle error
            return
        }
        when (requestCode) {
            1002 -> {
//                var currentUri: Uri? = data?.data
//                if (currentUri != null) {
//                    avatar.setImageURI(currentUri)
//                    currentAvatarUri = currentUri
//                }
                return
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}