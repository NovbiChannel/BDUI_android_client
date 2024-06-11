package com.novbicreate.bdui_test_client

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        GlobalScope.launch(Dispatchers.IO) {
            val layoutJson = URL("http://192.168.0.138:8081/screen").readText()
            withContext(Dispatchers.Main) {
                val layout = parseLayout(this@MainActivity, layoutJson)
                setContentView(layout)
            }
        }
    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        GlobalScope.launch(Dispatchers.IO) {
//            val json = URL("http://192.168.0.138:8081/layout").readText()
//            val layout = Json.decodeFromString(Layout.serializer(), json)
//            Log.e(this::class.simpleName, json)
//            withContext(Dispatchers.Main) {
//                when (layout.type) {
//                    "button" -> {
//                        val button = Button(this@MainActivity).apply {
//                            text = layout.properties["text"]
//                            setBackgroundColor(Color.parseColor(layout.properties["color"]))
//                            layoutParams = LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of the button
//                                LinearLayout.LayoutParams.WRAP_CONTENT  // Height of the button
//                            )
//                        }
//                        button.setOnClickListener {
//                            when (layout.action["type"]) {
//                                "showToast" -> {
//                                    val message = layout.action["data"]
//                                    message?.let { showToast(it) }
//                                }
//                                // Handle other types of actions...
//                            }
//                        }
//                        val linearLayout = LinearLayout(this@MainActivity).apply {
//                            gravity = Gravity.CENTER  // Center the button in the layout
//                            addView(button)
//                        }
//                        setContentView(linearLayout)
//                    }
//                    // Handle other types of layout elements...
//                }
//            }
//        }
//    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
@Serializable
data class Layout(val type: String, val properties: Map<String, String>, val action: Map<String, String>)