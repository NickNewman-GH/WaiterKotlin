package com.example.waiter

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    val receiver = Receiver()
    var is_stop = false

    companion object{
        var minCount = 0;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver(receiver, IntentFilter("android.intent.action.BATTERY_OKAY"))
        registerReceiver(receiver, IntentFilter("android.intent.action.BATTERY_LOW"))
        registerReceiver(receiver, IntentFilter("android.intent.action.TIME_TICK"))

        val textView: TextView = findViewById(R.id.text)
        textView.text = "Ждуние..."

        findViewById<Button>(R.id.button).setOnClickListener{
            if (!is_stop) {
                val textView: TextView = findViewById(R.id.text)
                unregisterReceiver(receiver)
                textView.text = "Ждун ушёл спать. Ждун больше не придёт."
                minCount = 0
                is_stop = true
            }
        }
    }

    class Receiver :BroadcastReceiver(){
        var is_hungry = false;
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "android.intent.action.BATTERY_LOW") {
                val textView = (context as Activity).findViewById<TextView>(R.id.text)
                textView.text = "Ждун не хочет ждать, он хочет ЖРАТЬ!"
                is_hungry = true
            } else if (intent.action == "android.intent.action.BATTERY_OKAY"){
                Toast.makeText(context, "То-то же! Ещё не хватало Вам быть сожраным Ждуном.", Toast.LENGTH_LONG).show()
                val textView = (context as Activity).findViewById<TextView>(R.id.text)
                textView.text = "Время ждуния: $minCount мин."
                is_hungry = false
            } else if (intent.action == "android.intent.action.TIME_TICK" && !is_hungry) {
                Toast.makeText(context, "Time ticked", Toast.LENGTH_LONG).show()
                val textView = (context as Activity).findViewById<TextView>(R.id.text)
                minCount += 1
                textView.text = "Время ждуния: $minCount мин."
            }
        }
    }
}