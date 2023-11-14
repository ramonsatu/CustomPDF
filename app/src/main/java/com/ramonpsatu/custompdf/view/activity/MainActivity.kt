package com.ramonpsatu.custompdf.view.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ramonpsatu.custompdf.R
import com.ramonpsatu.custompdf.callback.PermissionsCallback
import com.ramonpsatu.custompdf.utils.CheckPermissions
import com.ramonpsatu.custompdf.utils.PDFGenerator

class MainActivity : AppCompatActivity(), PermissionsCallback {

    private lateinit var btnGenerate: Button
    private lateinit var textPermissionsIsGranted: TextView
    private lateinit var editText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnGenerate = findViewById(R.id.button_generate)
        textPermissionsIsGranted = findViewById(R.id.permission_is_granted)
        editText = findViewById(R.id.number_of_subjects)
        checkPermissions()

        btnGenerate.setOnClickListener {

            PDFGenerator().printOutBlankSubjectGrid(editText.text.toString(),
                baseContext,this@MainActivity)

        }

    }

    private fun viewStateByPermissions(boolean: Boolean){
        textPermissionsIsGranted.visibility = View.VISIBLE
        if (boolean){
            btnGenerate.alpha = 1f
            btnGenerate.isClickable = boolean
            textPermissionsIsGranted.text = getString(R.string.text_permissions_granted)

        }else{
            btnGenerate.alpha = 0.5f
            btnGenerate.isClickable = boolean
            textPermissionsIsGranted.text = getString(R.string.text_dialog_permissions)

        }
    }


    private fun checkPermissions(){
        if (ContextCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            CheckPermissions(this).checkBuildVersionAndPermissions(baseContext,this)
        }else{
            viewStateByPermissions(true)
        }
    }

    override fun permissionsIsGranted(isGranted: Boolean) {
        viewStateByPermissions(isGranted)


    }
}