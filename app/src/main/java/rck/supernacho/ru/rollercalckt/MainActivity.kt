package rck.supernacho.ru.rollercalckt

import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import rck.supernacho.ru.rollercalckt.controller.MainController
import rck.supernacho.ru.rollercalckt.fragments.EditMaterialFragment
import rck.supernacho.ru.rollercalckt.fragments.CalcFragment
import rck.supernacho.ru.rollercalckt.fragments.FragmentsTags
import rck.supernacho.ru.rollercalckt.model.MaterialMapper

class MainActivity : AppCompatActivity(), CalcFragment.OnFragmentInteractionListener,
        EditMaterialFragment.OnFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                Log.d("++","BackStack: ${supportFragmentManager.backStackEntryCount}")
                if (supportFragmentManager.backStackEntryCount > 0) {
                    val tag = supportFragmentManager.getBackStackEntryAt(0).name
                    when(tag) {
                        FragmentsTags.EDIT_MATERIALS.tag -> {
                            Log.d("++", "BackStack: ${tag}")
                            val fragment = supportFragmentManager.findFragmentByTag(FragmentsTags.EDIT_MATERIALS.tag)

                            supportFragmentManager.beginTransaction()
                                    .remove(fragment)
                                    .commit()
                            supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                        FragmentsTags.ABOUT.tag -> {
                            val fragment = supportFragmentManager.findFragmentByTag(FragmentsTags.ABOUT.tag)
                            supportFragmentManager.beginTransaction()
                                    .remove(fragment)
                                    .commit()
                            supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                        FragmentsTags.SETTINGS.tag -> {
                            val fragment = supportFragmentManager.findFragmentByTag(FragmentsTags.SETTINGS.tag)
                            supportFragmentManager.beginTransaction()
                                    .remove(fragment)
                                    .commit()
                            supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                        else -> {
                            Toast.makeText(this, "No such fragment", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_help -> {
//                message.setText(R.string.title_help)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
//                message.setText(R.string.title_settings)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val matMapper = MaterialMapper(this)
        MainController.setMaterialMapper(matMapper)
        MainController.onCreate()
        init()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        navigation.selectedItemId = R.id.navigation_home
    }

    private fun init(){
        val calcFragment = CalcFragment.newInstance("tt","tt")
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, calcFragment, FragmentsTags.CALCULATE.tag)
                .commit()
    }

    override fun onFragmentInteraction(command: String) {
        when(command){
            "add_fragment" -> {
                Toast.makeText(this,"Button Add", Toast.LENGTH_SHORT).show()
                val editMaterialFragment = EditMaterialFragment.newInstance("tt","tt")
                supportFragmentManager.beginTransaction()
                        .addToBackStack(FragmentsTags.EDIT_MATERIALS.tag)
                        .add(R.id.fragment_container, editMaterialFragment, FragmentsTags.EDIT_MATERIALS.tag)
                        .commit()
            }
            else -> {
                Toast.makeText(this,"Passed command: " + command , Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        MainController.onDestroy()
    }
}
