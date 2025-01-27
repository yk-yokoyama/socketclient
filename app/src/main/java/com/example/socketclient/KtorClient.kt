package com.example.socketclient

import android.util.Log
import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.Socket
import io.ktor.network.sockets.aSocket
import io.ktor.network.sockets.openReadChannel
import io.ktor.network.sockets.openWriteChannel
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.ByteWriteChannel
import io.ktor.utils.io.readUTF8Line
import io.ktor.utils.io.writeStringUtf8
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * TCPソケットクライアント
 */
class KtorClient {

    private var selectorManager = SelectorManager(Dispatchers.IO)
    private var socket: Socket? = null
    private var receiveChannel: ByteReadChannel? = null
    private var sendChannel: ByteWriteChannel? = null

    /**
     * サーバーに接続する
     */
    fun open(host : String = "192.168.151.56", port : Int = 8080) {
        CoroutineScope(Dispatchers.IO).launch {
            // 既に接続されている場合は起動しない
            if (socket != null) return@launch

            socket = aSocket(selectorManager).tcp().connect(host, port)

            receiveChannel = socket!!.openReadChannel()
            sendChannel = socket!!.openWriteChannel(autoFlush = true)

            CoroutineScope(Dispatchers.IO).launch {
                while (true) {
                    // サーバーから受信したメッセージ
                    val receivedMessage = receiveChannel!!.readUTF8Line()
                    if (receivedMessage != null) {
                        Log.d("KtorClient", "### Server said: $receivedMessage")
                    } else {
                        Log.d("KtorClient","### Server closed a connection")
//                        socket!!.close()
//                        selectorManager.close()
                        break
                    }
                }
            }
        }
    }

    /**
     * サーバーへメッセージを送信する
     */
    fun sendMessage() {
        CoroutineScope(Dispatchers.IO).launch {
            val myMessage = "テストテスト民族"
            sendChannel!!.writeStringUtf8("$myMessage\n")
        }
    }

    /**
     * サーバーから切断する
     */
    fun close() {
        // 接続されていない場合は、何もしない
        if (socket == null) return
        CoroutineScope(Dispatchers.IO).launch {
            socket!!.close()
            selectorManager.close()
            socket = null
            receiveChannel = null
            sendChannel = null
            Log.d("KtorClient", "connection closed")
        }
    }
}