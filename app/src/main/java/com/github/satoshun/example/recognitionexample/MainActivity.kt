package com.github.satoshun.example.recognitionexample

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)


    val transitions = ArrayList<ActivityTransition>()
    transitions.add(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
    )
    transitions.add(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.WALKING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
    )
    val request = ActivityTransitionRequest(transitions)

    val i = Intent(Intent.ACTION_VIEW)
    val pending = PendingIntent
        .getActivity(this, 1, i, PendingIntent.FLAG_ONE_SHOT)

    val task: Task<Void?> = ActivityRecognition
        .getClient(this)
        .requestActivityTransitionUpdates(request, pending)

    task.addOnSuccessListener { result ->
      Log.d("addOnSuccessListener", result.toString())
    }
    task.addOnCompleteListener { result ->
      Log.d("addOnCompleteListener", result.toString())
    }
    task.addOnFailureListener { result ->
      Log.d("addOnFailureListener", result.toString())
    }
  }
}
