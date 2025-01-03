package com.example.client.ui

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.client.R
import com.example.client.databinding.ActivityFansorCaresBinding
import com.example.client.programLogic.bean.Others
import com.example.client.ui.component.PersonAdapter

class FansorCaresActivity : AppCompatActivity() {
    private lateinit var PersonAdapter: PersonAdapter
    private lateinit var binding: ActivityFansorCaresBinding

    private var recyclerViewPerson: RecyclerView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFansorCaresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var ForC = intent.getStringExtra("ForC")
        var persons = ArrayList<Others>()

        val toolbar : Toolbar = binding.include.idToolbar
        if (ForC == "F") {
            toolbar.setTitle("我的粉丝")
            persons=fetchPersonsFromServer()
        }
        else if(ForC == "C"){
            toolbar.setTitle("我的关注")
            persons=fetchPersonsFromServer()
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        recyclerViewPerson = binding.friendListRecyclerView
        recyclerViewPerson!!.layoutManager = LinearLayoutManager(this)
        PersonAdapter = PersonAdapter(persons)
        recyclerViewPerson!!.adapter = PersonAdapter
    }

    private fun fetchPersonsFromServer(): ArrayList<Others> {
        var persons = ArrayList<Others>()

        var person1 = Others(
            "昵称1",
            "user1",
            Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(R.drawable.baseline_person_48) + '/' +
                        resources.getResourceTypeName(R.drawable.baseline_person_48) + '/' +
                        resources.getResourceEntryName(R.drawable.baseline_person_48)
            )
        )

        persons.add(person1)

        var person2 = Others(
            "昵称2",
            "user2",
            Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                        resources.getResourcePackageName(R.drawable.baseline_person_48) + '/' +
                        resources.getResourceTypeName(R.drawable.baseline_person_48) + '/' +
                        resources.getResourceEntryName(R.drawable.baseline_person_48)
            )
        )

        persons.add(person2)
        return persons
    }
}