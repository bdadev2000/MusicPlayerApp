package com.bdadevfs.musicplayer

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import androidx.core.app.NotificationCompat
import java.lang.Exception

class MusicService : Service() {
    private var myBinder = MyBinder()
    var mediaPlayer:MediaPlayer? = null
    private lateinit var mediapSession: MediaSessionCompat
    override fun onBind(intent: Intent?): IBinder {
        mediapSession = MediaSessionCompat(baseContext,"My Music")
        return myBinder
    }
    inner class MyBinder:Binder(){
        fun currentService(): MusicService{
            return this@MusicService
        }
    }

    fun showNotification(playPauseBtn: Int){
        val prevIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PREVIOUS)
        val prevPendingIntent = PendingIntent.getBroadcast(baseContext,0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val playIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(baseContext,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val nextIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(baseContext,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val exitIntent = Intent(baseContext,NotificationReceiver::class.java).setAction(ApplicationClass.EXIT)
        val exitPendingIntent = PendingIntent.getBroadcast(baseContext,0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT)

        val imgArt = getImgArt(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
        val img = if(imgArt!=null){
            BitmapFactory.decodeByteArray(imgArt,0,imgArt.size)
        }else{
            BitmapFactory.decodeResource(resources,R.drawable.logo_music)
        }

        val notification = NotificationCompat.Builder(baseContext, ApplicationClass.CHANNEL_ID)
            .setContentTitle(PlayerActivity.musicListPA[PlayerActivity.songPosition].title)
            .setContentText(PlayerActivity.musicListPA[PlayerActivity.songPosition].artist)
            .setSmallIcon(R.drawable.ic_music_notifi)
            .setLargeIcon(img)
            .setStyle(androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediapSession.sessionToken))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .addAction(R.drawable.ic_previous,"Previous",prevPendingIntent)
            .addAction(playPauseBtn,"Play",playPendingIntent)
            .addAction(R.drawable.ic_next,"Next",nextPendingIntent)
            .addAction(R.drawable.ic_exit,"Exit",exitPendingIntent)
            .build()

        startForeground(13,notification)
    }

    fun createMediaPlayer(){
        try {
            if(PlayerActivity.musicService!!.mediaPlayer == null) PlayerActivity.musicService!!.mediaPlayer = MediaPlayer()
            PlayerActivity.musicService!!.mediaPlayer!!.reset()
            PlayerActivity.musicService!!.mediaPlayer!!.setDataSource(PlayerActivity.musicListPA[PlayerActivity.songPosition].path)
            PlayerActivity.musicService!!.mediaPlayer!!.prepare()
            PlayerActivity.binding.playPauseBtnPA.setIconResource(R.drawable.ic_pause)
            PlayerActivity.musicService!!.showNotification(R.drawable.ic_pause)
        }catch (e: Exception){
            return
        }
    }

}