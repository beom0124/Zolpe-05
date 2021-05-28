package com.example.zolpe_05

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.content.Context
import android.location.*
import android.net.Uri
import android.os.*
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
//import android.support.multidex.MultiDex
import android.view.*
import android.widget.Button
//import com.google.android.gms.location.*
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import java.util.ArrayList
import java.util.Locale
import kotlin.concurrent.thread

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

import com.github.bassaer.chatmessageview.model.ChatUser
import com.github.kittinunf.fuel.core.FuelManager
import com.github.bassaer.chatmessageview.model.Message
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.fuel.Fuel

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.api.client.googleapis.testing.auth.oauth2.MockGoogleCredential.ACCESS_TOKEN
import kotlinx.android.synthetic.main.activity_chat.*
import java.io.IOException
import java.net.URL
import java.util.*



class ChatActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        var actionBar : ActionBar?

        lateinit var bitmap: Bitmap
        lateinit var all : Bitmap
        lateinit var context : Context

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener(this)


        //아래는 fulfillment사용을 위한 작업인데 왜 자꾸 오류나냐 시발시발개시발
        FuelManager.instance.baseHeaders = mapOf(
                "Authorization" to "Bearer $ACCESS_TOKEN"
        )

        FuelManager.instance.basePath =
            "https://api.dialogflow.com/v1/"

        FuelManager.instance.baseParams = listOf(
            "v" to "20210528",                  // latest protocol
            "sessionId" to UUID.randomUUID(),   // random ID
            "lang" to "ko"                      // Korea language
        )

        // GetImage(iconurl)

        //채팅 인터페이스 구성
        val human = ChatUser(
            1,
            "나",
            bitmap
        )
        val agent = ChatUser(
            2,
            "오늘은",
            BitmapFactory.decodeResource(resources,
                R.mipmap.ic_launcher_today) //아이콘 넣기
        )

        my_chat_view.setRightBubbleColor(ContextCompat.getColor(this, R.color.lightBlue500));
       // Log.d("read" , email + auth_name)

        var welcome = "안녕하세요.\n오늘의 코디입니다!" //메세지뷰 처음 입장시 출력

        //메세지뷰 처음 입장시 출력
        my_chat_view.send(Message.Builder()
                .setUser(agent)
                .setText(welcome)
                .build()
        )


        //보내기버튼 눌렀을때
        my_chat_view.setOnClickSendButtonListener(
                View.OnClickListener {
                    //new message
                    //Set to chat view
                  //  context = this@MainActivity
                    var calender : Intent? = null
                    calender = context.packageManager.getLaunchIntentForPackage("com.google.android.calendar")

                    try{
                        if (calender == null){
                            install_calender()
                        }else{
                            my_chat_view.send(Message.Builder()
                                    .setRight(true)
                                    .setUser(human)
                                    .setText(my_chat_view.inputText)
                                    .build()
                            )
                            //HTTP 응답으로 result/fulfillment/speech 값에 에이전트의 응답이 포함 된 JSON 문서를 받게 된대
                            Fuel.get("/query",
                                    listOf("query" to my_chat_view.inputText.toString() +"#"+ email + "#" + auth_name))
                                    .responseJson { _, _, result ->
                                        val reply = result.get().obj()
                                                .getJSONObject("result")
                                                .getJSONObject("fulfillment")
                                                .getString("speech")

                                        if (reply.contains("http")) {
                                            if (reply.contains("#")) {

                                                var urlset = reply.split("#").toTypedArray()
                                                if(urlset.size == 3){
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setText(urlset[0])
                                                            .build()
                                                    )
                                                    Thread.sleep(6000);
                                                    try {
                                                        var url = URL(urlset[1])
                                                        GetImage_all(url)
                                                        my_chat_view.send(Message.Builder()
                                                                .setUser(agent)
                                                                .hideIcon(true)
                                                                .setPicture(all)
                                                                .setType(Message.Type.PICTURE)
                                                                .build()
                                                        )
                                                    } catch (e: IOException) {
                                                        e.printStackTrace()
                                                    }
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setText(urlset[2])
                                                            .build()
                                                    )
                                                }else if(urlset.size == 4){
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setText(urlset[0])
                                                            .build()
                                                    )
                                                    Thread.sleep(6000);
                                                    for(i in 1..2){
                                                        try {
                                                            var url = URL(urlset[i])
                                                            GetImage_all(url)
                                                            my_chat_view.send(Message.Builder()
                                                                    .setUser(agent)
                                                                    .hideIcon(true)
                                                                    .setPicture(all)
                                                                    .setType(Message.Type.PICTURE)
                                                                    .build()
                                                            )
                                                        } catch (e: IOException) {
                                                            e.printStackTrace()
                                                        }
                                                    }

                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setText(urlset[3])
                                                            .build()
                                                    )
                                                }else{
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setText(urlset[0])
                                                            .build()
                                                    )
                                                    try {
                                                        var url = URL(urlset[urlset.size - 3])
                                                        GetImage_all(url)
                                                        my_chat_view.send(Message.Builder()
                                                                .setUser(agent)
                                                                .hideIcon(true)
                                                                .setPicture(all)
                                                                .setType(Message.Type.PICTURE)
                                                                .build()
                                                        )
                                                        my_chat_view.setOnBubbleClickListener(object : Message.OnBubbleClickListener{
                                                            override fun onClick(message: Message){
                                                                musinsa(urlset[urlset.size-2])                                               }
                                                        })
                                                    } catch (e: IOException) {
                                                        e.printStackTrace()
                                                    }
                                                    for (i in 1..urlset.size - 4) {
                                                        try {
                                                            var url = URL(urlset[i])
                                                            GetImage(url)
                                                            my_chat_view.send(Message.Builder()
                                                                    .setUser(agent)
                                                                    .hideIcon(true)
                                                                    .setPicture(bitmap)
                                                                    .setType(Message.Type.PICTURE)
                                                                    .build()
                                                            )

                                                        } catch (e: IOException) {
                                                            e.printStackTrace()
                                                        }
                                                    }

                                                    //ChatView 위젯 내에서 응답을 렌더링하려면 다른 Message 개체를 다시 빌드해야  합니
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setText(urlset[urlset.size - 1])
                                                            .build()
                                                    )
                                                    Thread.sleep(5000)
                                                }

                                            } else {
                                                try {
                                                    var url = URL(reply)
                                                    GetImage(url)
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .hideIcon(true)
                                                            .setPicture(bitmap)
                                                            .setType(Message.Type.PICTURE)
                                                            .build()
                                                    )

                                                } catch (e: IOException) {
                                                    e.printStackTrace()
                                                }
                                            }
                                        } else {
                                            if (reply.contains("#")) {
                                                var usrlet = reply.split("#").toTypedArray()
                                                for (i in 0..usrlet.size - 1) {
                                                    my_chat_view.send(Message.Builder()
                                                            .setUser(agent)
                                                            .setText(usrlet[i])
                                                            .build()
                                                    )

                                                }
                                            } else {
                                                my_chat_view.send(Message.Builder()
                                                        .setUser(agent)
                                                        .setText(reply)
                                                        .build()
                                                )
                                            }
                                        }
                                    }
                            //Reset edit text
                            my_chat_view.inputText = ""
                        }
                    }catch(e : Exception){
                        e.printStackTrace()
                    }

                }
        )

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                val homeIntent = Intent(this, MainActivity::class.java)
                homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(homeIntent)
                finish()
                return true
            }
            R.id.camera -> {
                val cameraIntent = Intent(this, MainActivity::class.java)
                cameraIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(cameraIntent)
                finish()
                return true
            }
            R.id.chat -> {
                Toast.makeText(this,"이미 챗봇 메뉴에 있습니다.", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return false
    }
}
