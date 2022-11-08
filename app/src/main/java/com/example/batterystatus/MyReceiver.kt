package com.example.batterystatus

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // 알림사항 : NotificationManager

        val notificationManager = context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE)as NotificationManager
        val builder : NotificationCompat.Builder

        //오레오버전 26버전 이상
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //26버전 이상
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                //채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
            builder = NotificationCompat.Builder(context, channelId)
        }else{
            builder = NotificationCompat.Builder(context)
            builder.run {
                //알림의 기본 정보
                setSmallIcon(android.R.drawable.ic_notification_overlay)
                setWhen(System.currentTimeMillis())
                setContentTitle("김은정")
                val batteryPercent = intent.getStringExtra("batteryPercent")
                setContentText("안녕하세요 지금 배터리  양은 ${batteryPercent}")
            }
            notificationManager.notify(11, builder.build())


        }
}}