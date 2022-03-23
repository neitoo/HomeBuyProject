package ru.homebuy.neito

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addAnAd.setOnClickListener(){
            val intentAd = Intent(this, PlusAd::class.java)
            startActivity(intentAd)
        }

        settingButton.setOnClickListener(){
            val intentSet = Intent(this, setting_homes::class.java)
            startActivity(intentSet)
        }

    }

}