package com.newwesterndev.svcontrol.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import com.newwesterndev.svcontrol.MainActivity
import com.newwesterndev.svcontrol.R

class SVService : Service() {
    private var locationManager: LocationManager? = null

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val lowSpeed = intent!!.getIntExtra("lowspeed", 0)
        val lowVolume = intent.getIntExtra("lowvolume", 0)
        mSpeedUnits = intent.getStringExtra("speedunits")

        val mAudioManger: AudioManager = this.getSystemService(Context.AUDIO_SERVICE) as AudioManager

        mVolController = VolController(this, lowSpeed, lowVolume)
        val mUtility = Utility(this)
        mUtility.saveHighStream(mAudioManger.getStreamVolume(AudioManager.STREAM_MUSIC))

        showNotification()

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
        mNotificationManager!!.cancel(1)
        if (locationManager != null)
            for (i in 0..locationListeners.size) {
                try {
                    locationManager?.removeUpdates(locationListeners[i])
                } catch (e: Exception) {
                    Log.w(TAG, "Failed to remove location listeners")
                }
            }
        super.onDestroy()
    }

    private fun showNotification(){
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), 0)
        val builder = Notification.Builder(this)

        builder.setAutoCancel(false)
        builder.setContentTitle(resources.getString(R.string.app_name))
        builder.setContentText(resources.getString(R.string.notif_text))
        builder.setSmallIcon(R.drawable.ic_volume_up_white_18dp)
        builder.setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
        builder.setContentIntent(pendingIntent)
        builder.setOngoing(true)
        builder.build()

        var notification = builder.notification
        notification.flags = Notification.FLAG_ONGOING_EVENT
        mNotificationManager!!.notify(1, notification)
    }

    companion object {
        val TAG = "LocationTrackingService"
        val INTERVAL = 750.toLong() // In milliseconds
        val DISTANCE = 0.toFloat() // In meters
        val MPS_TO_MPH = 2.23694
        val MPS_TO_KPH = 3.6

        val locationListeners = arrayOf(
                LTRLocationListener(LocationManager.GPS_PROVIDER),
                LTRLocationListener(LocationManager.NETWORK_PROVIDER)
        )

        var mVolController: VolController? = null
        var mSpeedUnits: String? = null
        var mNotificationManager: NotificationManager? = null

        class LTRLocationListener(provider: String) : android.location.LocationListener {

            private val lastLocation = Location(provider)

            override fun onLocationChanged(location: Location?) {
                lastLocation.set(location)
                if (mSpeedUnits == "mph")
                    mVolController!!.controlVol((location!!.speed * MPS_TO_MPH).toFloat())
                else
                    mVolController!!.controlVol((location!!.speed * MPS_TO_KPH).toFloat())
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
