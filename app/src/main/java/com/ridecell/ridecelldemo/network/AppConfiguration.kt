package com.ridecell.ridecelldemo.network

import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class AppConfiguration(val context: Context) {

    private var jsonObject: JSONObject? = null
    private var configName: String? = null

    companion object{
        private const val URLS = "Urls"
        private const val DEFAULT_IDENTIFIER = "appconfiguration"


        @JvmStatic
        private fun getConfigResourceId(context: Context): Int{
            try {
                return context.resources.getIdentifier(DEFAULT_IDENTIFIER,
                    "raw", context.packageName)
            } catch (e: Exception){
                throw RuntimeException("Failed to read appconfiguration.json"
                        + " please check that it is correctly formed.",
                    e)
            }
        }
    }

    init {
        configName = URLS
        readInputJson(context, getConfigResourceId(context))
    }


    /*read json file*/
    private fun readInputJson(context: Context, resourceId: Int){
        try {
            val inputStream = context.resources.openRawResource(
                resourceId)
            val scanner = Scanner(inputStream)
            val sb = StringBuilder()
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine())
            }
            scanner.close()
            jsonObject = JSONObject(sb.toString())

        } catch (e: Exception){
            throw RuntimeException(
                "Failed to read appconfiguration.json please check that it is correctly formed.",
                e)
        }
    }

    /*get json object*/
    fun optJsonObject(key: String): JSONObject? {
        return try {
            val value: JSONObject = jsonObject!!.getJSONObject(key)
            /*if (value.has(this.configName)) {
                value = value.getJSONObject(this.configName)
            }*/
            JSONObject(value.toString())
        } catch (je: JSONException) {
            null
        }
    }

    /*get base url from file*/
    fun getBaseUrl(): String? {
        try {
            val urlsObject = optJsonObject(configName!!)
            if (urlsObject != null) {
                return urlsObject.getString("BaseUrl")
            }
            return null
        } catch (e: JSONException){
            return null
        }
    }
}