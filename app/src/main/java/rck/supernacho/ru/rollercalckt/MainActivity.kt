package rck.supernacho.ru.rollercalckt

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.controller.PrefsController
import rck.supernacho.ru.rollercalckt.fragments.*
import rck.supernacho.ru.rollercalckt.model.MaterialMapper
import rck.supernacho.ru.rollercalckt.screens.about.view.AboutFragment

class MainActivity : AppCompatActivity(), CalcFragment.OnFragmentInteractionListener,
        EditMaterialFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,
         KodeinAware {
    lateinit var prefsController: PrefsController

    override val kodein: Kodein by closestKodein()

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                removeFragments()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                if (navigation.selectedItemId != item.itemId) {
                    removeFragments()
                    supportFragmentManager.beginTransaction()
                            .addToBackStack(FragmentsTags.ABOUT.tag)
                            .replace(R.id.fragment_container, AboutFragment(),
                                    FragmentsTags.ABOUT.tag)
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                if (navigation.selectedItemId != item.itemId) {
                    removeFragments()
                    supportFragmentManager.beginTransaction()
                            .addToBackStack(FragmentsTags.SETTINGS.tag)
                            .replace(R.id.fragment_container, SettingsFragment.newInstance(),
                                    FragmentsTags.SETTINGS.tag)
                            .commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun removeFragments() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            val tag = supportFragmentManager.getBackStackEntryAt(0).name
            if (tag == FragmentsTags.EDIT_MATERIALS.tag || tag == FragmentsTags.SETTINGS.tag ||
                    tag == FragmentsTags.ABOUT.tag) {
                val fragment = supportFragmentManager.findFragmentByTag(tag)
                if (fragment != null)
                    supportFragmentManager.beginTransaction()
                            .remove(fragment)
                            .commit()
                supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            } else {
                Toast.makeText(this, "No such fragment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val matMapper = MaterialMapper(this)
        prefsController = PrefsController(this)
        MainController.setMaterialMapper(matMapper)
        MainController.onCreate()
        init()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    private fun init() {
        val calcFragment = CalcFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, calcFragment, FragmentsTags.CALCULATE.tag)
                .commit()
    }

    override fun onFragmentInteraction(command: String) {
        when (command) {
            "add_fragment" -> {
                val editMaterialFragment = EditMaterialFragment.newInstance()
                supportFragmentManager.beginTransaction()
                        .addToBackStack(FragmentsTags.EDIT_MATERIALS.tag)
                        .replace(R.id.fragment_container, editMaterialFragment, FragmentsTags.EDIT_MATERIALS.tag)
                        .commit()
            }
            else -> {
                Toast.makeText(this, "Passed command: $command", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MainController.onDestroy()
    }
}
