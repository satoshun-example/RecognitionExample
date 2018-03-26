package com.github.satoshun.example.recognitionexample

import android.app.IntentService
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.ActivityRecognitionResult
import com.google.android.gms.location.ActivityTransition
import com.google.android.gms.location.ActivityTransitionRequest
import com.google.android.gms.location.ActivityTransitionResult
import com.google.android.gms.location.DetectedActivity
import com.google.android.gms.tasks.Task

private var activity: MainActivity? = null


class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    activity = this
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
    transitions.add(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
            .build()
    )
    transitions.add(
        ActivityTransition.Builder()
            .setActivityType(DetectedActivity.RUNNING)
            .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
            .build()
    )
    val request = ActivityTransitionRequest(transitions)

    val i = Intent(Intent.ACTION_VIEW)
    val pending = PendingIntent
        .getActivity(this, 1, i, PendingIntent.FLAG_ONE_SHOT)

    val ii = Intent(this, Pending::class.java)
    val pending2 = PendingIntent
        .getService(this, 1, ii, PendingIntent.FLAG_ONE_SHOT)

//    val task: Task<Void?> = ActivityRecognition
//        .getClient(this)
//        .requestActivityTransitionUpdates(request, pending)

//    val task: Task<Void?> = ActivityRecognition
//        .getClient(this)
//        .requestActivityUpdates(1000L, pending2)

    val task: Task<Void?> = ActivityRecognition
        .getClient(this)
        .requestActivityTransitionUpdates(request, pending2)


    task.addOnSuccessListener { result ->
      Log.d("addOnSuccessListener", result.toString())
      addView("addOnSuccessListener $result")
    }
    task.addOnCompleteListener { result ->
      Log.d("addOnCompleteListener", result.toString())
      addView("addOnCompleteListener $result")
    }
    task.addOnFailureListener { result ->
      Log.d("addOnFailureListener", result.toString())
      addView("addOnFailureListener $result")
    }
  }

  fun addView(text: String) {
    findViewById<ViewGroup>(R.id.root).addView(TextView(this).apply {
      setText(text)
    })
  }
}


class Pending : IntentService("test") {
  override fun onHandleIntent(intent: Intent) {
    Log.d("onHandleIntent", intent.toString())
    Log.d("onHandleIntent", intent.extras.toString())

    if (ActivityRecognitionResult.hasResult(intent)) {
      Log.d("hasresult", "enter")
      val result = ActivityRecognitionResult.extractResult(intent)
      for (event in result.probableActivities) {
        Log.d("event", event.toString())
        Handler(Looper.getMainLooper()).post {
          activity!!.addView("ActivityRecognitionResult $event")
        }
      }
    }

    if (ActivityTransitionResult.hasResult(intent)) {
      Log.d("hasresult", "enter")
      val result = ActivityTransitionResult.extractResult(intent)
      for (event in result!!.transitionEvents) {
        Log.d("event", event.toString())
        Handler(Looper.getMainLooper()).post {
          activity!!.addView("ActivityTransitionResult $event")
        }
      }
    }
  }
}
