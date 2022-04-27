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
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*

class SettingFragment: Fragment() {

    private val displayMetrics = DisplayMetrics()
    private var screenHeight: Float = 0f

    private lateinit var userDao: UserDao
    private lateinit var moodDao: MoodDao

    private lateinit var buttonSplashSubmit: Button
    private lateinit var buttonSplashShowInfo: Button

	/* Debugging buttons. */
	private lateinit var buttonSampleA: Button
	private lateinit var buttonSampleB: Button
	private lateinit var buttonSampleC: Button
	private lateinit var buttonSampleD: Button

	/* Debugging other DAOs. */
//	private lateinit var userDao: UserDao
//	private lateinit var moodDao: MoodDao

    private lateinit var textViewInfo: TextView
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

        /* Get the screen metrics. */
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels.toFloat()

        /* Initializing widgets. */
        textViewName                    = view.findViewById(R.id.textViewName)
        editTextNameInputField          = view.findViewById(R.id.editTextNameInputField)
        buttonSplashSubmit              = view.findViewById(R.id.splashSubmit)
        buttonSplashShowInfo            = view.findViewById(R.id.splashInfo)
        textViewInfo                    = view.findViewById(R.id.textViewInfo)

		/* Debugging widgets. */
        buttonSampleA = view.findViewById(R.id.sampleA)
        buttonSampleB = view.findViewById(R.id.sampleB)
        buttonSampleC = view.findViewById(R.id.sampleC)
        buttonSampleD = view.findViewById(R.id.sampleD)

        val user = userDao.get_user_now("user")
        textViewName.text = "${user.name}"

        buttonSplashSubmit.setOnClickListener {
            val name: String = editTextNameInputField.text.toString()
            userDao.insert_user_now(User("user", name))
            textViewName.text = "${name}"
            Log.d(TAG, "Change Name")
        }


        buttonSplashShowInfo.setOnClickListener {
            if (textViewInfo.visibility == View.VISIBLE)
            {
                textViewInfo.visibility = View.INVISIBLE
            }
            else
            {
                textViewInfo.visibility = View.VISIBLE
                Log.d(TAG, "Show Info")
            }
        }

		/* Debugging buttons. */
		buttonSampleA.setOnClickListener {

			val context = getContext()

			/* Initializing variables. */
			if (context != null)
			{
				userDao = UserDatabase.getInstance(context).userDao
				moodDao = UserDatabase.getInstance(context).moodDao
			}

			Log.d(TAG, "aidjwoiajdoiwajda")

			moodDao.insert_mood_now(Mood(0, 1651001400000, 100))
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

