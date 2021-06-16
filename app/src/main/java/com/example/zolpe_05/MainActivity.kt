package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.zolpe_05.databinding.ActivityMainBinding
import com.example.zolpe_05.ui.ChatActivity
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
import java.text.SimpleDateFormat
import java.util.*

val num_of_rows = 10
val page_no = 1
val data_type = "JSON"
var base_time = "0800"
var base_date = "20210609"
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
        @Query("dataType") data_type: String,
        @Query("numOfRows") num_of_rows: Int,
        @Query("pageNo") page_no: Int,
        @Query("base_date") base_date: String,
        @Query("base_time") base_time: String,
        @Query("nx") nx: String,
        @Query("ny") ny: String
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
var clothText: String = ""

var clothList = emptyList<String>().toMutableList()

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val db: FirebaseFirestore = Firebase.firestore
    private var itemsCollectionRef = db.collection("Blazer")
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var actionBar: ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()
        var bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        getDateTime()
        val call = ApiObject.retrofitService.GetWeather(data_type, num_of_rows, page_no, base_date, base_time, nx, ny)
        call.enqueue(object : retrofit2.Callback<WEATHER> {
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    Log.d("api", response.body().toString())
                    Log.d("api", response.body()!!.response.body.items.toString()) //날짜, 시간 바꾸면 여기서 뻗음 이유 찾아야해
                    weatherResult = response.body()!!.response.body.items.toString().split("(")
                    setWeatherInfo()
                    setWeatherTextIcon()
                    setClothText()
                    binding.clothMessage.setText(clothText)
                }
            }
            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api", "api connection error")
            }
        })

    }

    fun getDateTime(){
        val now: Long = System.currentTimeMillis()
        val date = Date(now)
        val dateFormat = SimpleDateFormat("yyyyMMdd")
        val timeFormat = SimpleDateFormat("HH")
        val simpleDate = dateFormat.format(date)
        val simpleTime = timeFormat.format(date)

        base_date = simpleDate.toString()
        if (simpleTime.toString() == "02" || simpleTime.toString() == "03" || simpleTime.toString() == "04"){
            base_time = "0200"
        }
        else if(simpleTime.toString() == "05" || simpleTime.toString() == "06" || simpleTime.toString() == "07"){
            base_time = "0500"
        }
        else if(simpleTime.toString() == "08" || simpleTime.toString() == "09" || simpleTime.toString() == "10"){
            base_time = "0800"
        }
        else if(simpleTime.toString() == "11" || simpleTime.toString() == "12" || simpleTime.toString() == "13" ){
            base_time = "1100"
        }
        else if(simpleTime.toString() == "14" || simpleTime.toString() == "15" || simpleTime.toString() == "16"){
            base_time = "1400"
        }
        else if(simpleTime.toString() == "17" || simpleTime.toString() == "18" || simpleTime.toString() == "19"){
            base_time = "1700"
        }
        else if( simpleTime.toString() == "20" || simpleTime.toString() == "21" || simpleTime.toString() == "22" ){
            base_time = "2000"
        }
        else if( simpleTime.toString() == "00" || simpleTime.toString() == "01" ){
            base_date = (simpleDate.toInt()-1).toString()
            base_time = "2300"
        }
        else{ //23시일때
            base_time = "2300"
        }

        Log.d("api", base_date+ base_time)
    }

    fun setClothImage(clothId: String, itemId: Int) {
        var tempId = itemId.toString()
        itemsCollectionRef = db.collection(clothId)
        itemsCollectionRef.document(tempId).get()
            .addOnSuccessListener { // it: DocumentSnapshot
                Glide.with(this).load(it["img"].toString().toUri()).into(binding.clothImageView)
                binding.clothName.setText(it["name"].toString())
                binding.clothPrice.setText(it["price"].toString())
                val link = it["link"].toString().toUri()
                val musinsaIntent = Intent(Intent.ACTION_VIEW)
                musinsaIntent.setData(link)
                binding.clothImageView.setOnClickListener(){
                    startActivity(musinsaIntent)
                }
                binding.musinsaButton.setOnClickListener(){
                    startActivity(musinsaIntent)
                }

            }.addOnFailureListener {
                Log.d("firestore", "firestoreError!")
            }
    }

    fun setClothText(){
        if(temp<=5){
            if(skyStatus==1){
                clothText = "날이 많이 춥지만 맑은 날씨네요! 따뜻하게 코트를 입어보는 것은 어떠신가요?"
            }
            else if(skyStatus==3){
                clothText = "날이 많이 춥고 구름이 많네요.. 따뜻하게 숏패딩를 입어보는 것은 어떠신가요?"
            }
            else if(skyStatus==4){
                clothText = "날이 많이 춥고 흐려서 우중충 하네요~ 따뜻하게 숏패딩를 입어보는 것은 어떠신가요?"
            }
            else if(rainStatus==1||rainStatus==4){
                clothText = "많이 추운 날씨에 비가 주륵주륵 내려요.. 따뜻하게 트렌치 코트를 입어보는 것은 어떠신가요? 우산은 꼭 챙기세요!"
                clothList.add("Trench")
                clothList.add("Umbrella")
            }
            else if(rainStatus==3||rainStatus==2){
                clothText = "날이 춥고 눈이 내려 세상이 하얗네요! 따뜻하게 코트를 입어보는 것은 어떠신가요? 우산을 쓰는 것도 좋을것 같아요~"
                clothList.add("Coat")
                clothList.add("Umbrella")
            }
            else if(rainStatus==5){
                clothText = "날도 많이 춥고 빗방울이 날리는 날이에요! 따뜻하게 숏패딩을 입어보는 것은 어떠신가요? 우산은 챙기는게 좋을 듯 해요~!"
                clothList.add("short_Padding")
                clothList.add("Umbrella")
            }
            else if(rainStatus==6||rainStatus==7){
                clothText="날이 춥고 눈발이 조금 날리는 날이에요! 따뜻하게 코트를 입어보는 것은 어떠신가요?"
                clothList.add("Coat")
            }
            else{
                clothText = "날이 매우 추워요.. 코트나 패딩은 필수 항목인거 아시죠??!!"
            }
            clothList.add("Coat")
            clothList.add("Short_Padding")
            clothList.add("Hood")
            clothList.add("MTM")
            clothList.add("Cotten_Pants")
            clothList.add("Slacks")
            clothList.add("Denim")
        }
        else if(temp in 6..15){
            if(rainStatus==1||rainStatus==4){
                clothText = "날이 쌀쌀하고 비가 내리네요.. 트렌치 코트를 입어보시는건 어때요? 우산은 꼭 챙기세요!!"
                clothList.add("Trench")
                clothList.add("Umbrella")
            }
            if(rainStatus==5){
                clothText= "날이 쌀쌀하고 비가 조금 내리네요! 푸근하게 블루종을 입어보시는건 어때요? 우산은 챙기시는게 좋겠죠?"
                clothList.add("Blouseon")
                clothList.add("Umbrella")
            }
            else if(skyStatus==1){
                clothText="날이 좀 쌀쌀하지만 맑은 날씨에요! 푸근하게 MA-1을 입어보시는것은 어때요?"
            }
            else if(skyStatus==3){
                clothText="날이 쌀쌀하고 구름이 조금 있네요~ 라이더 자켓을 입어보시는 것은 어때요?"
            }
            else if(skyStatus==4){
                clothText="날이 쌀쌀하고 흐려서 어둑어둑 하네요... 트렌치 코트를 입어보시는 것은 어때요?"
            }
            else{
                clothText = "일교차가 큽니다. 자켓이나 코트같은 따뜻한 것을 입고 감기 예방하세요!"
            }
            clothList.add("Blouseon")
            clothList.add("Cardigan")
            clothList.add("MA-1")
            clothList.add("Rider")
            clothList.add("Blouse")
            clothList.add("Shirt")
            clothList.add("Long_Sleeve_Tee")
            clothList.add("Hood")
            clothList.add("MTM")
            clothList.add("Cotten_Pants")
            clothList.add("Slacks")
            clothList.add("Denim")
            clothList.add("Jogger")
            clothList.add("Skirt")
            clothList.add("Jumpsuit")

        }
        else if(temp in 16..23){
            if(rainStatus==1||rainStatus==4){
                clothText = "날이 따뜻하고 비가 내려요! 상큼하게 후드티 입어보시는건 어떤가요? 우산은 챙기시는게 좋을것같네요!"
                clothList.add("Hood")
                clothList.add("Umbrella")
            }
            else if(rainStatus==5){
                clothText="날이 따뜻하고 비가 조금 내리네요! 가디건을 걸쳐보세요! 우산을 챙기면 더 좋을것 같아요!"
                clothList.add("Cardigan")
                clothList.add("Umbrella")
            }
            else if(skyStatus==1){
                clothText="날이 따뜻하고 맑아요! 상큼하게 베스트 어떠세요?"
            }
            else if(skyStatus==3){
                clothText="날이 따뜻하지만 구름이 조금 있네요! 상큼하게 블레이저 어떠신가요?"
            }
            else if(skyStatus==4){
                clothText="날이 따뜻하지만 흐려서 우중충 하네요... 상큼하게 맨투맨 어떠신가요?"
            }
            else{
                clothText = "일교차가 큽니다. 가디건이나 자켓을 챙기세요!"
            }
            clothList.add("Blazer")
            clothList.add("Blouseon")
            clothList.add("Cardigan")
            clothList.add("MA-1")
            clothList.add("Vest")
            clothList.add("Rider")
            clothList.add("Blouse")
            clothList.add("Shirt")
            clothList.add("Hood")
            clothList.add("MTM")
            clothList.add("Cotten_Pants")
            clothList.add("Slacks")
            clothList.add("Denim")
            clothList.add("Jogger")
            clothList.add("Skirt")
            clothList.add("Jumpsuit")
            clothList.add("One_piece")
        }
        else if(temp in 24..29){
            if(rainStatus==1||rainStatus==4){
                clothText = "날이 조금 덥고 비가 내려 습하네요! 선선하게 긴팔티 어떠세요? 우산은 꼭 챙겨야하는거 아시죠?"
                clothList.add("Long_Sleeve_Tee")
                clothList.add("Umbrella")
            }
            else if(rainStatus==5){
                clothText="날이 조금 덥고 비가 조금 내려요! 선선하게 셔츠 어떠신가요? 우산은 챙기셔도 좋아요!"
                clothList.add("Shirt")
                clothList.add("Umbrella")
            }
            else if(skyStatus==1){
                clothText="날이 조금 덥고 맑아요! 선선하게 반팔티 어떠신가요?"
            }
            else if(skyStatus==3){
                clothText="날이 조금 덥고 구름이 많아요~ 선선하게 블라우스 어떠신가요?"
            }
            else if(skyStatus==4){
                clothText="날이 조금 덥고 흐려서 어둑어둑 하네요.. 선선하게 셔츠 어떠신가요?"
            }
            else{
                clothText = "낮에는 덥고 밤에는 선선한 날씨네요! 걸칠 셔츠 정도는 챙기는 센스!"
            }
            clothList.add("Blouse")
            clothList.add("Shirt")
            clothList.add("Short_Sleeve_Tee")
            clothList.add("Long_Sleeve_Tee")
            clothList.add("Cotten_Pants")
            clothList.add("Denim")
            clothList.add("Jogger")
            clothList.add("Skirt")
            clothList.add("Slacks")
            clothList.add("One_piece")
        }
        else{
            if(rainStatus==1||rainStatus==4){
                clothText = "날이 매우 덥고 비가 많이 내려 엄청 습해요!!! 시원하게 반바지 어떠세요? 우산은 필수입니다!"
                clothList.add("Short_Pants")
                clothList.add("Umbrella")
            }
            else if(rainStatus==5){
                clothText="날이 매우 덥고 비가 조금 내려 습하네요... 시원하게 반팔티 어떠세요? 우산은 챙기시는게 좋을것같아요~!"
                clothList.add("Short_Sleeve_Tee")
                clothList.add("Umbrella")
            }
            else if(skyStatus==1){
                clothText="날이 매우 덥고 맑아 열이 오르겠네요... 시원하게 원피스를 입어보세요!"
            }
            else if(skyStatus==3){
                clothText="날이 매우 덥지만 구름이 많아 다행이네요! 시원하게 반팔티 추천드려요~!"
            }
            else if(skyStatus==4){
                clothText="날이 매우 덥고 흐려서 우중충해요~ 시원하게 반바지 어떠세요?"
            }
            else{
                clothText = "너무너무너무 더워요!! 열사병, 일사병 유의하여 옷 입는게 좋아요~!~!"
            }
            clothList.add("Blouse")
            clothList.add("Shirt")
            clothList.add("Short_Sleeved_Tee")
            clothList.add("Short_Pants")
            clothList.add("Skirt")
            clothList.add("One_piece")
        }

        fun doRandom() {
            val list_size = clothList.size
            val random = Random()
            val cloth_random_num = random.nextInt(list_size)
            val cloth_random_id = random.nextInt(19)+1
            Log.d("random_num",cloth_random_num.toString()+"  "+cloth_random_id.toString())
            setClothImage(clothList[cloth_random_num], cloth_random_id)
        }
        doRandom()
        binding.leftArrow.setOnClickListener(){
            doRandom()
        }
        binding.rightArrow.setOnClickListener(){
            doRandom()
        }
    }

    fun setWeatherInfo(){
        var rainPercentInfo = weatherResult[2]
        var rainStatusInfo = weatherResult[3]
        var skyStatusInfo = ""
        var tempInfo = ""
        var findSky1 = weatherResult[7].indexOf("=")
        var findSky2 = weatherResult[7].indexOf(",")
        var findSkyResult = weatherResult[7].slice(IntRange(findSky1 + 1, findSky2 - 1))
        if(findSkyResult=="SKY"){
            skyStatusInfo = weatherResult[7]
        }
        else{
            skyStatusInfo = weatherResult[5]
        }
        var findTempResult = weatherResult[8].slice(IntRange(findSky1 + 1, findSky2 - 1))
        if(findTempResult == "T3H"){
            tempInfo = weatherResult[8]
        }
        else{
            tempInfo = weatherResult[6]
        }

        //bae

        var find1 = rainPercentInfo.lastIndexOf("=")
        var find2 = rainPercentInfo.indexOf(")")
        val find3 = rainStatusInfo.lastIndexOf("=")
        val find4 = rainStatusInfo.indexOf(")")
        val find5 = skyStatusInfo.lastIndexOf("=")
        val find6 = skyStatusInfo.indexOf(")")
        val find7 = tempInfo.lastIndexOf("=")
        val find8 = tempInfo.indexOf(")")
        rainPercent = rainPercentInfo.slice(IntRange(find1 + 1, find2 - 1)).toInt()
        rainStatus = rainStatusInfo.slice(IntRange(find3 + 1, find4 - 1)).toInt()
        skyStatus = skyStatusInfo.slice(IntRange(find5 + 1, find6 - 1)).toInt()
        temp = tempInfo.slice(IntRange(find7 + 1, find8 - 1)).toInt()
    }

    fun setWeatherTextIcon(){
        var rainPercentText: String = ""
        var rainStatusText: String = ""
        var skyStatusText: String = ""
        var tempText: String = ""

        when (rainStatus){
            0 -> rainStatusText = "비가 내릴 가능성이 있고"
            1 -> rainStatusText = "비가 내리겠고"
            2 -> rainStatusText = "진눈개비가 날리겠고"
            3 -> rainStatusText = "눈이 내리겠고"
            4 -> rainStatusText = "소나기가 내리겠고"
            5 -> rainStatusText = "빗방울이 날리겠고"
            6 -> rainStatusText = "비와 눈이 날리겠고"
            7 -> rainStatusText = "눈이 날리겠고"
            else -> rainStatusText = "???"
        }
        if(rainPercent ==0){
            rainPercentText = "\n오늘은 비 소식이 없고"
        }
        else{
            rainPercentText = "\n오늘은 "+ rainPercent+"%의 확률로 " + rainStatusText
        }
        when(skyStatus){
            1 -> skyStatusText = "맑은 날씨입니다."
            3 -> skyStatusText = "구름 많은 날씨입니다."
            4 -> skyStatusText = "흐린 날씨입니다."
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

        if(skyStatus == 3){
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

        if(skyStatus == 4){
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
                Toast.makeText(this, "이미 메인 화면 입니다", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.magazine -> {
                val magazineIntent = Intent(this, MagazineActivity::class.java)
                magazineIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(magazineIntent)
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