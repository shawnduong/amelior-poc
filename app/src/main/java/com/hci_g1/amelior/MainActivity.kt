package com.hci_g1.amelior

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hci_g1.amelior.entities.relations.UserHealthCrossRef
import com.hci_g1.amelior.entities.User
import com.hci_g1.amelior.entities.Health
import kotlinx.coroutines.launch

class MainActivity: AppCompatActivity()
{
	override fun onCreate (savedInstanceState: Bundle?)
	{
		super.onCreate(savedInstanceState)
		setContentView(R.layout.main_activity)
		val dao = UserDatabase.getInstance(this).userDao

		val healths = listOf (
			Health(1, 300, 400, 120.27),
			Health(2, 400, 200, 120.73)
		)

		val users = listOf (
			User("Testing1", 18,"F", 120.7, 160.3),
			User("Testing2", 20,"F", 115.0, 170.5)
		)

		val userHealthRelations = listOf (
			UserHealthCrossRef("Testing1", 1),
			UserHealthCrossRef("Testing2", 2)
		)

		lifecycleScope.launch {
			healths.forEach { dao.insertHealth(it) }
			users.forEach { dao.insertUser(it) }
			userHealthRelations.forEach { dao.insertUserHealthCrossRef(it) }
		}

	}
}
