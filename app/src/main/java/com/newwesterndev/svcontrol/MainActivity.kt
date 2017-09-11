package com.newwesterndev.svcontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.Toast
import butterknife.bindView

class MainActivity : AppCompatActivity() {

    private val mStartButton: Button by bindView(R.id.start_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 10)

        val i = Intent(this, SVService::class.java)

        mStartButton.setOnClickListener { _ ->
            if(mStartButton.text == getString(R.string.start_button)) {
                startService(i)
                showToast("Service has been started")
                mStartButton.setText(getString(R.string.stop_button))
            }
            else {
                stopService(i)
                showToast("Service has been stopped")
                mStartButton.setText(getString(R.string.start_button))
            }
        }
    }

    private fun showToast(message: String){
        val t: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        t.show()
    }
}
