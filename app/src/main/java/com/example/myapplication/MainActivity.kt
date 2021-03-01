package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import base.BaseFlow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import flows.MainFlow


class MainActivity : AppCompatActivity() {
    var rootFlow : BaseFlow? = null
    var imageLoaded : OnImageLoaded? = null

    private var auth : FirebaseAuth? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.out.println("isCalled 1")
        if (savedInstanceState == null){
            System.out.println("isCalled 2")
            Handler().postDelayed({
                init()
            }, 100)
        }
    }

    fun init(){
        supportActionBar?.hide();
        setContentView(R.layout.activity_main)

        var mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)

        searchAndViewScreen()
    }

    fun searchAndViewScreen(){
        if (rootFlow == null) {
            rootFlow = MainFlow()
        }
        rootFlow?.currentStep()?.let{
            startFragment(it)
        }
    }
    private fun startFragment(fragment : Fragment){
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.justAPlaceOrder, fragment).commit()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, requestCode, data)
        if (resultCode == Activity.RESULT_OK && data?.data != null){
            imageLoaded?.onImageLoaded(data.data!!)
        }
    }

    override fun onBackPressed() {
        rootFlow?.currentUIStep()?.onBackPressed()
        searchAndViewScreen()
    }

    interface OnImageLoaded{
        fun onImageLoaded(uir : Uri)
    }
}