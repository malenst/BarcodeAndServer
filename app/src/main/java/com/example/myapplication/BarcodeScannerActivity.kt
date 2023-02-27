package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException
import android.Manifest
import androidx.core.app.ActivityCompat

class BarcodeScannerActivity : AppCompatActivity() {
    private var isResultActivityRunning = false
    companion object {
        const val BARCODE_RESULT = 100
    }
    private lateinit var cameraPreview: SurfaceView
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barcode_scanner)

        cameraPreview = findViewById(R.id.camera_preview)
        barcodeDetector = BarcodeDetector.Builder(this).build()
        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setAutoFocusEnabled(true)
            .setRequestedPreviewSize(1600, 1024)
            .build()
        val CAMERA_PERMISSION_REQUEST_CODE = 123
        cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                if (ContextCompat.checkSelfPermission(this@BarcodeScannerActivity, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@BarcodeScannerActivity,
                        arrayOf(Manifest.permission.CAMERA),
                        CAMERA_PERMISSION_REQUEST_CODE)
                } else {
                    try {
                        cameraSource.start(holder)
                    } catch (e: IOException) {
                        // Handle error
                    }
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Handle changes in surface format and size
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                // Do nothing
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                if (isResultActivityRunning) {
                    return
                }
                val barcodes = detections.detectedItems
                if (barcodes.size() > 0) {
                    val barcode = barcodes.valueAt(0)
                    isResultActivityRunning = true

                    val intent = Intent(this@BarcodeScannerActivity, ResultActivity::class.java)
                    intent.putExtra("barcode", barcode.displayValue)
                    //cameraSource.stop()
                    startActivity(intent)

                }
            }
        })
    }
}


