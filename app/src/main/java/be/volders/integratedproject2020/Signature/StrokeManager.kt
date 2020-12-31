package be.volders.integratedproject2020

import android.content.Context
import android.os.Debug
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.vision.digitalink.*

object StrokeManager {
    private var inkBuilder = Ink.builder()
    private var strokeBuilder = Ink.Stroke.builder()
    private lateinit var model: DigitalInkRecognitionModel

    fun addNewTouchEvent(event: MotionEvent, releaseCounter : Int) : Int  {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        val t = System.currentTimeMillis()
        var releases = releaseCounter

        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> strokeBuilder.addPoint(
                Ink.Point.create(
                    x,
                    y,
                    t
                )
            )
            MotionEvent.ACTION_UP -> {
                strokeBuilder.addPoint(Ink.Point.create(x, y, t))
                inkBuilder.addStroke(strokeBuilder.build())
                strokeBuilder = Ink.Stroke.builder()

                releases++

            }
            else -> {
                // Action not relevant for ink construction
            }
        }

        return releases

    }

    fun download() {
        var modelIdentifier: DigitalInkRecognitionModelIdentifier? = null
        try {
            modelIdentifier =
                DigitalInkRecognitionModelIdentifier.fromLanguageTag("zxx-Zsym-x-autodraw")
        } catch (e: MlKitException) {
            // language tag failed to parse, handle error.
        }

        model =
            DigitalInkRecognitionModel.builder(modelIdentifier!!).build()

        val remoteModelManager = RemoteModelManager.getInstance()
        remoteModelManager.download(model, DownloadConditions.Builder().build())
            .addOnSuccessListener {
                Log.i("StrokeManager", "Model downloaded")
            }
            .addOnFailureListener { e: Exception ->
                Log.e("StrokeManager", "Error while downloading a model: $e")
            }
    }

    fun recognize(context: Context) {
        val recognizer: DigitalInkRecognizer =
            DigitalInkRecognition.getClient(
                DigitalInkRecognizerOptions.builder(model).build()
            )

        val ink = inkBuilder.build()

        recognizer.recognize(ink)
            .addOnSuccessListener { result: RecognitionResult ->
               // Toast.makeText(context, "${result.candidates[0].text}", Toast.LENGTH_LONG).show()
                Toast.makeText(context, "${result}", Toast.LENGTH_LONG).show()
                Log.d("ink", "${ink}")
            }
            .addOnFailureListener { e: Exception ->
                Log.e("StrokeManager", "Error during recognition: $e")
            }
    }

    fun clear() {
        inkBuilder = Ink.builder()
    }
}