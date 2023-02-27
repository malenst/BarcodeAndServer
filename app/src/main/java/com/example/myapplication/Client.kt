package com.example.myapplication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.net.Socket
import java.nio.charset.StandardCharsets
import kotlin.system.exitProcess

class Client(private val ip: String, private val port: Int) {
    private val cli = Socket(ip, port)
    private var resultJson: String = "";
    fun getResultJson(): String {
        return resultJson;
    }



    fun connect(number: String) {
        try {
            val msg = cli.getInputStream().bufferedReader(StandardCharsets.UTF_8).readLine()
            while(msg != "You are connected"){}
            if (msg == "You are connected") {
                listen(number)
            } else {
                exitProcess(0)
            }
        } catch (e: Exception) {
            println("Error:${e.message}")
            exitProcess(0)
        }
    }

    fun sender(text: String) {
        cli.getOutputStream().write(text.toByteArray(StandardCharsets.UTF_8))
        while (cli.getInputStream().bufferedReader(StandardCharsets.UTF_8).readLine() != "getted") {
            cli.getOutputStream().write(text.toByteArray(StandardCharsets.UTF_8))
        }
    }

    fun listen(number: String) {
        var isWork = true
        while (isWork) {
            print("напиши сообщение")
            val req = number

            if (req == "disconnect") {
                sender(req)
                println(cli.getInputStream().bufferedReader(StandardCharsets.UTF_8).readLine())
            } else {
                println("прием пакетов")
                sender(req ?: "")
                println("прием пакетов2")
                val data = JSONObject(
                    cli.getInputStream().bufferedReader(StandardCharsets.UTF_8).readLine()
                )
                println("прием пакетов3")
                resultJson = data.toString()
                println(data)
            }
        }
    }
}