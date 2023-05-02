package com.dicoding.storyapp.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.databinding.ActivityMainBinding
import com.dicoding.storyapp.utils.dataStore
import com.dicoding.storyapp.view.adapter.LoadingStoryAdapter
import com.dicoding.storyapp.view.adapter.StoryAdapter
import com.dicoding.storyapp.view.addstory.AddStoryActivity
import com.dicoding.storyapp.view.login.LoginActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModel.ViewModelFactory(
            this,
            UserPreference.getInstance(dataStore)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        binding.root.setOnRefreshListener {
            setupView()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_story -> AddStoryActivity.start(this)
            R.id.logout -> showLogoutDialog()
            R.id.open_map -> MapsActivity.start(this)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("Anda yakin ingin Logout?")
            setPositiveButton("Ya") { _, _ ->
                logout()
            }
            setNegativeButton("Tidak", null)
            create()
            show()
        }
    }

    private fun logout() {
        mainViewModel.logout()
        LoginActivity.start(this)
        finish()
    }

    private fun setupView() {
        binding.root.isRefreshing = false
        binding.rvStory.layoutManager = LinearLayoutManager(this)
        setRecycleView()
    }

    private fun setRecycleView() {
        val storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStoryAdapter { storyAdapter.retry() }
            )

            lifecycleScope.launchWhenCreated {
                mainViewModel.listStory.observe(this@MainActivity) {
                    storyAdapter.submitData(lifecycle, it)
                }
            }
        }
    }



    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, MainActivity::class.java)
            starter.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(starter)
        }
    }
}