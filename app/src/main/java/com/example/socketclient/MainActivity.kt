package com.example.socketclient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val startButton by lazy { findViewById<Button>(R.id.btn_start) }
    private val stopButton by lazy { findViewById<Button>(R.id.btn_stop) }
    private val messageEditText by lazy { findViewById<EditText>(R.id.edit_message) }
    private val sendButton by lazy { findViewById<Button>(R.id.btn_send) }

    private var ktorClient = KtorClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        startButton.setOnClickListener {
            // サーバーに接続
            ktorClient.open()
        }
        stopButton.setOnClickListener {
            // サーバーから切断
            ktorClient.close()
        }
        sendButton.setOnClickListener {
            // サーバーへメッセージを送信
            ktorClient.sendMessage()
        }
    }
}