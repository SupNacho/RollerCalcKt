package rck.supernacho.ru.rollercalckt

import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import kotlinx.android.synthetic.main.activity_main.*
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.controller.PrefsController
import rck.supernacho.ru.rollercalckt.fragments.*
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

class MainActivity : AppCompatActivity(), CalcFragment.OnFragmentInteractionListener,
        EditMaterialFragment.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener,
        AboutFragment.OnFragmentInteractionListener {
    lateinit var prefsController: PrefsController
    private lateinit var refWatcher: RefWatcher


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
                            .replace(R.id.fragment_container, AboutFragment.newInstance(),
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
        refWatcher = LeakCanary.install(application)
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

    fun getRWatcher(): RefWatcher {
        return refWatcher
    }

    override fun onDestroy() {
        super.onDestroy()
        MainController.onDestroy()
        refWatcher.watch(this)
    }
}
