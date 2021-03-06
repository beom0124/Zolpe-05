package com.example.zolpe_05.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.R
import android.util.Log
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zolpe_05.data.Message
import com.example.zolpe_05.utils.Constants.RECEIVE_ID
import com.example.zolpe_05.utils.Constants.SEND_ID
import com.example.zolpe_05.utils.Time
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.coroutines.*
import java.lang.Math.random

class ChatActivity : AppCompatActivity(){

    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter

    var weatherText = ""
    var temp = 0
    var listSize =0
    var clothList = emptyList<String>().toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        recyclerView()
        clickEvents()
        customBotMessage("안녕하세요, 오늘의 코디입니다. \n 지금 채팅을 시작해보세요!")
        val actionbar = supportActionBar
        actionbar!!.title = " 코디 챗봇"
        actionbar.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.apply {
            setBackgroundDrawable(
                ColorDrawable(
                    Color.parseColor("#0D0463")
                )
            )
        }

        if(intent.hasExtra("weatherText")){ //MainActivity에서 변수 받아옴
            weatherText = intent.getStringExtra("weatherText").toString()
        }
        if(intent.hasExtra("temp")){
            temp = intent.getIntExtra("temp",0)
        }
        if(intent.hasExtra("listSize")){
            listSize = intent.getIntExtra("listSize",0)
        }
        for (i in 0..listSize-1){
            var index = "clothName"+i.toString()
            var tempString = ""
            tempString = intent.getStringExtra(index).toString()
            clothList.add(tempString)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun clickEvents() { //메시지 클릭시 처리
        btn_send.setOnClickListener {
            sendMessage()
        }
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
            delay(1000)

            withContext(Dispatchers.Main) {
                val response = basicResponses(message)

                messagesList.add(Message(response, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))
                rv_messages.scrollToPosition(adapter.itemCount - 1)
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

    fun basicResponses(_message: String): String { //챗봇이 보내는 답장을 처리

        val message =_message.toLowerCase()
        var textToReturn = ""
        var weatherText = ""
        var temp = 0

        var r = (0..listSize).random()
        if(intent.hasExtra("weatherText")){
            weatherText = intent.getStringExtra("weatherText").toString()
        }
        if(intent.hasExtra("temp")){
            temp = intent.getIntExtra("temp",temp)
        }

        //응답문 결정을 위한 Logic
        if(message.contains("안녕")||message.contains("하이")){

            val random = (0..2).random()
            when(random){
                0 -> textToReturn = "안녕하세요! 오늘의 코디입니다~"
                1 -> textToReturn = "오늘의 코디입니다!"
                2 -> textToReturn = "무엇이 궁금하신가요?"
            }
        }
        else if(message.contains("캐쥬얼")||message.contains("캐주얼")||message.contains("케쥬얼")||message.contains("케주얼")||message.contains("캐듀얼")||message.contains("케듀얼")||message.contains(" Cadual")||message.contains("cadual")||message.contains("무난")||message.contains("평범")||message.contains("캠퍼스")||message.contains("켐퍼스")||message.contains("대학생")||message.contains("Campus")||message.contains("campus")||message.contains("학교")||message.contains("대학교")||message.contains("대딩")||message.contains("개강")||message.contains("댄디")||message.contains("덴디")||message.contains("Dandy")||message.contains("dand")||message.contains("멋")||message.contains("우아")||message.contains("데이트")||message.contains("대이트")||message.contains("date")||message.contains("Date")||message.contains("파티")||message.contains("소개팅")||message.contains("축제")||message.contains("페스티벌")||message.contains("스포츠")||message.contains("운동")||message.contains("Sport")||message.contains("sport")||message.contains("런닝")||message.contains("체육")||message.contains("헬스")||message.contains("스트릿")||message.contains("Street")||message.contains("street")||message.contains("유행")||message.contains("비즈니스")||message.contains("비지니스")||message.contains("Business")||message.contains("business")||message.contains("회사")||message.contains("입사")||message.contains("일")||message.contains("정장")||message.contains("면접")){
            val r_k = (0..4).random()
            when(r_k){
                0-> textToReturn = "이런 조합은 어떠신가요?"
                1-> textToReturn = "이런 옷은 마음에 드시나요?"
                2-> textToReturn = "이렇게 입어보시는 것은 어떠신가요?"
                3-> textToReturn = "이렇게 입어보는 것도 좋을 것 같은데요?!"
                4-> textToReturn = "이런 패션 어때요?"
            }
        }
        else if(message.contains("날씨")||message.contains("weather")){

            var x =0
            if(temp<=5){
                if(weatherText.contains("맑")){
                    x = 1
                }
                else if(weatherText.contains("비")){
                    x = 2
                }
                else if(weatherText.contains("흐림")){
                    x = 3
                }
                else if(weatherText.contains("구름")){
                    x = 4
                }
                else if(weatherText.contains("눈")){
                    x = 5
                }
                else {
                    x = 6
                }
            }
            else if(temp>5||temp<=15){
                if(weatherText.contains("맑")){
                    x = 7
                }
                else if(weatherText.contains("비")){
                    x = 8
                }
                else if(weatherText.contains("흐림")){
                    x = 9
                }
                else if(weatherText.contains("구름")){
                    x = 10
                }
                else {
                    x = 11
                }
            }
            else if(temp>15||temp<=23){
                if(weatherText.contains("맑")){
                    x = 12
                }
                else if(weatherText.contains("비")){
                    x = 13
                }
                else if(weatherText.contains("흐림")){
                    x = 14
                }
                else if(weatherText.contains("구름")){
                    x = 15
                }
                else {
                    x = 16
                }
            }
            else if(temp>23||temp<=29){
                if(weatherText.contains("맑")){
                    x = 17
                }
                else if(weatherText.contains("비")){
                    x = 18
                }
                else if(weatherText.contains("흐")){
                    x = 19
                }
                else if(weatherText.contains("구름")){
                    x = 20
                }
                else {
                    x = 21
                }
            }
            else if(temp>29){
                if(weatherText.contains("맑")){
                    x = 22
                }
                else if(weatherText.contains("비")){
                    x = 23
                }
                else if(weatherText.contains("흐림")){
                    x = 24
                }
                else if(weatherText.contains("구름")){
                    x = 25
                }
                else {
                    x = 26
                }
            }
            else{
                x = 27
            }
            when(x){
                1-> textToReturn = weatherText+"이런 추운 날씨에는 "+clothList[r]+"가 좋을것 같아요!"
                2-> textToReturn = weatherText+"이런 춥고 비가 오는 날씨에는 "+clothList[r]+"가 낫겠죠?!"
                3-> textToReturn = weatherText+"이런 춥고 흐린 날씨에는 "+clothList[r]+"가 딱이죠!ㅎㅎ"
                4-> textToReturn = weatherText+"이런 춥고 구름있는 날씨에는 "+clothList[r]+" 어때요?"
                5-> textToReturn = weatherText+"이런 춥고 눈이오는 날씨에는 당연히 "+clothList[r]+"죠!"
                6-> textToReturn = weatherText+"날씨가 왜이러죠?!!"
                7-> textToReturn = weatherText+"날씨가 좀 쌀쌀하니 "+clothList[r]+" 어떤가요?"
                8-> textToReturn = weatherText+"이렇게 쌀쌀하고 비내리는 날씨에는 "+clothList[r]+"가 좋겠어요 ㅎㅎ"
                9-> textToReturn = weatherText+"흐리고 쌀쌀하니 "+clothList[r]+"가 좋을것 같아요!"
                10-> textToReturn = weatherText+"쌀쌀한데 구름이 좀 많으니"+clothList[r]+"가 좋겠네요!"
                11-> textToReturn = weatherText+"날씨가 왜이러죠?"
                12-> textToReturn = weatherText+"선선하니 "+clothList[r]+"가 좋을것 같아요!"
                13-> textToReturn = weatherText+"비가오니 일교차가 클꺼같아요! "+clothList[r]+"는(은) 챙기는게 좋겠어요!"
                14-> textToReturn = weatherText+"우중충 하니 감기 조심해야해요! "+clothList[r]+"챙겨서 감기 조심하세요!"
                15-> textToReturn = weatherText+"선선하니 "+clothList[r]+"가 좋을것 같아요!"
                16-> textToReturn = weatherText+"날씨가 왜이러죠?"
                17-> textToReturn = weatherText+"따듯하네요! "+clothList[r]+"가 좋을것 같아요!"
                18-> textToReturn = weatherText+"따듯한데 비가 오니 "+clothList[r]+" 어때요?"
                19-> textToReturn = weatherText+"날씨가 구리네요... "+clothList[r]+" 어떤가요?!"
                20-> textToReturn = weatherText+"좋은 날씨에요! "+clothList[r]+"가 좋을것 같아요!"
                21-> textToReturn = weatherText+"날씨가 왜이러죠?"
                22-> textToReturn = weatherText+"좋은 날씨지만 매우 더워요! "+clothList[r]+"로 시원하게!"
                23-> textToReturn = weatherText+"으 찝찝한데요... "+clothList[r]+" 입어서 기분전환 어때요?!"
                24-> textToReturn = weatherText+"덥고 구리구리한 날씨에요...ㅠㅠ "+clothList[r]+"가 최적이네요 ㅎㅎ "
                25-> textToReturn = weatherText+"좋은 날씨네요! "+clothList[r]+"가 좋겠죠?!"
                26-> textToReturn = weatherText+"날씨가 왜이러죠?"
                27-> textToReturn = "난수오류!"

            }
        }
        else if(message.contains("추천")||message.contains("코디")||message.contains("recom")){
            val y = (0..14).random()
            when(y) {

                0-> textToReturn = "오늘의 추천 코디는"+clothList[r]+"입니다!"
                1-> textToReturn = clothList[r]+" 같은 옷은 어떠신가요?"
                2-> textToReturn = "오늘은 "+clothList[r]+" 이 옷을 추천하고 싶네요!"
                3-> textToReturn = "음... 제가 보기에는 "+clothList[r]+" 이 옷이 괜찮을것 같아요!"
                4-> textToReturn = "혹시 "+clothList[r]+" 이런 옷은 어떠세요?"
                5-> textToReturn = "오늘 "+clothList[r]+"이 옷을 입으면 당신도 연예인!"
                6-> textToReturn = clothList[r]+" 이런 옷도 정말 잘 어울릴꺼 같아요!"
                7-> textToReturn = "오늘의 코디 추천해드릴께요!"+"\n"+"오늘의 추천 코디는 "+clothList[r]+"입니다!"
                8-> textToReturn = "5~~늘의 추천 코디는 바로~~~~ "+clothList[r]+"입니다!!!!!"
                9-> textToReturn = "내 친히 너에게 코오디를 추천 해주겠노라.\n"+clothList[r]+" 이 옷을 하사하겠노라!"
                10-> textToReturn = clothList[r]+"이 옷 정말 잘어울릴꺼같아요!"
                11-> textToReturn = "오늘은!! \n"+clothList[r]+"이 옷이 좋겠네요!"
                12-> textToReturn = "오늘 제가 추천드리고 싶은 코디는 "+clothList[r]+" 이 옷 입니다!"
                13-> textToReturn = clothList[r]+"이 옷이 오늘 정말 잘 어울릴꺼같아요!"
                14-> textToReturn = "오늘 추천 코디는 "+clothList[r]+" 이 옷이 잘 어울리겠네요!"
            }
        }

        else if(message.contains("무신사")&&(message.contains("열어")||message.contains("보여"))){
            val link = "https://store.musinsa.com/app/".toUri()
            val musinsaIntent = Intent(Intent.ACTION_VIEW)
            musinsaIntent.setData(link)
            startActivity(musinsaIntent)
        }

        else if(message.contains("네이버")&&(message.contains("열어")||message.contains("보여"))){
            val link = "https://www.naver.com/".toUri()
            val musinsaIntent = Intent(Intent.ACTION_VIEW)
            musinsaIntent.setData(link)
            startActivity(musinsaIntent)
        }

        else if(message.contains("종료")||message.contains("꺼")||message.contains("닫아")){
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        }

        else{ //학습되지 않은 입력인 경우
            val random = (0..2).random()
            when(random){
                0-> textToReturn = "학습되지 않은 말입니다!"
                1-> textToReturn = "다시 말씀해주세요!"
                2-> textToReturn = "이해하지 못 했어요"
            }
        }
        return textToReturn
    }
}