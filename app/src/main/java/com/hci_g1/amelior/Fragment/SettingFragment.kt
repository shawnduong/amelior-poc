package com.hci_g1.amelior

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.hci_g1.amelior.entities.Mood
import com.hci_g1.amelior.entities.User
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment: Fragment() {

    private val displayMetrics = DisplayMetrics()
    private var screenHeight: Float = 0f

    private lateinit var userDao: UserDao
    private lateinit var moodDao: MoodDao

    private lateinit var buttonSplashSubmit: Button
    private lateinit var imageViewMoodGraphic: ImageView
    private lateinit var linearLayoutMoodForm: LinearLayout
    private lateinit var seekBarMoodBar: SeekBar
    private lateinit var textViewMoodDescription: TextView

    private lateinit var editTextNameInputField: TextView
    private lateinit var textViewName: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        val context = getContext()

        /* Initializing variables. */
        if (context != null)
        {
            userDao = UserDatabase.getInstance(context).userDao
            // moodDao = UserDatabase.getInstance(context).moodDao
            Log.d(SettingFragment.TAG, "Database successfully loaded.")
        }

        /* Initializing widgets. */
        textViewName                    = view.findViewById(R.id.textViewName)
        editTextNameInputField          = view.findViewById(R.id.editTextNameInputField)
        buttonSplashSubmit              = view.findViewById(R.id.splashSubmit)

        val user = userDao.get_user_now("user")
        textViewName.text = "${user.name}"

        buttonSplashSubmit.setOnClickListener {
            val name: String = editTextNameInputField.text.toString()
            userDao.insert_user_now(User("user", name))
            textViewName.text = "${name}"
            Log.d(TAG, "Change Name")
        }

    }

    companion object
    {
        private val TAG = "SettingFragment"
    }
}

/*
class SettingFragment : Fragment(R.layout.fragment_setting) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val settingView = inflater.inflate(R.layout.fragment_setting, container, false)
        return settingView
    }

}
 */

