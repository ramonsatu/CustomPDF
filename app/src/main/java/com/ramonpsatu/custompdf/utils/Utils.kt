package com.ramonpsatu.custompdf.utils

import android.content.Context
import android.widget.Toast

fun toastMessageShort(context: Context, string: String){
    Toast.makeText(context,string, Toast.LENGTH_SHORT).show()
}

fun toastMessageLong(context: Context, string: String){
    Toast.makeText(context,string, Toast.LENGTH_LONG).show()
}