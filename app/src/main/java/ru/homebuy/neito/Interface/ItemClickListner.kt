package ru.homebuy.neito.Interface



interface ItemClickListner {
    fun onClick(view: android.view.View?, position: Int, isLongClick: Boolean)
}