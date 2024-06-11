package com.novbicreate.bdui_test_client

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.InputStream
import java.net.URL

fun parseLayout(context: Context, layoutJson: String): View {

    val layout = RelativeLayout(context)
    val json = JSONObject(layoutJson)
    val children = json.getJSONArray("children")


    for (i in 0 until children.length()) {
        val childJson = children.getJSONObject(i)
        val viewType = childJson.getString("type")

        when (viewType) {
            "div" -> {
                val div = LinearLayout(context)
                val backgroundColor = Color.parseColor(childJson.getString("backgroundColor"))
                try {
                    val cornerRadius = childJson.getInt("border-radius").toFloat()
                    val gradientDrawable = GradientDrawable()
                    gradientDrawable.setColor(backgroundColor)
                    gradientDrawable.cornerRadius = cornerRadius
                    div.background = gradientDrawable
                } catch (e: Exception) {
                    div.setBackgroundColor(backgroundColor)
                }

                div.layoutParams = RelativeLayout.LayoutParams(
                    childJson.getInt("width"),
                    childJson.getInt("height")
                )

                if (childJson.has("children")) {
                    val childrenArray = childJson.getJSONArray("children")
                    for (j in 0 until childrenArray.length()) {
                        val child = childrenArray.getJSONObject(j)
                        when (child.getString("type")) {
                            "text" -> {
                                val textView = TextView(context)
                                textView.text = child.getString("text")
                                textView.textSize = child.getInt("fontSize").toFloat()
                                textView.typeface = Typeface.DEFAULT_BOLD
                                textView.setTextColor(Color.parseColor(child.getString("textColor")))
                                div.addView(textView)
                            }
                            "div" -> {
                                val childDiv = RelativeLayout(context)
                            }
                            "image" -> {
                                val image = ImageView(context)
                                GlobalScope.launch(Dispatchers.IO) {
                                    val inputStream = URL(child.getString("src")).openStream()
                                    val drawable = Drawable.createFromStream(inputStream, null)
                                    val borderRadius = child.getInt("border-radius")

                                    image.setImageDrawable(drawable)
                                }
                                image.layoutParams = RelativeLayout.LayoutParams(
                                    child.getInt("width"),
                                    child.getInt("height")
                                )
                            }
                        }
                    }
                }
                layout.addView(div)
            }

            "text" -> {
                val textView = TextView(context)
                textView.text = childJson.getString("text")
                textView.textSize = childJson.getInt("fontSize").toFloat()
                textView.typeface = Typeface.DEFAULT_BOLD
                textView.setTextColor(Color.parseColor(childJson.getString("textColor")))
                textView.x = childJson.getInt("x").toFloat()
                textView.y = childJson.getInt("y").toFloat()
                layout.addView(textView)
            }
        }
    }
    return layout
}