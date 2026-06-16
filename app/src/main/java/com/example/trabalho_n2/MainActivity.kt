package com.example.trabalho_n2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var inputCodigo: EditText
    private lateinit var botaoConsultar: Button
    private lateinit var areaResultado: TextView
    private lateinit var progresso: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputCodigo = findViewById(R.id.inputCodigo)
        botaoConsultar = findViewById(R.id.botaoConsultar)
        areaResultado = findViewById(R.id.areaResultado)
        progresso = findViewById(R.id.progresso)

        botaoConsultar.setOnClickListener {
            val codigo = inputCodigo.text.toString().trim()

            // Validação de campo vazio (requisito obrigatório)
            if (codigo.isEmpty()) {
                inputCodigo.error = "Digite um código de barras"
                return@setOnClickListener
            }

            consultarProduto(codigo)
        }
    }

    private fun consultarProduto(codigo: String) {
        progresso.visibility = View.VISIBLE
        areaResultado.text = ""

        // O parâmetro fields limita a resposta apenas aos campos que vamos usar,
        // deixando o JSON muito menor e o parsing mais simples.
        val url = "https://world.openfoodfacts.org/api/v2/product/$codigo" +
                "?fields=product_name,brands,quantity,nutrition_grades,ingredients_text"

        val requisicao = JsonObjectRequest(
            Request.Method.GET, url, null,
            { resposta ->
                progresso.visibility = View.GONE
                tratarResposta(resposta)
            },
            { erro ->
                progresso.visibility = View.GONE

                // Distinção importante:
                // - networkResponse == null  -> o servidor NÃO respondeu.
                //   É erro real de conexão (sem internet, timeout, DNS).
                // - networkResponse != null  -> o servidor RESPONDEU, mas com
                //   um código de erro HTTP (404, 503...). Não é problema de
                //   internet do usuário. A Open Food Facts retorna erro HTTP
                //   para códigos de barras em formato inválido (ex.: 12 dígitos).
                val resposta = erro.networkResponse
                if (resposta == null) {
                    areaResultado.text =
                        "Não foi possível conectar. Verifique sua internet e tente novamente."
                } else {
                    areaResultado.text = when (resposta.statusCode) {
                        404 -> "Produto não encontrado para este código de barras."
                        429, 503 -> "Serviço indisponível no momento. Tente novamente em instantes."
                        else -> "Não foi possível obter os dados (erro ${resposta.statusCode})."
                    }
                }
            }
        )

        Volley.newRequestQueue(this).add(requisicao)
    }

    private fun tratarResposta(resposta: JSONObject) {
        // PONTO-CHAVE: produto inexistente com código VÁLIDO volta como
        // HTTP 200 com status = 0. Por isso o "não encontrado" desse caminho
        // é tratado AQUI, no sucesso, e não no listener de erro de rede.
        val status = resposta.optInt("status", 0)
        if (status != 1) {
            areaResultado.text = "Produto não encontrado para este código de barras."
            return
        }

        val produto = resposta.optJSONObject("product")
        if (produto == null) {
            areaResultado.text = "Resposta inesperada da API."
            return
        }

        // optString com fallback evita quebrar quando um campo não existe
        // (comum em produtos com cadastro incompleto na base).
        val nome = produto.optString("product_name", "").ifBlank { "Não informado" }
        val marca = produto.optString("brands", "").ifBlank { "Não informada" }
        val quantidade = produto.optString("quantity", "").ifBlank { "Não informada" }
        val nutriscore = produto.optString("nutrition_grades", "")
            .ifBlank { "Não disponível" }
            .uppercase()
        val ingredientes = produto.optString("ingredients_text", "")
            .ifBlank { "Não informados" }

        val resultado = buildString {
            append("Produto: ").append(nome).append("\n\n")
            append("Marca: ").append(marca).append("\n\n")
            append("Quantidade: ").append(quantidade).append("\n\n")
            append("Nutri-Score: ").append(nutriscore).append("\n\n")
            append("Ingredientes: ").append(ingredientes)
        }

        areaResultado.text = resultado
    }
}