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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
val base_time = 2000
val base_date = 20210606
val nx = "60"
val ny = "127" //성북구 삼선동 좌표임

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
    val category: String,
    val fcstValue: String
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
}

val retrofit = Retrofit.Builder()
    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object  ApiObject{
    val retrofitService: WeatherInterface by lazy {
        retrofit.create(WeatherInterface::class.java)
    }
}
var weatherResult = emptyList<String>()
var rainPercent: Int = 0
var rainStatus: Int = 0
var skyStatus: Int = 0
var temp: Int = 0
var weatherText: String = ""


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("Blazer")
    var names = emptyList<String>()
    var prices = emptyList<String>()
    var imgs = emptyList<String>()

    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)


        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_date, base_time, nx, ny)
        call.enqueue(object : retrofit2.Callback<WEATHER>{
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if(response.isSuccessful){
                    Log.d("weather",response.body()!!.response.body.items.toString()) //날짜, 시간 바꾸면 여기서 뻗음 이유 찾아야해
                    weatherResult =response.body()!!.response.body.items.toString().split("(")
                    setWeatherInfo()
                    setWeatherTextIcon()
                }
            }
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api","api connection error")
            }
        })
    }

    fun setClothInfo(){
        var tempRef = db.collection("Blazer")
        if (temp <= 5){
            if(rainStatus>=1){ //비가오면
                tempRef = db.collection("Trench_Coat")
            }

            //itemsCollectionRef = db.collection("Coat")
        }
        else if(temp in 5..15){
            if(rainStatus>=1){
                //트랜치
            }

        }
        else if(temp in 16..25){

        }
        else if(temp >=26){

        }
        else{
            Log.d("tempCloth","temp error!")
        }
    }

    fun setWeatherInfo(){
        var rainPercentInfo = weatherResult[2]
        var rainStatusInfo = weatherResult[3]
        var skyStatusInfo = weatherResult[7] //얘랑 날씨가 5,6값 줘야할때도 있음 왔다갔다혀
        var tempInfo = weatherResult[8]
        var find1 = rainPercentInfo.lastIndexOf("=")
        var find2 = rainPercentInfo.indexOf(")")
        val find3 = rainStatusInfo.lastIndexOf("=")
        val find4 = rainStatusInfo.indexOf(")")
        val find5 = skyStatusInfo.lastIndexOf("=")
        val find6 = skyStatusInfo.indexOf(")")
        val find7 = tempInfo.lastIndexOf("=")
        val find8 = tempInfo.indexOf(")")
        rainPercent = rainPercentInfo.slice(IntRange(find1+1,find2-1)).toInt()
        rainStatus = rainStatusInfo.slice(IntRange(find3+1,find4-1)).toInt()
        skyStatus = skyStatusInfo.slice(IntRange(find5+1,find6-1)).toInt()
        temp = tempInfo.slice(IntRange(find7+1,find8-1)).toInt()
    }

    fun setWeatherTextIcon(){
        var rainPercentText: String = ""
        var rainStatusText: String = ""
        var skyStatusText: String = ""
        var tempText: String = ""

        when (rainStatus){
            0-> rainStatusText  = "비가 내릴 가능성이 있고"
            1-> rainStatusText = "비가 내리겠고"
            2-> rainStatusText = "진눈개비가 날리겠고"
            3-> rainStatusText = "눈이 내리겠고"
            4-> rainStatusText = "소나기가 내리겠고"
            5-> rainStatusText = "빗방울이 날리겠고"
            6-> rainStatusText = "비와 눈이 날리겠고"
            7-> rainStatusText = "눈이 날리겠고"
            else -> rainStatusText = "???"
        }
        if(rainPercent ==0){
            rainPercentText = "오늘은 비 소식이 없고"
        }
        else{
            rainPercentText = "오늘은 "+ rainPercent+"%의 확률로 " + rainStatusText
        }
        when(skyStatus){
            1-> skyStatusText = "맑은 날씨입니다."
            3-> skyStatusText = "구름 많은 날씨입니다."
            4-> skyStatusText = "흐린 날씨입니다."
            else -> skyStatusText = "???"
        }
        tempText = temp.toString() + "°C의 "

        if(skyStatus == 1){
            if(rainStatus ==0){
                binding.weatherImageView.setImageResource(R.drawable.sun)
            }
            else if(rainStatus == 1 || rainStatus == 4 || rainStatus == 5){
                binding.weatherImageView.setImageResource(R.drawable.suncloudrain)
            }
            else if(rainStatus ==2 || rainStatus == 6 || rainStatus == 7){
                binding.weatherImageView.setImageResource(R.drawable.suncloudsnow)
            }
        }

        if(skyStatus == 2){
            if(rainStatus ==0){
                binding.weatherImageView.setImageResource(R.drawable.suncloud)
            }
            else if(rainStatus == 1 || rainStatus == 4 || rainStatus == 5){
                binding.weatherImageView.setImageResource(R.drawable.suncloudrain)
            }
            else if(rainStatus ==2 || rainStatus == 6 || rainStatus == 7){
                binding.weatherImageView.setImageResource(R.drawable.suncloudsnow)
            }
        }

        if(skyStatus == 3){
            if(rainStatus ==0){
                binding.weatherImageView.setImageResource(R.drawable.cloud)
            }
            else if(rainStatus == 1 || rainStatus == 4 || rainStatus == 5){
                binding.weatherImageView.setImageResource(R.drawable.cloudrain)
            }
            else if(rainStatus ==2 || rainStatus == 6 || rainStatus == 7){
                binding.weatherImageView.setImageResource(R.drawable.cloudsnow)
            }
        }

        weatherText = rainPercentText + "\n"+ tempText + skyStatusText
        binding.weatherMessage.setText(weatherText)
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