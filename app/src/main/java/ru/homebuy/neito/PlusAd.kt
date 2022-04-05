@file:Suppress("DEPRECATION")

package ru.homebuy.neito

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_plus_ad.*
import java.util.*


class PlusAd : AppCompatActivity() {

    private var saveCurrentDate: String ?= null
    private var saveCurrentTime: String ?= null
    private var RandomKey: String ?= null
    private var HouseImageRef: StorageReference ?= null
    private var houseImage: ImageView ?= null
    private var ImageUri: Uri ?=null
    private var downloadImageUrl: String ?= null
    private var costView: EditText ?=null
    private var HouseReference: DatabaseReference ?= null
    private var locationView: EditText ?=null
    private var roomsView: EditText ?=null
    private var squareView: EditText ?=null
    private var infoView: EditText ?=null
    private var phoneNumber: EditText ?= null

    private var closeButton: Button ?= null
    private var selectButton: Button ?=null

    val typeHouse = arrayOf("Квартира","Частный дом")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_ad)

        init()

        val arrayAdapter = ArrayAdapter(this@PlusAd,android.R.layout.simple_spinner_dropdown_item, typeHouse)
        spinner.adapter = arrayAdapter
        /*spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Toast.makeText(this@PlusAd,""+typeHouse[p2],Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }*/

        closeButton?.setOnClickListener {
            val closeAd = Intent(this, MainActivity::class.java)
            startActivity(closeAd)
        }

        houseImage?.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else{
                    pickImageFromGallery()
                }
            }else{
                pickImageFromGallery()
            }
        }

        selectButton?.setOnClickListener{
            validatingValue()
        }

    }

    private fun validatingValue() {
        val costCheck = costView?.text.toString().trim()
        val locationCheck = locationView?.text.toString().trim()
        val roomsCheck = roomsView?.text.toString().trim()
        val squareCheck = squareView?.text.toString().trim()
        val infoCheck = infoView?.text.toString().trim()
        val numberCheck = phoneNumber?.text.toString().trim()


        if (costCheck.isNullOrBlank()) {
            costView?.error = "Заполните цену!"

        }
        else if (locationCheck.isNullOrBlank()) {
            locationView?.error = "Заполните адрес!"
        }
        else if (roomsCheck.isNullOrBlank()) {
            roomsView?.error = "Заполните количество комнат!"
        }
        else if (squareCheck.isNullOrBlank()) {
            squareView?.error = "Заполните площадь!"
        }
        else if (infoCheck.isNullOrBlank()) {
            infoView?.error = "Заполните описание!"
        }
        else if (infoCheck.length <= 50) {
            infoView?.error = "Длина описания не менее 50 символов!"
        }
        else if (numberCheck.isNullOrBlank()) {
            phoneNumber?.error = "Заполните номер телефона!"
        }
        else if (numberCheck[0] == '+' && numberCheck[1] != '7')  {
            phoneNumber?.error = "Номер должен начинаться с 8 или +7!"
        }
        else if (numberCheck[0] != '8' && numberCheck[0] != '+' && numberCheck[1] != '7'){
            phoneNumber?.error = "Номер должен начинаться с 8 или +7!"
        }
        else if (numberCheck[0] == '8' && numberCheck.length < 11 || numberCheck[0] == '+' && numberCheck[1] == '7' && numberCheck.length < 12)  {
            phoneNumber?.error = "Номер должен содержать 11 или 12 цифр!"
        }
        else if (numberCheck[0] == '8' && numberCheck.length > 11 || numberCheck[0] == '+' && numberCheck[1] == '7' && numberCheck.length > 12)  {
            phoneNumber?.error = "Номер должен содержать 11 или 12 цифр!"
        }
        else HomeInformation()
    }



    private fun HomeInformation() {
        val value: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar.getInstance()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val DateFormat  = SimpleDateFormat("ddMMyyyy")
        saveCurrentDate = DateFormat.format(value.time)

        val TimeFormat  = SimpleDateFormat("HHmmss")
        saveCurrentTime = TimeFormat.format(value.time)

        RandomKey = saveCurrentDate + saveCurrentTime

        val filePath: StorageReference = HouseImageRef!!.child(ImageUri!!.lastPathSegment + RandomKey + ".jpg")

        val UploadTask: UploadTask = filePath.putFile(ImageUri!!)

        UploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this, "Ошибка: $message", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            Toast.makeText(this, "Изображение успешно загружено.", Toast.LENGTH_SHORT).show()
            UploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                downloadImageUrl = filePath.downloadUrl.toString()
                return@continueWithTask filePath.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadImageUrl = task.result.toString()
                    Toast.makeText(this, "Фото сохранено", Toast.LENGTH_SHORT).show()
                    SaveProductInfoToDatabase()
                }
            }
        }

    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any>()

        productMap["put"] = RandomKey!!
        productMap["date"] = saveCurrentDate!!
        productMap["time"] = saveCurrentTime!!
        productMap["infoV"] = infoView!!
        productMap["numberV"] = phoneNumber!!
        productMap["image"] = downloadImageUrl!!
        productMap["costV"] = costView!!
        productMap["locationV"] = locationView!!
        productMap["roomsV"] = roomsView!!
        productMap["squareV"] = squareView!!
        productMap["tpHouseV"] = typeHouse


        HouseReference?.child(RandomKey!!)?.updateChildren(productMap)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Объявление добавлено", Toast.LENGTH_SHORT).show()
                val AdIntent = Intent(this, MainActivity::class.java)
                startActivity(AdIntent)
            } else {
                val message = task.exception.toString()
                Toast.makeText(this, "Ошибка: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object{
        private const val IMAGE_PICK_CODE = 1000
        private const val PERMISSION_CODE = 1001
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this, "Отказано в разрешении.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            select_home_image.setImageURI(data?.data)
        }
    }

    private fun init(){
        houseImage = findViewById(R.id.select_home_image)
        costView = findViewById(R.id.cost)
        locationView = findViewById(R.id.location)
        roomsView = findViewById(R.id.room)
        squareView = findViewById(R.id.square)
        infoView = findViewById(R.id.info_house)
        phoneNumber = findViewById(R.id.number)
        closeButton = findViewById(R.id.exitBuy)
        selectButton = findViewById(R.id.button)
        HouseImageRef = FirebaseStorage.getInstance().reference.child("House Image")
        HouseReference = FirebaseDatabase.getInstance().reference.child("Houses")
    }


}

