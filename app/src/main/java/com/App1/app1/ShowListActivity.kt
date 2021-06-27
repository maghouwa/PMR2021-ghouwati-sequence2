package com.App1.app1

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class ShowListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val bundle = intent.extras
        val baseUrl = bundle?.get("baseUrl").toString()
        val hashToken = bundle?.get("hashToken").toString()
        val listeId: Int? = bundle?.getInt("listeId")
        val apiHandler = APIHandler(baseUrl=baseUrl, hashToken=hashToken)

        val newItemField = findViewById<EditText>(R.id.show_field)
        val confirmButton = findViewById<Button>(R.id.show_list_confirm_button)

        val backButton = findViewById<ImageView>(R.id.show_back_icon)


        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        val layoutManager = LinearLayoutManager(this)
        var adapter = ItemAdapter(listeId!!, apiHandler, ArrayList())
        adapter.addData(apiHandler.getListeItems(idListe = listeId)!!.mesItems)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        backButton.setOnClickListener{
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val newItemName = newItemField.text.toString()
            apiHandler.addItem(label = newItemName, idListe = listeId)
            adapter = ItemAdapter(listeId, apiHandler, ArrayList())
            adapter.addData(apiHandler.getListeItems(idListe = listeId)!!.mesItems)
            recyclerView.adapter = adapter
        }
    }
}