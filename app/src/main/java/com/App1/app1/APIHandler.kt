package com.App1.app1

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import android.widget.Toast
import com.App1.app1.model.AuthenticationToken
import com.App1.app1.model.ItemResponse
import com.App1.app1.model.ListResponse
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException
import java.io.Serializable


class APIHandler(val baseUrl: String, var hashToken: String? = null): Serializable {

    private val client = OkHttpClient.Builder().followRedirects(false).build()
    init {
        if (hashToken == null) hashToken = getDefaultHash()!!.token
    }

    private fun getDefaultHash(): AuthenticationToken? {
        val requestUrl = "$baseUrl/authenticate?"
        println(requestUrl)

        val formBody = FormBody.Builder()
            .add("user","tom")
            .add("password", "web")
            .build()

        val request = Request.Builder()
            .addHeader("hash", "977b68e4a3624e35a1b114fadbb3704e")
            .url(requestUrl)
            .post(body = formBody)
            .build()

        var authenticationToken: AuthenticationToken? = null

        val call = client.newCall(request = request)
            call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                authenticationToken = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (authenticationToken == null) {
            Thread.sleep(1)
        }

        return authenticationToken
    }

    fun authenticate(user: String, password: String): Boolean {
        val requestUrl = "$baseUrl/authenticate?"
        println(requestUrl)

        val formBody = FormBody.Builder()
            .add("user",user)
            .add("password", password)
            .build()

        val request = hashToken?.let {
            Request.Builder()
                .addHeader("hash", it)
                .url(requestUrl)
                .post(body = formBody)
                .build()
        }

        var authenticationToken: AuthenticationToken? = null

        val call = request?.let { client.newCall(request = it) }
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                authenticationToken = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (authenticationToken == null) {
            Thread.sleep(1)
        }

        hashToken = authenticationToken!!.token
        return authenticationToken!!.success
    }

    fun getUserLists(): ListResponse? {
        val requestUrl = "$baseUrl/lists?"
        println(requestUrl)

        val formBody = FormBody.Builder()
            .build()

        val request = Request.Builder()
            .addHeader("hash", hashToken!!)
            .url(requestUrl)
            .build()

        var mesListes: ListResponse? = null

        val call = client.newCall(request = request)
        call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()

                mesListes = gson.fromJson(body, ListResponse::class.java)
            }
        })

        while (mesListes == null) {
            Thread.sleep(1)
        }

        return mesListes
    }

    fun addListe(label: String): Boolean {
        val requestUrl = "$baseUrl/lists?"
        println(requestUrl)

        val formBody = FormBody.Builder()
            .add("label",label)
            .build()

        val request = hashToken?.let {
            Request.Builder()
                .addHeader("hash", it)
                .url(requestUrl)
                .post(body = formBody)
                .build()
        }

        var addResponse: AuthenticationToken? = null

        val call = request?.let { client.newCall(request = it) }
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                addResponse = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (addResponse == null) {
            Thread.sleep(1)
        }

        return addResponse!!.success
    }

    fun deleteListe(listeId: Int): Boolean {
        val requestUrl = "$baseUrl/lists/$listeId"

        val mediaType = "text/plain".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, "")

        val request = hashToken?.let {
            Request.Builder()
                .addHeader("hash", it)
                .url(requestUrl)
                .delete(body = requestBody)
                .build()
        }

        var addResponse: AuthenticationToken? = null

        val call = request?.let { client.newCall(request = it) }
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                addResponse = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (addResponse == null) {
            Thread.sleep(1)
        }

        return addResponse!!.success
    }

    fun getListeItems(idListe: Int): ItemResponse? {
        val requestUrl = "$baseUrl/lists/$idListe/items?"
        println(requestUrl)

        val formBody = FormBody.Builder()
            .build()

        val request = Request.Builder()
            .addHeader("hash", hashToken!!)
            .url(requestUrl)
            .build()

        var mesItems: ItemResponse? = null

        val call = client.newCall(request = request)
        call.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()

                mesItems= gson.fromJson(body, ItemResponse::class.java)
            }
        })

        while (mesItems == null) {
            Thread.sleep(1)
        }

        println(mesItems)
        return mesItems
    }

    fun addItem(label: String, idListe: Int, url: String?=null): Boolean {
        val requestUrl = "$baseUrl/lists/$idListe/items?"
        println(requestUrl)

        val formBody = if (url!=null) {
            FormBody.Builder()
                .add("label", label)
                .add("url", url)
                .build()
        } else {
            FormBody.Builder()
                .add("label", label)
                .build()
        }

        val request = hashToken?.let {
            Request.Builder()
                .addHeader("hash", it)
                .url(requestUrl)
                .post(body = formBody)
                .build()
        }

        var addResponse: AuthenticationToken? = null

        val call = request?.let { client.newCall(request = it) }
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                addResponse = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (addResponse == null) {
            Thread.sleep(1)
        }

        return addResponse!!.success
    }

    fun deleteItem(idItem: Int, idListe: Int): Boolean {
        val requestUrl = "$baseUrl/lists/$idListe/items/$idItem"
        println(requestUrl)

        val mediaType = "text/plain".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, "")

        val request = hashToken?.let {
            Request.Builder()
                .addHeader("hash", it)
                .url(requestUrl)
                .delete(body = requestBody)
                .build()
        }

        var addResponse: AuthenticationToken? = null

        val call = request?.let { client.newCall(request = it) }
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                addResponse = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (addResponse == null) {
            Thread.sleep(1)
        }

        return addResponse!!.success
    }

    fun checkUncheckItem(idListe: Int, idItem: Int, check: Int): Boolean {
        val requestUrl = "$baseUrl/lists/$idListe/items/$idItem?check=$check"
        println(requestUrl)

        val mediaType = "text/plain".toMediaTypeOrNull()
        val requestBody = RequestBody.create(mediaType, "")

        val request = hashToken?.let {
            Request.Builder()
                .addHeader("hash", it)
                .url(requestUrl)
                .put(body = requestBody)
                .build()
        }

        var addResponse: AuthenticationToken? = null
        val call = request?.let { client.newCall(request = it) }
        call?.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed Request.")
                println(e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val gson = Gson()
                println(body)
                println("$idItem $idListe $check")
                addResponse = gson.fromJson(body, AuthenticationToken::class.java)
            }
        })

        while (addResponse == null) {
            Thread.sleep(1)
        }

        return addResponse!!.success
    }


}
