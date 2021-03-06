package com.bdadevfs.musicplayer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bdadevfs.musicplayer.databinding.ActivityPlayerBinding
import com.bdadevfs.musicplayer.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlaylistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}