package com.bdadevfs.musicplayer

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import com.bdadevfs.musicplayer.databinding.ActivityPlayerBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.lang.Exception

class PlayerActivity : AppCompatActivity(),ServiceConnection {
    companion object{
        lateinit var musicListPA : ArrayList<Music>
        var songPosition: Int = 0
        var isPlaying: Boolean = false
        var musicService: MusicService ?= null
        lateinit var binding: ActivityPlayerBinding

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.coolPink)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //For starting service
        val intent = Intent(this, MusicService::class.java)
        bindService(intent,this, BIND_AUTO_CREATE)
        startService(intent)
        initialzeLayout()
        binding.playPauseBtnPA.setOnClickListener {
            if(isPlaying) pauseMusic()
            else playMusic()
        }

        binding.previousBtn.setOnClickListener {
            prevNextSong(increment = false)
        }

        binding.nextBtn.setOnClickListener {
            prevNextSong(increment = true)
        }
    }
    private fun setLayout(){
        Glide.with(this)
            .load(musicListPA[songPosition].artUri)
            .apply(RequestOptions().placeholder(R.drawable.logo_music).centerCrop())
            .into(binding.songImgPA)
        binding.songNamePA.text = musicListPA[songPosition].title
    }
    private fun createMediaPlayer(){
        try {
            if(musicService!!.mediaPlayer == null) musicService!!.mediaPlayer = MediaPlayer()
            musicService!!.mediaPlayer!!.reset()
            musicService!!.mediaPlayer!!.setDataSource(musicListPA[songPosition].path)
            musicService!!.mediaPlayer!!.prepare()
            musicService!!.mediaPlayer!!.start()
            isPlaying = true
            binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)

        }catch (e: Exception){
            return
        }
    }
    private fun initialzeLayout(){
        songPosition = intent.getIntExtra("index",0)
        when(intent.getStringExtra("class")){
            "MusicAdapter" -> {
                musicListPA = ArrayList()
                musicListPA.addAll(MainActivity.MusicListMA)
                setLayout()
                createMediaPlayer()

            }
        }
    }
    private fun playMusic(){
        binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
        musicService!!.showNotification(R.drawable.ic_pause)
        isPlaying =true
        musicService!!.mediaPlayer!!.start()
    }
    private fun pauseMusic(){
        binding.playPauseBtnPA.setIconResource(R.drawable.ic_play)
        musicService!!.showNotification(R.drawable.ic_play)
        isPlaying = false
        musicService!!.mediaPlayer!!.pause()
    }

    private fun prevNextSong(increment: Boolean){
        if(increment)
        {
            setSongPosition(increment = true)
            setLayout()
            createMediaPlayer()
        }
        else{
            setSongPosition(increment = false)
            setLayout()
            createMediaPlayer()
        }
    }



    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.currentService()
        createMediaPlayer()

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
    }


}