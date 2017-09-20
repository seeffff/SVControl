package com.newwesterndev.svcontrol

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import butterknife.bindView
import com.pawegio.kandroid.e
import com.pawegio.kandroid.onProgressChanged

class MainActivity : AppCompatActivity() {

    private val mStartButton: Button by bindView(R.id.start_button)
    private val mLowSpeedSeek: SeekBar by bindView(R.id.low_mph_seek)
    private val mLowVolumeSeek: SeekBar by bindView(R.id.low_volume_seek)
    private val mHighSpeedSeek: SeekBar by bindView(R.id.high_mph_seek)
    private val mHighVolumeSeek: SeekBar by bindView(R.id.high_volume_seek)
    private val mLowSpeedText: TextView by bindView(R.id.low_mph_text)
    private val mLowVolumeText: TextView by bindView(R.id.low_volume_text)
    private val mHighSpeedText: TextView by bindView(R.id.high_mph_text)
    private val mHighVolumeText: TextView by bindView(R.id.high_volume_text)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 10)

        init()

        val i = Intent(this, SVService::class.java)

        mStartButton.setOnClickListener { _ ->
            if (mStartButton.text == getString(R.string.start_button)) {
                i.putExtra("lowspeed", mLowSpeedSeek.progress)
                i.putExtra("highspeed", mHighSpeedSeek.progress)
                i.putExtra("lowvolume", mLowVolumeSeek.progress)
                i.putExtra("highvolume", mHighVolumeSeek.progress)
                startService(i)
                enableSeekBars(false)
                showToast("Service has been started")
                mStartButton.setText(getString(R.string.stop_button))
            } else {
                stopService(i)
                enableSeekBars(true)
                showToast("Service has been stopped")
                mStartButton.setText(getString(R.string.start_button))
            }
        }

            mLowSpeedSeek.onProgressChanged { progress, _ ->
                var progressString: String = progress.toString() + " " + resources.getString(R.string.speed_text)
                mLowSpeedText.text = progressString
            }
            mHighSpeedSeek.onProgressChanged { progress, _ ->
                var progressString: String = progress.toString() + " " + resources.getString(R.string.speed_text)
                mHighSpeedText.text = progressString
            }
            mLowVolumeSeek.onProgressChanged { progress, _ ->
                var progressString: String = progress.toString() + " " + resources.getString(R.string.percent_text)
                mLowVolumeText.text = progressString
            }
            mHighVolumeSeek.onProgressChanged { progress, _ ->
                var progressString: String = progress.toString() + " " + resources.getString(R.string.percent_text)
                mHighVolumeText.text = progressString
            }

    }

    private fun init(){
        mLowSpeedSeek.max = 100
        mHighSpeedSeek.max = 100
        mLowVolumeSeek.max = 100
        mHighVolumeSeek.max = 100
    }

    private fun enableSeekBars(bool: Boolean){
        mLowSpeedSeek.isEnabled = bool
        mHighSpeedSeek.isEnabled = bool
        mLowVolumeSeek.isEnabled = bool
        mHighVolumeSeek.isEnabled = bool
    }

    private fun showToast(message: String){
        val t: Toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        t.show()
    }
}
