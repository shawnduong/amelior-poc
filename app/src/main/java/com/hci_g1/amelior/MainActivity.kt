package com.hci_g1.amelior

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.hci_g1.amelior.R.layout


class MainActivity : AppCompatActivity() {
	private lateinit var button: Button
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(layout.main_activity)
		button = findViewById(R.id.questionBtn)
		button.setOnClickListener {
			val intent = Intent(this, QuestionActivity::class.java)
			startActivity(intent)

		}

	}
}

