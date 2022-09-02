package com.onedev.googlemaps.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast

fun Context.intentTo(clazz: Class<*>) {
    startActivity(
        Intent(this, clazz)
    )
}

fun Context.showToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()