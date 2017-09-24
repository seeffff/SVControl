package com.newwesterndev.svcontrol.utils

import android.content.Context
import android.media.AudioManager
import com.pawegio.kandroid.e

class VolController(c: Context, ls: Int, lv: Int) {

    private val mLowSpeed = ls
    private var mLowStreamVolume = lv
    private var mUtility = Utility(c)

    private var mAudioManager: AudioManager =
            c.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun controlVol(speed: Float) {

        e("Low stream vol " + mLowStreamVolume.toString())
        e("Current vol " + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toString())
        e("Current speed " + speed.toString())
        e("Speed to drop vol " + mLowSpeed.toString())

        val currentStreamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)

        if (mAudioManager.isMusicActive) {
            if (speed <= mLowSpeed && currentStreamVolume > mLowStreamVolume){
                mUtility.saveHighStream(currentStreamVolume)
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mLowStreamVolume, 0)
            } else if (speed > mLowSpeed && currentStreamVolume == mLowStreamVolume) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mUtility.getHighStream(), 0)
            }
        }
    }
}
