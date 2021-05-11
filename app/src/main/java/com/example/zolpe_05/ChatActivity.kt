package com.example.zolpe_05

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

//여기부터 복붙한거
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;

import com.google.api.services.calendar.model.*;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.zolpe_05.databinding.ActivityCameraBinding
import com.example.zolpe_05.databinding.ActivityChatBinding
import com.google.gson.JsonObject
import org.json.JSONObject

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//날씨 import
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



class ChatActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    val binding by lazy { ActivityChatBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var actionBar : ActionBar?

        actionBar = supportActionBar
        actionBar?.hide()

        var bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        val retrofit: Retrofit? = Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }

//    @GET("weather?")
//    fun getCurrentWeather(
//            @Query("lat") lat: String,
//            @Query("lon") lon: String,
//            @Query("APPID") APPID: String)
//            : Call<JsonObject>
//
//
//    fun getCurrentWeather() {
//
//
//        var res: Call<JsonObject> = RetrofitClient
//                .getInstance()
//                .buildRetrofit()
//                .getCurrentWeather(latitude, longitude, OPEN_WEATHER_MAP_KEY)
//
//        res.enqueue(object: Callback<JsonObject> {
//
//            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
//                Log.d(TAG, "Failure : ${t.message.toString()}")
//            }
//
//            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
//                var jsonObj = JSONObject(response.body().toString())
//                Log.d(TAG , "Success :: $jsonObj")
//
//            }
//        })
//    }

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
