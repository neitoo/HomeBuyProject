package ru.homebuy.neito

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.homebuy.neito.Interface.ItemClickListner

class HouseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    var txtCost: TextView
    var txtLocation: TextView
    var txtInfo: TextView
    var imageView: ImageView
    var listner: ItemClickListner? = null
    fun setItemClickListner(listner: ItemClickListner?) {
        this.listner = listner
    }

    override fun onClick(view: View?) {
        listner?.onClick(view, getAdapterPosition(), false)
    }

    init {
        imageView = itemView.findViewById(R.id.house_image)
        txtCost = itemView.findViewById(R.id.costHouseAd)
        txtLocation = itemView.findViewById(R.id.locationAd)
        txtInfo = itemView.findViewById(R.id.cropInfoAd)
    }
}