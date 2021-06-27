package com.App1.app1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ChoixListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        val bundle = intent.extras
        val pseudo = bundle?.get("pseudo").toString()
        val baseUrl = bundle?.get("baseUrl").toString()
        val hashToken = bundle?.get("hashToken").toString()
        val apiHandler = APIHandler(baseUrl=baseUrl, hashToken=hashToken)

        val newListField = findViewById<EditText>(R.id.editText2)
        val confirmButton = findViewById<Button>(R.id.choix_list_confirm_button)

        val backButton = findViewById<ImageView>(R.id.choix_back_icon)

        val recyclerView = findViewById<RecyclerView>(R.id.liste_list)
        val layoutManager = LinearLayoutManager(this)

        var adapter = ListeAdapter(pseudo, apiHandler, ArrayList())
        adapter.addData(apiHandler.getUserLists()!!.mesListes)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        backButton.setOnClickListener{
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val newListName = newListField.text.toString()
            apiHandler.addListe(label = newListName)
            adapter = ListeAdapter(pseudo, apiHandler, ArrayList())
            adapter.addData(apiHandler.getUserLists()!!.mesListes)
            recyclerView.adapter = adapter
        }
    }

}