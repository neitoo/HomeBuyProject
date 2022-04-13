package ru.homebuy.neito

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.annotations.NotNull
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import ru.homebuy.neito.Model.Houses

class MainActivity : AppCompatActivity() {

    /*var HouseReference: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
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


        recyclerView = findViewById(R.id.recycler_menu)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
    }
    override fun onStart() {
        super.onStart()
        val options = HouseReference?.let {
            FirebaseRecyclerOptions.Builder<Houses>()
                .setQuery(it, Houses::class.java).build()
        }
        val adapter: FirebaseRecyclerAdapter<Houses, HouseViewHolder> = object : FirebaseRecyclerAdapter<Houses, HouseViewHolder>(options!!) {
                override fun onBindViewHolder(
                    @NotNull holder: HouseViewHolder,
                    i: Int,
                    @NotNull model: Houses
                ) {
                    holder.txtCost.text = model.getCostV()
                    holder.txtLocation.text = model.getLocationV()
                    holder.txtInfo.text = model.getInfoV()
                    Picasso.get().load(model.getImage()).into(holder.imageView)
                }

                @NotNull
                override fun onCreateViewHolder(
                    @NotNull parent: ViewGroup,
                    viewType: Int
                ): HouseViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.house_image_layout, parent, false)
                    return HouseViewHolder(view)
                }
            }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }*/

    var HouseReference: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        HouseReference = FirebaseDatabase.getInstance().reference.child("Houses")
        val PlusAdActivity = findViewById<View>(R.id.addAnAd) as Button
        PlusAdActivity.setOnClickListener { view ->
            val intent = Intent(view.context, PlusAd::class.java)
            view.context.startActivity(intent)
        }

        settingButton.setOnClickListener(){
            val intentSet = Intent(this, setting_homes::class.java)
            startActivity(intentSet)
        }
        recyclerView = findViewById(R.id.recycler_menu)
        recyclerView?.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView?.setLayoutManager(layoutManager)
    }

    override fun onStart() {
        super.onStart()
        val options = FirebaseRecyclerOptions.Builder<Houses>()
            .setQuery(HouseReference!!, Houses::class.java).build()
        val adapter: FirebaseRecyclerAdapter<Houses, HouseViewHolder> =
            object : FirebaseRecyclerAdapter<Houses, HouseViewHolder>(options) {
                override fun onBindViewHolder(
                    @NotNull holder: HouseViewHolder,
                    i: Int,
                    @NotNull model: Houses
                ) {
                    holder.txtCost.setText(model.getCostV())
                    holder.txtLocation.setText(model.getLocationV())
                    holder.txtInfo.setText(model.getInfoV())
                    Picasso.get().load(model.getImage()).into(holder.imageView)
                }

                @NotNull
                override fun onCreateViewHolder(
                    @NotNull parent: ViewGroup,
                    viewType: Int
                ): HouseViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.house_image_layout, parent, false)
                    return HouseViewHolder(view)
                }
            }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }

}