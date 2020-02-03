package com.example.td_android_studio

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment

class LifeCycleActivity : AppCompatActivity(), OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_life_cycle)
        val frag1 = FirstFragment()
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, frag1).commit()
    }

    override fun swipeFragment(frag_destination: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, frag_destination).commit()
    }
}

