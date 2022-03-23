package ru.homebuy.neito

import android.R.attr.button
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_setting_homes.*


class setting_homes : AppCompatActivity() {

    private var houseCh: Button ?= null
    private var houseKw: Button ?= null
    private var roomOne: Button ?= null
    private var roomTwo: Button ?= null
    private var roomFree: Button ?= null
    private var roomFour: Button ?= null
    private var roomFivePlus: Button ?= null
    private var dyapazoneAfter: EditText ?= null
    private var dyapazoneBefore: EditText ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_homes)

        init()
    }

    private fun init(){
        houseCh = findViewById(R.id.buttonHome)
        houseKw = findViewById(R.id.buttonHome2)
        roomOne = findViewById(R.id.room1)
        roomTwo = findViewById(R.id.room2)
        roomFree = findViewById(R.id.room3)
        roomFour = findViewById(R.id.room4)
        roomFivePlus = findViewById(R.id.room5)
        dyapazoneAfter = findViewById(R.id.costAfter)
        dyapazoneBefore = findViewById(R.id.costBefore)
    }
}