package ru.homebuy.neito.Interface

import android.view.View


interface ItemClickListner {
    fun onClick(view: View?, position: Int, isLongClick: Boolean)
}