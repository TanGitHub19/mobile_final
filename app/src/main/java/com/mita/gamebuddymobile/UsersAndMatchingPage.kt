package com.mita.gamebuddymobile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mita.gamebuddymobile.api.ApiService
import com.mita.gamebuddymobile.api.ReportRequest
import com.mita.gamebuddymobile.api.ReportResponse
import com.mita.gamebuddymobile.api.RetrofitClient
import com.mita.gamebuddymobile.api.UserDataClass
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersAndMatchingPage : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private lateinit var userList: ArrayList<UserDataClass>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_and_matching_page)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        apiService = RetrofitClient.apiService

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.bottom_home -> {
                    val intent = Intent(this, HomePage::class.java)
                    startActivity(intent)
                    true
                }

                R.id.bottom_users -> {
                    true
                }

                R.id.bottom_myAccount -> {
                    val intent = Intent(this, AccounSettings::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.bottom_users

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()

        fetchUserData()

        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

    }

    private fun fetchUserData() {
        apiService.getUser().enqueue(object : Callback<List<UserDataClass>> {
            override fun onResponse(
                call: Call<List<UserDataClass>>,
                response: Response<List<UserDataClass>>
            ) {
                if (response.isSuccessful) {
                    val users = response.body()
                    users?.let {
                        userList.addAll(it)
                        userAdapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(
                        this@UsersAndMatchingPage,
                        "Failed to fetch user data from server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<UserDataClass>>, t: Throwable) {
                Toast.makeText(
                    this@UsersAndMatchingPage,
                    "Error occurred while fetching user data",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


}