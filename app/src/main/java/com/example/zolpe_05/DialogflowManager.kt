package com.example.zolpe_05

import android.content.res.Resources
import com.google.api.Context
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import com.google.protobuf.Struct
import com.google.protobuf.Value
import java.io.InputStream
import java.util.*
//

class DialogflowManager {
    private var client: SessionsClient? = null
    private var session: SessionName? = null
    private val LANGUAGE_CODE = "ko"

    fun initAssistant(context: Context) {
        try {
            val resources = android.content.res.Resources.getSystem()
            val stream = resources.openRawResource(R.raw.credentials)
            val credentials = GoogleCredentials.fromStream(stream)
            val projectId = (credentials as ServiceAccountCredentials).projectId
            val uuid = UUID.randomUUID().toString() //uuid import 맞게했나 아리까리
            val settingsBuilder = SessionsSettings.newBuilder()
            val sessionsSettings =
                settingsBuilder.setCredentialsProvider(FixedCredentialsProvider.create(credentials)).build()


            this.client = SessionsClient.create(sessionsSettings)
            this.session = SessionName.of(projectId, uuid)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    fun detectIntentText(somePayload: Int, text: String): QueryResult {

        if(client == null || session == null){
            throw Exception("Error: no dialogflow client")
        }
        // Set the text (input) and language code (en) for the query
        val textInput = TextInput.newBuilder().setText(text).setLanguageCode(LANGUAGE_CODE)
        // Build the query with the TextInput
        val queryInput = QueryInput.newBuilder().setText(textInput).build()
        //Save received payload into a protobuf Struct
        val somePayload = Value.newBuilder().setNumberValue(somePayload.toDouble()).build()
        val struct = Struct.newBuilder()
        struct.putFields("somePayload", somePayload)
        //Set queryParameters
        val queryParameters = QueryParameters.newBuilder().setPayload(struct).build()
        //Build the request
        val request = DetectIntentRequest.newBuilder()
            .setSession(session.toString())
            .setQueryInput(queryInput)
            .setQueryParams(queryParameters)
            .build()
        // Performs the detect intent request
        val response = client!!.detectIntent(request)
        // Display the query result
        return response.queryResult
    }
}