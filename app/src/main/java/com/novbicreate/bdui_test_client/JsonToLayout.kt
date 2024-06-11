package com.novbicreate.bdui_test_client

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.Space
import androidx.coordinatorlayout.widget.CoordinatorLayout
import org.json.JSONArray
import org.json.JSONObject

fun jsonToLayout(context: Context, jsonString: String): View {
    val layout = CoordinatorLayout(context)
    val json = JSONObject(jsonString)
    val component = json.getJSONObject("component")
    parseComponent(context, layout, component)
    return layout
}

fun parseComponent(context: Context, parent: ViewGroup, jsonObject: JSONObject) {
    val viewType = jsonObject.getString("type")
    val modification = jsonObject.getJSONObject("modification")
    when (viewType) {
        "div" -> {
            val div = LinearLayout(context).apply { applyOrientation(modification) }

            applyBackground(div, modification)
            applyLayoutParams(div, parent, modification)

            parent.addView(div)

            // Обработка дочерних элементов, если они есть
            if (jsonObject.has("children")) {
                val children: JSONArray = jsonObject.getJSONArray("children")
                parseChildrenComponent(context, div, children)
            }
        }
        "text" -> {
            // ... develop ...
        }
        "space" -> {
            val space = Space(context)
            applyLayoutParams(space, parent, modification)
            parent.addView(space)
        }
        "button" -> {
            // ... develop ...
        }
    }
}
fun parseChildrenComponent(context: Context, parent: ViewGroup, children: JSONArray) {
    for (i in 0 until children.length()) {
        val childJson = children.getJSONObject(i)
        parseComponent(context, parent, childJson)
    }
}

fun applyBackground(view: View, modification: JSONObject) {
    val hasBackground = modification.isNull("background").not()
    if (hasBackground) {
        val bgColor = Color.parseColor(modification.getString("background"))
        try {
            val hasBorderRadius = modification.isNull("border-radius").not()
            var borderRadius = 0f
            if (hasBorderRadius) {
                val borderRadiusString = modification.getString("border-radius")
                borderRadius = borderRadiusString.replace("px", "").toInt().dp().toFloat()
            }
            val gradientDrawable = GradientDrawable().apply {
                setColor(bgColor)
                cornerRadius = borderRadius
            }
            view.background = gradientDrawable
        } catch (e: Exception) {
            view.setBackgroundColor(bgColor)
        }
    }
}

fun LinearLayout.applyOrientation(modification: JSONObject) {
    val hasDisplay = modification.isNull("display").not()
    if (hasDisplay) {
        val display = modification.getString("display")
        val orientation = when (display) {
            "table", "flex", "table-cell" -> LinearLayout.HORIZONTAL
            else -> LinearLayout.VERTICAL
        }
        this.orientation = orientation
    }
}

fun applyLayoutParams(view: View, parent: ViewGroup, modification: JSONObject) {
    val hasWidth = modification.isNull("width").not()
    val hasHeight = modification.isNull("height").not()
    var width = WRAP_CONTENT
    var height = WRAP_CONTENT

    if (hasWidth) {
        width = when (val parseWidth = modification.getString("width")) {
            "100%" -> MATCH_PARENT
            "50%" -> parent.width / 2
            "auto" -> WRAP_CONTENT
            else -> parseWidth.toInt().dp()
        }
    }
    if (hasHeight) {
        height = when (val parseHeight = modification.getString("height")) {
            "100%" -> MATCH_PARENT
            "50%" -> parent.width / 2
            "auto" -> WRAP_CONTENT
            else -> parseHeight.toInt().dp()
        }
    }

    view.layoutParams = ViewGroup.LayoutParams(width, height)
}
fun Int.dp(): Int = this * 2