@file:Suppress("DEPRECATION")

package ru.homebuy.neito

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*


class PlusAd : AppCompatActivity() {
    private var costCheck: String? = null
    private var locationCheck: String? = null
    private var roomsCheck: String? = null
    private var squareCheck: String? = null
    private var infoCheck: String? = null
    private var numberCheck: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var RandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var houseImage: ImageView? = null
    private var costView: EditText? = null
    private var locationView: EditText? = null
    private var roomsView: EditText? = null
    private var squareView: EditText? = null
    private var infoView: EditText? = null
    private var phoneNumber: EditText? = null
    private var selectButton: Button? = null
    private var closeButton: Button? = null
    private var ImageUri: Uri? = null
    private var HouseImageRef: StorageReference? = null
    private var HouseReference: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plus_ad)
        init()
        closeButton!!.setOnClickListener { view ->
            val closeAd = Intent(view.context, MainActivity::class.java)
            view.context.startActivity(closeAd)
        }

        houseImage!!.setOnClickListener { OpenGallery() }

        selectButton!!.setOnClickListener { ValidateProductData() }
    }

    private fun ValidateProductData() {
        costCheck = costView!!.text.toString()
        locationCheck = locationView!!.text.toString()
        roomsCheck = roomsView!!.text.toString()
        squareCheck = squareView!!.text.toString()
        infoCheck = infoView!!.text.toString()
        numberCheck = phoneNumber!!.text.toString()
        if (ImageUri == null) {
            Toast.makeText(this, "Добавьте изображение недвижимости.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(costCheck)) {
            Toast.makeText(this, "Добавьте стоимость недвижимости.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(locationCheck)) {
            Toast.makeText(this, "Добавьте расположение недвижимости.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(roomsCheck)) {
            Toast.makeText(this, "Добавьте количество комнат.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(squareCheck)) {
            Toast.makeText(this, "Добавьте площадь недвижимости.", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(infoCheck)) {
            Toast.makeText(this, "Добавьте описание недввижимости.", Toast.LENGTH_SHORT).show()
        } else if (infoCheck!!.length <= 50) {
            Toast.makeText(this, "Длина описания не менее 50 символов!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(numberCheck)) {
            Toast.makeText(this, "Добавьте номер телефона для связи.", Toast.LENGTH_SHORT).show()
        } else if (numberCheck!![0] == '+' && numberCheck!![1] != '7') {
            Toast.makeText(this, "Номер должен начинаться с 8 или +7!", Toast.LENGTH_SHORT).show()
        } else if (numberCheck!![0] != '8' && numberCheck!![0] != '+' && numberCheck!![1] != '7') {
            Toast.makeText(this, "Номер должен начинаться с 8 или +7!", Toast.LENGTH_SHORT).show()
        } else if (numberCheck!![0] == '8' && numberCheck!!.length < 11 || numberCheck!![0] == '+' && numberCheck!![1] == '7' && numberCheck!!.length < 12) {
            Toast.makeText(this, "Номер должен содержать 11 или 12 цифр!", Toast.LENGTH_SHORT)
                .show()
        } else if (numberCheck!![0] == '8' && numberCheck!!.length > 11 || numberCheck!![0] == '+' && numberCheck!![1] == '7' && numberCheck!!.length > 12) {
            Toast.makeText(this, "Номер должен содержать 11 или 12 цифр!", Toast.LENGTH_SHORT)
                .show()
        } else StoreProductInformation()
    }

    private fun StoreProductInformation() {
        loadingBar!!.setTitle("Загрузка данных")
        loadingBar!!.setMessage("Пожалуйста, подождите...")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("ddMMyyyy")
        saveCurrentDate = currentDate.format(calendar.time)
        val currentTime = SimpleDateFormat("HHmmss")
        saveCurrentTime = currentTime.format(calendar.time)
        RandomKey = saveCurrentDate + saveCurrentTime
        val filePath = HouseImageRef!!.child(ImageUri!!.lastPathSegment + RandomKey + ".jpg")
        val uploadTask = filePath.putFile(ImageUri!!)
        uploadTask.addOnFailureListener { e ->
            val message = e.toString()
            Toast.makeText(this@PlusAd, "Ошибка: $message", Toast.LENGTH_SHORT).show()
            loadingBar!!.dismiss()
        }.addOnSuccessListener {
            Toast.makeText(this@PlusAd, "Изображение успешно загружено.", Toast.LENGTH_SHORT).show()
            uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception!!
                }
                downloadImageUrl = filePath.downloadUrl.toString()
                filePath.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    downloadImageUrl = task.result.toString()
                    Toast.makeText(this@PlusAd, "Фото сохранено", Toast.LENGTH_SHORT).show()
                    SaveProductInfoToDatabase()
                }
            }
        }
    }

    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any?>()
        productMap["pid"] = RandomKey
        productMap["date"] = saveCurrentDate
        productMap["time"] = saveCurrentTime
        productMap["infoV"] = infoView
        productMap["numberV"] = phoneNumber
        productMap["image"] = downloadImageUrl
        productMap["costV"] = costView
        productMap["locationV"] = locationView
        productMap["roomsV"] = roomsView
        productMap["squareV"] = squareView
        HouseReference!!.child(RandomKey!!).updateChildren(productMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loadingBar!!.dismiss()
                    Toast.makeText(this@PlusAd, "Товар добавлен", Toast.LENGTH_SHORT).show()
                    val loginIntent = Intent(this@PlusAd, MainActivity::class.java)
                    startActivity(loginIntent)
                } else {
                    val message = task.exception.toString()
                    Toast.makeText(this@PlusAd, "Ошибка: $message", Toast.LENGTH_SHORT).show()
                    loadingBar!!.dismiss()
                }
            }
    }

    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERYPICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERYPICK && resultCode == RESULT_OK && data != null) {
            ImageUri = data.data
            houseImage!!.setImageURI(ImageUri)
        }
    }

    private fun init() {
        houseImage = findViewById(R.id.select_home_image)
        costView = findViewById(R.id.cost)
        locationView = findViewById(R.id.location)
        roomsView = findViewById(R.id.room)
        squareView = findViewById(R.id.square)
        infoView = findViewById(R.id.info_house)
        phoneNumber = findViewById(R.id.number)
        closeButton = findViewById(R.id.exitBuy)
        selectButton = findViewById(R.id.button)
        HouseImageRef = FirebaseStorage.getInstance().reference.child("House Images")
        HouseReference = FirebaseDatabase.getInstance().reference.child("Houses")
        loadingBar = ProgressDialog(this)
    }

    companion object {
        private const val GALLERYPICK = 1
    }
}

