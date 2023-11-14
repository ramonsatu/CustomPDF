package com.ramonpsatu.custompdf.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.ramonpsatu.custompdf.callback.PermissionsCallback
import com.ramonpsatu.custompdf.R

class CheckPermissions(private val permissionsCallback: PermissionsCallback) {


    private lateinit var permissions: Array<String>

    fun checkBuildVersionAndPermissions(context: Context, activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT in 26..28) {
            permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            handlePermission(context, activity)

        } else if (Build.VERSION.SDK_INT in 29..32) {
            permissions = arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            handlePermission(context, activity)

        } else if (Build.VERSION.SDK_INT >= 33) {
            //Not necessary for this version of the API and higher
            permissionsCallback.permissionsIsGranted(true)
        }

    }

    private fun handlePermission(context: Context, activity: AppCompatActivity) {
        val permissionsRequestCallBack = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->

            var counterTrue = 0
            for (index in result) {
                if (index.value) counterTrue += 1
            }

            if (counterTrue == permissions.size) {
                toastMessageLong(context, context.getString(R.string.text_permissions_granted))
                permissionsCallback.permissionsIsGranted(true)
            }else{
                permissionsCallback.permissionsIsGranted(false)

            }
        }
        permissionsRequestCallBack.launch(permissions)
    }
}