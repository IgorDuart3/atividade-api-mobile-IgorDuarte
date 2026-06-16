package com.example.trabalho_n2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val botaoPesquisar = findViewById<Button>(R.id.botaoPesquisar)

        // Abre a tela de consulta (MainActivity) ao tocar no botão.
        botaoPesquisar.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
