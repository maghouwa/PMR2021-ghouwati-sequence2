package com.App1.app1

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val backArrowButton = findViewById<ImageView>(R.id.main_back_icon)
        val menuButton = findViewById<ImageView>(R.id.main_menu_icon)

        backArrowButton.setOnClickListener{
            // return to previous activity
            onBackPressed()
        }

        menuButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // finding the button
        val confirmButton = findViewById<Button>(R.id.main_confirm_button)

        // finding the edit text
        val pseudoField = findViewById<EditText>(R.id.pseudo_field)
        val passwordField = findViewById<EditText>(R.id.password_field)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val lastUsedPseudo = sharedPreferences.getString("LAST_USED_PSEUDO", null)

        pseudoField.setText(lastUsedPseudo.toString())

        // Setting On Click Listener
        confirmButton.setOnClickListener {
            val pseudo = pseudoField.text.toString()
            val password = passwordField.text.toString()

            savePseudo(pseudo = pseudo)

            val intent = Intent(this, ChoixListActivity::class.java)
            intent.putExtra("pseudo", pseudo)

            val connected = verifReseau()
            val baseUrl = sharedPreferences.getString("URL", "http://tomnab.fr/todo-api")

            if (connected) {
                val apiHandler = APIHandler(baseUrl = baseUrl!!)
                if (apiHandler.authenticate(
                    user = pseudo,
                    password = password
                )) {
                    intent.putExtra("baseUrl", apiHandler.baseUrl)
                    intent.putExtra("hashToken", apiHandler.hashToken)
                    startActivity(intent)
                }

            }
            else {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.alert_connection_dialogue)
                dialog.setCanceledOnTouchOutside(false)

                dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT)

                val retryWifiCheck = dialog.findViewById<Button>(R.id.retry_wifi_check)
                val proceed_anyway = dialog.findViewById<Button>(R.id.proceed_anyway)

                retryWifiCheck.setOnClickListener {
                    dialog.cancel()
                }

                proceed_anyway.setOnClickListener {
                    Toast.makeText(this,"to be implemented", Toast.LENGTH_SHORT).show()
                }

                dialog.show()
            }
        }
    }

    private fun savePseudo(pseudo: String) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("LAST_USED_PSEUDO", pseudo)
        }.apply()
    }

    private fun verifReseau(): Boolean {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        val cnMngr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cnMngr.activeNetworkInfo
        var sType = "Aucun réseau détecté"
        var bStatut = false
        if (netInfo != null) {
            val netState = netInfo.state
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true
                val netType = netInfo.type
                when (netType) {
                    ConnectivityManager.TYPE_MOBILE -> sType = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> sType = "Réseau wifi détecté"
                }
                Toast.makeText(this, "Connection available", Toast.LENGTH_LONG).show()
            }
        }
        return bStatut
    }
}
