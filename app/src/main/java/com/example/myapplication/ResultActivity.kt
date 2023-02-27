package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ResultActivity : AppCompatActivity() {
    private val client by lazy { Client("192.168.0.193", 7001) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val resultTextView = findViewById<TextView>(R.id.result_text_view)

        val number = "11111111111"


        // Отправка запроса на сервер и обработка ответа
        Thread {
            client.connect(number)


            // Чтение ответа
            val response = client.getResultJson()

            // Обновление текста в TextView
            runOnUiThread {
                resultTextView.text = response
            }
        }.start()
    }
}








