package com.example.myapplication


import DominantColorTask
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.gms.vision.barcode.Barcode
import android.Manifest


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private val RC_BARCODE_CAPTURE = 9001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val firstOval = findViewById<ImageView>(R.id.circle_green)
        val secondOval = findViewById<ImageView>(R.id.circle_blue)
        val scannerButton = findViewById<Button>(R.id.scanner_button)


        val rotationAnimator = ValueAnimator.ofFloat(0f, 360f)
        rotationAnimator.duration = 50000
        rotationAnimator.repeatMode = ValueAnimator.RESTART
        rotationAnimator.repeatCount = ValueAnimator.INFINITE
        rotationAnimator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            firstOval.rotation = value
            secondOval.rotation = value
        }

        val animator = ValueAnimator.ofFloat(0f, 1f, 0f)
        animator.duration = 50000
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            firstOval.translationX = 1700 * value
            secondOval.translationX = -1700 * value
        }

        val mainAnimatorSet = AnimatorSet()
        mainAnimatorSet.playTogether(rotationAnimator, animator)
        mainAnimatorSet.start()

        scannerButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                startActivityForResult(Intent(this, ResultActivity::class.java), RC_BARCODE_CAPTURE)
            }
        }

    }
        /*val imageView = findViewById<ImageView>(R.id.bookCover)

        val imageUrl = "https://s1.livelib.ru/boocover/1000322337/200x305/c386/boocover.jpg"

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        DominantColorTask(this, firstOval, secondOval).execute(imageUrl)*/

    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == Activity.RESULT_OK) {
                val barcode = data?.getParcelableExtra<Barcode>("barcode")
                // Use the barcode result
            } else {
                // Handle error
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
        val intent = Intent(this, BarcodeScannerActivity::class.java)
        intent.putExtra("barcode", barcode)
        startActivity(intent)
    }*/
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(Intent(this, BarcodeScannerActivity::class.java), RC_BARCODE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_BARCODE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val barcode = data?.getIntExtra(BarcodeScannerActivity.BARCODE_RESULT.toString(), -1)
            if (barcode != null) {
                Toast.makeText(this, barcode.toString(), Toast.LENGTH_LONG).show()
            }
        }

    }
}