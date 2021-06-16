package com.example.zolpe_05.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.MainActivity
import com.example.zolpe_05.R
import com.google.android.material.bottomnavigation.BottomNavigationView

import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zolpe_05.MagazineActivity
//import com.example.zolpe_05.DialogflowManager
import com.example.zolpe_05.data.Message
import com.example.zolpe_05.utils.Constants.RECEIVE_ID
import com.example.zolpe_05.utils.Constants.SEND_ID
import com.example.zolpe_05.utils.BotResponse
import com.example.zolpe_05.utils.Constants.OPEN_GOOGLE
import com.example.zolpe_05.utils.Constants.OPEN_SEARCH
import com.example.zolpe_05.utils.Time
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*

class ChatActivity : AppCompatActivity(){

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        recyclerView()
        clickEvents()

        customBotMessage("안녕하세요, 오늘의 코디입니다. \n 지금 채팅을 시작해주세요!")

        val actionbar = supportActionBar
        actionbar!!.title = " 오늘의 코디"
        //actionbar backbutton 설정
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.apply {
            //actionbar background color 설정
            setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#0D0463")
                )
            )
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun clickEvents() {

        //메시지 보내기
        btn_send.setOnClickListener {
            sendMessage()
        }

        //사용자가 텍스트 보기를 클릭할 때 올바른 위치로 스크롤? 하기
        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    rv_messages.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        //메시지가 있는 경우 앱을 다시 열 때 아래로 스크롤하는 기능
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(Message(message, SEND_ID, timeStamp))
            et_message.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rv_messages.scrollToPosition(adapter.itemCount - 1)


            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //그냥 딜레이
            delay(1000)

            withContext(Dispatchers.Main) {
                //응답 받아오기
                val response = BotResponse.basicResponses(message)

                //리스트에 추가
                messagesList.add(Message(response, RECEIVE_ID, timeStamp))

                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                //최신 메시지 위치로 스크롤
                rv_messages.scrollToPosition(adapter.itemCount - 1)

                //구글 시작 -우리 필요한가?
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}