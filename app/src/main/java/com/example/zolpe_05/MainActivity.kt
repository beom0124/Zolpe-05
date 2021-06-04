package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.zolpe_05.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

//import com.example.firebasedatabase.databinding.ActivityMainBinding
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.ktx.firestore
//import com.google.firebase.ktx.Firebase

val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
val base_time = 2200
val base_date = 20210604
val nx = "55"
val ny = "127"

data class WEATHER(
    val response: RESPONSE
)
data class RESPONSE(
    val header: HEADER,
    val body: BODY
)
data class HEADER(
    val resultCode: Int,
    val resultMsg: String
)
data class BODY(
    val data_type: String,
    val items: ITEMS
)

data class ITEMS(
    val item: List<ITEM>
)
data class ITEM(
    val baseData: Int,
    val baseTime: Int,
    val category: String
)

interface  WeatherInterface{
    @GET("getVilageFcst?serviceKey=pbknASs62KuOAXWykoKe2SgYgmbPllEWyGr2X5LrKa9yB2r5FjsJM1VgU%2B1TPy639oL%2FTqj4JM14Z01CpQMlXg%3D%3D")
    fun GetWeather(
        @Query("dataType") data_type:String,
        @Query("numOfRows") num_of_rows:Int,
        @Query("pageNo") page_no:Int,
        @Query("base_date") base_date:Int,
        @Query("base_time") base_time:Int,
        @Query("nx") nx:String,
        @Query("ny") ny:String
    ): Call<WEATHER>
    //서비스키 인코딩인지 디코딩인지 테스트해봐야함
}



val retrofit = Retrofit.Builder()
    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/") //링크 확인 필요
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object  ApiObject{
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        val rightarrow = binding.rightArrow
        rightarrow.setOnClickListener {
            Toast.makeText(this, "right arrow", Toast.LENGTH_SHORT).show()
        }

        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_date, base_time, nx, ny)
        call.enqueue(object : retrofit2.Callback<WEATHER>{
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if(response.isSuccessful){
                    print("여기까지 옴!!!")
                    //print(response.body().toString())
                    Log.d("api",response.body().toString())
                }
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api","api connection error")
            }
        })


    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.home -> {
                Toast.makeText(this,"이미 메인 화면 입니다",Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.camera -> {
                val cameraIntent = Intent(this, CameraActivity::class.java)
                cameraIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(cameraIntent)
                return true
            }
            R.id.chat -> {
                val chatIntent = Intent(this, ChatActivity::class.java)
                chatIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(chatIntent)
                return true
            }
        }
        return false
    }
}