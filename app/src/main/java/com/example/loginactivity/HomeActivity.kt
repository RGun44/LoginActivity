package com.example.loginactivity

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.loginactivity.databinding.ActivityHomeBinding
import com.example.loginactivity.fragment.*
import com.example.loginactivity.room.UserDB
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var binding: ActivityHomeBinding
    var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)

        setContentView(R.layout.activity_home)
        setCurrentFragment(DashboardFragment())
        sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        val id = sharedPreferences!!.getString("id", "")

        Toast.makeText(this@HomeActivity,id.toString(),Toast.LENGTH_SHORT).show()

        bottom_navigation.setOnItemSelectedListener {
            when(it){
                R.id.ic_dashboard->setCurrentFragment(DashboardFragment())
                R.id.ic_profile->{
                    setCurrentFragment(Profile_Fragment())
                }
                R.id.ic_qr->{setCurrentFragment(FragmentQRCode())}
                R.id.ic_info->setCurrentFragment(InfoFragment())
                R.id.location->setCurrentFragment(LocationFragment())
            }
            true
        }
    }

    private fun setCurrentFragment(fragment:Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_exit) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
            builder.setMessage("Are you sure want to exit?")
                .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        finishAndRemoveTask()
                    }
                })
                .show()
        }
        return super.onOptionsItemSelected(item)
    }
}