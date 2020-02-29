package com.yorkismine.sensorapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread
import kotlin.math.abs

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var manager: SensorManager
    private lateinit var vibrator: Vibrator
    private lateinit var cameraManager: CameraManager

    private val accelerometerReading = FloatArray(3)
    private val magneticReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val rotationAngles = FloatArray(3)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val state = FloatArray(3)

        manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager

        saveAnglesBtn.setOnClickListener {
            System.arraycopy(rotationAngles, 0, state, 0, state.size)

            result_tv.text =
                "Result -->  X: ${state[0] * 60} Y: ${state[1] * 60} Z: ${state[2] * 60}"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event!!.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magneticReading, 0, magneticReading.size)
        }

        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magneticReading)
        SensorManager.getOrientation(rotationMatrix, rotationAngles)

        textView.text =
            "X: ${rotationAngles[0] * 60} Y: ${rotationAngles[1] * 60} Z: ${rotationAngles[2] * 60}"

        if ((abs(rotationAngles[0] * 60).toInt() == 30) or (abs(rotationAngles[0] * 60).toInt() == -30)) {
            vibrate()
            showToast()
            flash()
            playSound()
        }
        if ((abs(rotationAngles[1] * 60).toInt() == 30) or (abs(rotationAngles[1] * 60).toInt() == -30)) {
            vibrate()
            showToast()
            flash()
            playSound()
        }
        if ((abs(rotationAngles[2] * 60).toInt() == 30) or (abs(rotationAngles[2] * 60).toInt() == -30)) {
            vibrate()
            showToast()
            flash()
            playSound()
        }
    }


    override fun onResume() {
        super.onResume()

        manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).let { ac ->
            manager.registerListener(
                this,
                ac,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }

        manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD).let { m ->
            manager.registerListener(
                this,
                m,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
            )
        }
    }

    override fun onPause() {
        super.onPause()

        manager.unregisterListener(this)
    }

    private fun showToast() =
        Toast.makeText(this, "Angle is +- 30!", Toast.LENGTH_SHORT).show()

    private fun vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(2000, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(2000)
        }
    }

    private fun flash() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            thread {
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                Thread.sleep(250)
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
                Thread.sleep(250)
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                Thread.sleep(250)
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)
                Thread.sleep(250)
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], true)
                Thread.sleep(250)
                cameraManager.setTorchMode(cameraManager.cameraIdList[0], false)

            }

        }
    }

    private fun playSound() {
        val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        val ringtoneSound: Ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
        thread {
            ringtoneSound.play()
            Thread.sleep(2000)
            ringtoneSound.stop()
        }

    }
}