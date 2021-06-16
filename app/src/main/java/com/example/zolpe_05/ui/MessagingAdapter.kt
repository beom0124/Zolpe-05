package com.example.zolpe_05.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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
                    text = currentMessage.message
                    visibility = View.VISIBLE
                    findStyle(currentMessage.message)
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
                else { //처음 이후에는는
                    imageViewParams.width = 800
                    imageViewParams.height = 1050
                    val random = (0..20).random()
                    itemsCollectionRef = db.collection(calledStyle)
                    itemsCollectionRef.document(random.toString()).get().addOnSuccessListener {
                        Glide.with(context).load(it["image"].toString().toUri()).into(holder.itemView.imageView)
                    }
                }
                holder.itemView.tv_message.visibility = View.GONE
            }
        }
    }

    fun findStyle(input: String){
        if(input.contains("캐쥬얼")){
            calledStyle = "category_Cadual"
        }
        else if(input.contains("캠퍼스")){
            calledStyle = "category_Campus"
        }
        else if(input.contains("댄디")){
            calledStyle = "category_Dandy"
        }
        else if(input.contains("데이트")){
            calledStyle = "category_Date"
        }
        else if(input.contains("스포츠")||input.contains("운동")){
            calledStyle = "category_Sports"
        }
        else if(input.contains("스트릿")){
            calledStyle = "category_street"
        }


        else{
            calledStyle = "category_Cadual"
        }
    }


    fun insertMessage(message: Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }

}