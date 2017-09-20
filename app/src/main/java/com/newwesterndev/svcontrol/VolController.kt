package com.newwesterndev.svcontrol

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.provider.MediaStore
import com.pawegio.kandroid.e

class VolController(c: Context, ls: Int, hs: Int, lv: Int, hv: Int){

    private val mContext = c
    private val mLowSpeed = ls
    private val mHighSpeed = hs
    private val mLowVolume = lv
    private val mHighVolume = hv

    private var mAudioManager: AudioManager =
            mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    fun controlVol(speed: Float){
        e(speed.toString())
        if(mAudioManager.isMusicActive){
            e("true")
        }
        else{
            e("false")
        }
    }
}
