package com.senzaproject.ios27

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var root: LinearLayout
    private lateinit var grid: GridLayout
    private lateinit var search: EditText
    private var apps: List<LaunchApp> = emptyList()

    private data class LaunchApp(val label: String, val packageName: String, val icon: android.graphics.drawable.Drawable)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.rgb(16, 20, 47)
        window.navigationBarColor = Color.rgb(16, 20, 47)
        buildUi()
        loadApps()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    private fun rounded(color: Int, radius: Int): GradientDrawable =
        GradientDrawable().apply { setColor(color); cornerRadius = dp(radius).toFloat() }

    private fun text(value: String, size: Float, color: Int, bold: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
        }

    private fun buildUi() {
        val background = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(Color.rgb(26, 35, 82), Color.rgb(64, 72, 145), Color.rgb(17, 22, 51))
        )
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(20), dp(22), dp(20), dp(12))
            this.background = background
        }

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scroll.addView(content)

        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }
        top.addView(text("SenzaProject", 14f, Color.rgb(220, 227, 255), true),
            LinearLayout.LayoutParams(0, dp(34), 1f))
        top.addView(text("●  BETA", 11f, Color.rgb(190, 211, 255), true))
        content.addView(top)

        content.addView(text("Hello, welcome.", 16f, Color.rgb(215, 223, 255)).apply {
            setPadding(0, dp(28), 0, dp(4))
        })
        content.addView(text(SimpleDateFormat("EEEE, d MMMM", Locale.getDefault()).format(Date()), 15f, Color.rgb(220, 225, 255)))
        content.addView(text(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()), 54f, Color.WHITE, true).apply {
            setPadding(0, dp(2), 0, dp(14))
        })

        search = EditText(this).apply {
            hint = "⌕   Search apps"
            textSize = 15f
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.rgb(218, 224, 255))
            setPadding(dp(16), 0, dp(16), 0)
            background = rounded(0x35FFFFFF, 28)
            imeOptions = EditorInfo.IME_ACTION_SEARCH
        }
        content.addView(search, LinearLayout.LayoutParams(-1, dp(48)).apply { bottomMargin = dp(22) })

        content.addView(text("Your apps", 20f, Color.WHITE, true).apply {
            setPadding(dp(2), 0, 0, dp(12))
        })
        grid = GridLayout(this).apply { columnCount = 4; alignmentMode = GridLayout.ALIGN_BOUNDS }
        content.addView(grid, LinearLayout.LayoutParams(-1, -2))
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))

        val footer = text("iOS 27  ·  A fresh way to navigate", 11f, Color.rgb(205, 213, 250)).apply {
            gravity = Gravity.CENTER
            setPadding(0, dp(10), 0, dp(4))
        }
        root.addView(footer, LinearLayout.LayoutParams(-1, -2))
        setContentView(root)

        search.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { renderApps(s?.toString().orEmpty()) }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })
    }

    private fun loadApps() {
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        apps = packageManager.queryIntentActivities(intent, 0)
            .mapNotNull { info ->
                try {
                    LaunchApp(
                        info.loadLabel(packageManager).toString(),
                        info.activityInfo.packageName,
                        info.loadIcon(packageManager)
                    )
                } catch (_: Exception) { null }
            }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase(Locale.getDefault()) }
        renderApps("")
    }

    private fun renderApps(query: String) {
        if (!::grid.isInitialized) return
        grid.removeAllViews()
        val filtered = apps.filter { it.label.contains(query.trim(), ignoreCase = true) }
        val width = (resources.displayMetrics.widthPixels - dp(40)) / 4
        filtered.forEach { app ->
            val item = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(dp(2), dp(5), dp(2), dp(14))
                isClickable = true
                isFocusable = true
                setOnClickListener { launchApp(app.packageName) }
            }
            val icon = ImageView(this).apply {
                setImageDrawable(app.icon)
                scaleType = ImageView.ScaleType.FIT_CENTER
                background = rounded(0x20FFFFFF, 18)
                setPadding(dp(9), dp(9), dp(9), dp(9))
            }
            item.addView(icon, LinearLayout.LayoutParams(dp(54), dp(54)))
            item.addView(text(app.label, 10.5f, Color.WHITE).apply {
                gravity = Gravity.CENTER
                maxLines = 2
                ellipsize = android.text.TextUtils.TruncateAt.END
                setPadding(0, dp(6), 0, 0)
            }, LinearLayout.LayoutParams(-1, dp(32)))
            val params = GridLayout.LayoutParams().apply {
                width = width
                height = dp(100)
            }
            grid.addView(item, params)
        }
        if (filtered.isEmpty()) {
            val empty = text("No apps found", 14f, Color.WHITE).apply { setPadding(dp(8), dp(16), 0, dp(16)) }
            grid.addView(empty, GridLayout.LayoutParams().apply { width = -1; height = -2; columnSpec = GridLayout.spec(0, 4) })
        }
    }

    private fun launchApp(packageName: String) {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return
        try { startActivity(launchIntent) } catch (_: Exception) {}
    }
}
