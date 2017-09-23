package com.newwesterndev.svcontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.pawegio.kandroid.onProgressChanged
import kotterknife.bindView

class MainActivity : AppCompatActivity() {

    private val mStartButton: Button by bindView(R.id.start_button)
    private val mLowSpeedSeek: SeekBar by bindView(R.id.low_mph_seek)
    private val mLowVolumeSeek: SeekBar by bindView(R.id.low_volume_seek)
    private val mLowSpeedText: TextView by bindView(R.id.low_mph_text)
    private val mLowVolumeText: TextView by bindView(R.id.low_volume_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 10)

        init()
        val i = Intent(this, SVService::class.java)

        mStartButton.setOnClickListener { _ ->
            if (mStartButton.text == getString(R.string.start_button)) {
                activateService(i)
            } else {
                deactivateService(i)
            }
        }

        mLowSpeedSeek.onProgressChanged { progress, _ ->
            var progressString: String = progress.toString() + " " + resources.getString(R.string.speed_text)
            mLowSpeedText.text = progressString
        }
        mLowVolumeSeek.onProgressChanged { progress, _ ->
            var progressString: String = progress.toString() + " " + resources.getString(R.string.percent_text)
            mLowVolumeText.text = progressString
        }

        if(savedInstanceState != null){
            var isRunning = savedInstanceState.getBoolean("running")
            if(isRunning){
                enableServiceActiveUI()
            }
        }
    }

    fun activateService(i: Intent) {
        i.putExtra("lowspeed", mLowSpeedSeek.progress)
        i.putExtra("lowvolume", mLowVolumeSeek.progress)
        startService(i)
        mIsServiceRunning = true
        enableSeekBars(false)
        showToast("Service has been started")
        mStartButton.setText(getString(R.string.stop_button))
    }

    fun deactivateService(i: Intent) {
        stopService(i)
        mIsServiceRunning = false
        enableSeekBars(true)
        showToast("Service has been stopped")
        mStartButton.setText(getString(R.string.start_button))
    }

    fun enableServiceActiveUI(){
        enableSeekBars(false)
        mStartButton.setText(getString(R.string.stop_button))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putBoolean("running", mIsServiceRunning)
        super.onSaveInstanceState(outState)
    }

    private fun init() {
        mLowSpeedSeek.max = 50
        mLowVolumeSeek.max = 100
    }

    private fun enableSeekBars(bool: Boolean) {
        mLowSpeedSeek.isEnabled = bool
        mLowVolumeSeek.isEnabled = bool
    }

    private fun showToast(message: String) {
        val t: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        t.show()
    }

    companion object {
        var mIsServiceRunning = false
    }
}
