package com.newwesterndev.svcontrol

import android.content.Context
import android.media.AudioManager
import com.pawegio.kandroid.e

class VolController(c: Context, ls: Int, lv: Int, uv: Int) {

    private val mLowSpeed = ls
    private val mUserHighVolume = uv
    private var mLowStreamVolume = lowStreamVolume(lv.toFloat(), uv.toFloat())

    private var mAudioManager: AudioManager =
            c.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun controlVol(speed: Float) {

        e("Low stream vol " + mLowStreamVolume.toString())
        e("User high vol  " + mUserHighVolume.toString())
        e("Current vol " + mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC).toString())
        e("Current speed " + speed.toString())
        e("Speed to drop vol " + mLowSpeed.toString())

        if (mAudioManager.isMusicActive) {
            if (speed <= mLowSpeed && mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) > mLowStreamVolume){
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, 0)
            } else if (speed > mLowSpeed && mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC) < mUserHighVolume) {
                mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, 0)
            }
        }
    }

    private fun lowStreamVolume(percent: Float, highVolume: Float): Int{
        return ((percent/100) * highVolume).toInt()
    }
}
