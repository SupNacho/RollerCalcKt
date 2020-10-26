package rck.supernacho.ru.rollercalckt

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.activity_nav.*
import rck.supernacho.ru.rollercalckt.screens.custom.setVisibility
import rck.supernacho.ru.rollercalckt.screens.setKeyboardShownListener

class NavActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nav)
        setupWithNavController(bnv_Navigation, navHostFragment.findNavController())
        navHostFragment.findNavController().addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.manageMaterial -> bnv_Navigation.visibility = View.GONE
                else -> bnv_Navigation.visibility = View.VISIBLE

            }
        }
        nav_container.setKeyboardShownListener { bnv_Navigation?.setVisibility(isVisible = !it) }
    }
}
