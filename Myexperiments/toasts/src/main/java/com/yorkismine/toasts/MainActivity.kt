package com.yorkismine.toasts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.custom_toast.*
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewGroup = findViewById<ViewGroup>(R.id.toast_container)
        val view = layoutInflater.inflate(R.layout.custom_toast, viewGroup)

        val textView = view.findViewById<TextView>(R.id.toast_text)
        textView.text = "Hello there!"

        val toast = Toast(this)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.setMargin(-1.0f, -1.0f)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.show()

    }
}
