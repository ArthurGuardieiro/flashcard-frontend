package com.example.flashcardapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.module
import io.ktor.server.application.ApplicationEngine
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private var server: ApplicationEngine? = null
    private val serverPort = 8081
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val statusTextView = findViewById<TextView>(R.id.serverStatusTextView)
        
        // Iniciar o servidor Ktor em uma coroutine
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                startKtorServer()
                updateStatus(statusTextView, "Servidor rodando na porta $serverPort")
            } catch (e: Exception) {
                updateStatus(statusTextView, "Erro ao iniciar o servidor: ${e.message}")
            }
        }
    }
    
    private fun startKtorServer() {
        server = embeddedServer(Netty, port = serverPort, watchPaths = emptyList()) {
            module() // Módulo Ktor definido em Application.kt
        }.start(wait = false)
    }
    
    private fun updateStatus(textView: TextView, message: String) {
        runOnUiThread {
            textView.text = message
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        server?.stop(1000, 2000)
    }
} 