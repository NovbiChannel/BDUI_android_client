package com.novbicreate.bdui_test_client

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject

fun parseLayout(context: Context, layoutJson: String): View {

    val layout = RelativeLayout(context)

    val json = JSONObject(layoutJson)

    val children = json.getJSONArray("children")


    for (i in 0 until children.length()) {

        val childJson = children.getJSONObject(i)

        val viewType = childJson.getString("type")


        when (viewType) {

            "rect" -> {

                val rect = RelativeLayout(context)

                val backgroundColor = Color.parseColor(childJson.getString("backgroundColor"))

                try {
                    val cornerRadius = childJson.getInt("cornerRadius").toFloat()

                    val gradientDrawable = GradientDrawable()
                    gradientDrawable.setColor(backgroundColor)
                    gradientDrawable.cornerRadius = cornerRadius

                    rect.background = gradientDrawable
                } catch (e: Exception) {
                    rect.setBackgroundColor(backgroundColor)
                }

                rect.layoutParams = RelativeLayout.LayoutParams(

                    childJson.getInt("width"),

                    childJson.getInt("height")

                )

                rect.x = childJson.getInt("x").toFloat()

                rect.y = childJson.getInt("y").toFloat()


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

                                textView.x = child.getInt("x").toFloat()

                                textView.y = child.getInt("y").toFloat()

                                rect.addView(textView)

                            }

                            "checkbox" -> {

                                val checkBox = CheckBox(context)

                                checkBox.x = child.getInt("x").toFloat()

                                checkBox.y = child.getInt("y").toFloat()

                                rect.addView(checkBox)

                            }

                        }

                    }

                }


                layout.addView(rect)

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