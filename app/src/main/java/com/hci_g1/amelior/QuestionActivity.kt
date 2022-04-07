package com.hci_g1.amelior

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
private const val TAG = "questionActivity"
class QuestionActivity : AppCompatActivity() {
    private lateinit var nameTextField: EditText
    private lateinit var ageTextField: EditText
    private lateinit var seekBar: SeekBar
    private lateinit var nameLabel: TextView
    private lateinit var feelingLabel: TextView
    private lateinit var ageLabel: TextView
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_activity)

        //action bar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Questionary"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        nameTextField = findViewById(R.id.nameTextField)
        ageTextField = findViewById(R.id.ageTextField)
        seekBar = findViewById(R.id.seekBar)
        nameLabel = findViewById(R.id.nameLabel)
        feelingLabel = findViewById(R.id.feelingLabel)
        ageLabel = findViewById(R.id.ageLabel)
        button = findViewById(R.id.mainMenuBtn)

        button.setOnClickListener{
            val intent = Intent(this, MainMenuActivity:: class.java)
            startActivity(intent)
        }
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                Log.i(TAG, "onProgressChanged $p1")
                feelingLabel.text = "$p1"
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        nameTextField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
            }
        })

        ageTextField.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                Log.i(TAG, "afterTextChanged $p0")
            }

        })
    }
    //handle onBack pressed(go to previous page)
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}