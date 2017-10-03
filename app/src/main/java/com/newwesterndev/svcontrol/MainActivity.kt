package com.newwesterndev.svcontrol

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.newwesterndev.svcontrol.utils.SVService
import com.newwesterndev.svcontrol.utils.Utility
import com.pawegio.kandroid.e
import com.pawegio.kandroid.onProgressChanged
import com.pawegio.kandroid.startActivity
import kotterknife.bindView

class MainActivity : AppCompatActivity() {

    private val mStartButton: Button by bindView(R.id.start_button)
    private val mLowSpeedSeek: SeekBar by bindView(R.id.low_mph_seek)
    private val mLowVolumeSeek: SeekBar by bindView(R.id.low_volume_seek)
    private val mLowSpeedText: TextView by bindView(R.id.low_mph_text)
    private val mLowVolumeText: TextView by bindView(R.id.low_volume_text)
    private val mUtility = Utility(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 10)

        if(intent.getStringExtra("notifIntent") != null) {
            enableServiceActiveUI()
        }

        init(savedInstanceState)
        val i = Intent(this, SVService::class.java)

        mStartButton.setOnClickListener { _ ->
            if (mStartButton.text == getString(R.string.start_button)) {
                activateService(i)
            } else {
                deactivateService(i)
            }
        }

        mLowSpeedSeek.onProgressChanged { progress, _ ->
            val progressString: String = "   " + progress.toString() + " " + mUtility.getSpeedUnits()
            mLowSpeedText.text = progressString
        }
        mLowVolumeSeek.onProgressChanged { progress, _ ->
            val progressString: String = "   " + progress.toString()
            mLowVolumeText.text = progressString
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if(id == R.id.action_settings){
            startActivity(Intent(this, SettingsActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun activateService(i: Intent) {
        i.putExtra("lowspeed", mLowSpeedSeek.progress)
        i.putExtra("lowvolume", mLowVolumeSeek.progress)
        i.putExtra("speedunits", mUtility.getSpeedUnits())
        startService(i)
        mIsServiceRunning = true
        enableSeekBars(false)
        mStartButton.setText(getString(R.string.stop_button))
    }

    private fun deactivateService(i: Intent) {
        stopService(i)
        mIsServiceRunning = false
        enableSeekBars(true)
        mStartButton.setText(getString(R.string.start_button))
    }

    private fun enableServiceActiveUI(){
        enableSeekBars(false)
        mStartButton.setText(getString(R.string.stop_button))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putBoolean("running", mIsServiceRunning)
        super.onSaveInstanceState(outState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mAudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val defText = "   0 " + mUtility.getSpeedUnits()

        mLowSpeedText.text = defText
        mLowVolumeText.text = "   0"
        mLowSpeedSeek.max = 50
        mLowVolumeSeek.max = mAudioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)!!.toInt()

        if(savedInstanceState != null){
            val isRunning = savedInstanceState.getBoolean("running")
            if(isRunning){
                enableServiceActiveUI()
            }
        }
    }

    private fun enableSeekBars(bool: Boolean) {
        mLowSpeedSeek.isEnabled = bool
        mLowVolumeSeek.isEnabled = bool
    }

    companion object {
        var mIsServiceRunning = false
        var mAudioManager: AudioManager? = null
    }
}
