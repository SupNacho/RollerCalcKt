package rck.supernacho.ru.rollercalckt

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import rck.supernacho.ru.rollercalckt.fragments.AddMaterialFragment
import rck.supernacho.ru.rollercalckt.fragments.CalcFragment

class MainActivity : AppCompatActivity(), CalcFragment.OnFragmentInteractionListener,
        AddMaterialFragment.OnFragmentInteractionListener {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
//                message.setText(R.string.title_home)
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
        init()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun init(){
        val calcFragment = CalcFragment.newInstance("tt","tt")
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, calcFragment)
                .commit()
    }

    override fun onFragmentInteraction(command: String) {
        when(command){
            "add_fragment" -> {
                Toast.makeText(this,"Button Add", Toast.LENGTH_SHORT).show()
                val addMaterialFragment = AddMaterialFragment.newInstance("tt","tt")
                supportFragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .add(R.id.fragment_container, addMaterialFragment)
                        .commit()
            }
            else -> {
                Toast.makeText(this,"Passed command: " + command , Toast.LENGTH_SHORT).show()
            }
        }
    }
}
