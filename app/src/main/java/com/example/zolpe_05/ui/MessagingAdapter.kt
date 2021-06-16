package com.example.zolpe_05.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.zolpe_05.R
import com.example.zolpe_05.data.Message
import com.example.zolpe_05.utils.Constants.RECEIVE_ID
import com.example.zolpe_05.utils.Constants.SEND_ID
import kotlinx.android.synthetic.main.message_item.view.*
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.net.URI

class MessagingAdapter: RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<Message>()
    lateinit var context: Context
    private val db: FirebaseFirestore = Firebase.firestore
    private var itemsCollectionRef = db.collection("category_Cadual")
    var calledStyle = ""
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                //Remove message on the item clicked
                messagesList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        context = parent.context //context값 지정해줌
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder( holder: MessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]
        val imageViewParams = holder.itemView.imageView.layoutParams
        when (currentMessage.id) {
            SEND_ID -> { //사용자가 보내는 메시지
                holder.itemView.tv_message.apply {
                    if(currentMessage.message.contains("날씨") || currentMessage.message.contains("안녕") || currentMessage.message.contains("하이") || currentMessage.message.contains("이동") || currentMessage.message.contains("무신사")){
                        imageViewParams.width = 0
                        imageViewParams.height = 0
                    }
                    text = currentMessage.message
                    visibility = View.VISIBLE
                    findStyle(text.toString())
                }
                holder.itemView.tv_bot_message.visibility = View.GONE
            }
            RECEIVE_ID -> { //챗봇에서 보내는 메시지
                holder.itemView.tv_bot_message.apply {
                    text = currentMessage.message
                    visibility = View.VISIBLE
                }
                Log.d(
                    "messageListSize", messagesList.size.toString())//대화할수록 messageList의 크기는 계속 늘어남 처음에는 1
                if (messagesList.size == 1) { //처음 시작이나 챗봇의 대답에 사진이 필요하지 않은경우
                    imageViewParams.width = 0
                    imageViewParams.height = 0
                }
                else { //처음 이후에는
                    if (calledStyle == "") {
                        imageViewParams.width = 0
                        imageViewParams.height = 0
                    } else {
                        val random = (0..20).random()
                        itemsCollectionRef = db.collection(calledStyle)
                        itemsCollectionRef.document(random.toString()).get().addOnSuccessListener {
                            holder.itemView.imageView.visibility = View.VISIBLE
                            Log.d("image",it["image"].toString())
                            Glide.with(context).load(it["image"].toString().toUri())
                                .into(holder.itemView.imageView)
                            imageViewParams.width = 800
                            imageViewParams.height = 1050
                        }
                    }
                }
                holder.itemView.tv_message.visibility = View.GONE
            }
        }
    }


    fun findStyle(input: String){
        if(input.contains("캐쥬얼")||input.contains("캐주얼")||input.contains("케쥬얼")||input.contains("케주얼")||input.contains("캐듀얼")||input.contains("케듀얼")||input.contains(" Cadual")||input.contains("cadual")||input.contains("무난")||input.contains("평범")){
            calledStyle = "category_Cadual"
        }
        else if(input.contains("캠퍼스")||input.contains("켐퍼스")||input.contains("대학생")||input.contains("Campus")||input.contains("campus")||input.contains("학교")||input.contains("대학교")||input.contains("대딩")||input.contains("개강")){
            calledStyle = "category_Campus"
        }
        else if(input.contains("댄디")||input.contains("덴디")||input.contains("Dandy")||input.contains("dand")||input.contains("멋")||input.contains("우아")){
            calledStyle = "category_Dandy"
        }
        else if(input.contains("데이트")||input.contains("데이트")||input.contains("대이트")||input.contains("date")||input.contains("Date")||input.contains("파티")||input.contains("소개팅")||input.contains("축제")||input.contains("페스티벌")){
            calledStyle = "category_Date"
        }
        else if(input.contains("스포츠")||input.contains("운동")||input.contains("Sport")||input.contains("sport")||input.contains("런닝")||input.contains("체육")||input.contains("헬스")){
            calledStyle = "category_Sports"
        }
        else if(input.contains("스트릿")||input.contains("Street")||input.contains("street")||input.contains("유행")){
            calledStyle = "category_street"
        }
        else if(input.contains("비즈니스")||input.contains("포멀")||input.contains("비지니스")||input.contains("Business")||input.contains("business")||input.contains("회사")||input.contains("입사")||input.contains("정장")||input.contains("면접")){
            calledStyle = "category_Business"
        }
        else{
            calledStyle = ""
        }
    }

    fun insertMessage(message: Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }

}
