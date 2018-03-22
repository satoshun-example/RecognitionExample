package com.github.satoshun.example.recognitionexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.ActivityTransitionResult


class MyBroadcastReceiver : BroadcastReceiver() {
  override fun onReceive(context: Context, intent: Intent) {
    Log.d("onReceive2", "enter")
    if (ActivityTransitionResult.hasResult(intent)) {
      val result = ActivityTransitionResult.extractResult(intent)
      Log.d("onReceive", result.toString())

      for (event in result!!.transitionEvents) {
        // chronological sequence of events....
      }
    }
  }
}
