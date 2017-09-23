package com.newwesterndev.svcontrol

import android.app.*
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log

class SVService : Service() {
    var locationManager: LocationManager? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val lowSpeed = intent!!.getIntExtra("lowspeed", 0)
        val lowVolumePercent = intent.getIntExtra("lowvolume", 0)

        val mAudioManger: AudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mStreamVol = mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC)
        mVolController = VolController(this, lowSpeed, lowVolumePercent,
                mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC))

        return START_STICKY
    }

    override fun onCreate() {
        if (locationManager == null)
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, DISTANCE, locationListeners[1])
        } catch (e: SecurityException) {
            Log.e(TAG, "Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "Network provider does not exist", e)
        }

        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, locationListeners[0])
        } catch (e: SecurityException) {
            Log.e(TAG, "Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Log.e(TAG, "GPS provider does not exist", e)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (locationManager != null)
            for (i in 0..locationListeners.size) {
                try {
                    locationManager?.removeUpdates(locationListeners[i])
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to remove location listeners")
                }
            }
    }

    companion object {
        val TAG = "LocationTrackingService"
        val INTERVAL = 750.toLong() // In milliseconds
        val DISTANCE = 0.toFloat() // In meters

        val locationListeners = arrayOf(
                LTRLocationListener(LocationManager.GPS_PROVIDER),
                LTRLocationListener(LocationManager.NETWORK_PROVIDER)
        )

        var mVolController: VolController? = null
        var mStreamVol: Int? = null

        class LTRLocationListener(provider: String) : android.location.LocationListener {

            val lastLocation = Location(provider)

            override fun onLocationChanged(location: Location?) {
                lastLocation.set(location)
                mVolController!!.controlVol(location!!.speed)
            }

            override fun onProviderDisabled(provider: String?) {
            }

            override fun onProviderEnabled(provider: String?) {
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            }

        }
    }
}
