package com.example.zolpe_05.utils

import com.example.zolpe_05.utils.Constants.OPEN_GOOGLE
import com.example.zolpe_05.utils.Constants.OPEN_SEARCH
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object BotResponse {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {

            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I flipped a coin and it landed on $result"
            }

            //챗봇 기본 질문 대답! ->기본적인건 dialogflow말고 여기 넣자
            message.contains("안녕") -> {
                when (random) {
                    0 -> "안녕하세요! 오늘의 코디입니다~"
                    1 -> "오늘의 코디입니다!"
                    2 -> "무엇이 궁금하신가요?"
                    else -> "error" }
            }

            message.contains("오류는 배때문이야") -> {
                when (random) {
                    0 -> "배때문!"
                    1 -> "배때문이야 배때문이야"
                    2 -> "어케 알았누 시팔"
                    else -> "error" }
            }

            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            //프로그램 응답 애매할때 3가지로 나눠 대답
            else -> {
                when (random) {
                    0 -> "이해할 수 없어요."
                    1 -> "다시 말씀해 주시겠어요?"
                    2 -> "학습되지 않은 말이에요!"
                    else -> "error"
                }
            }
        }
    }
}