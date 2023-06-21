package com.example.firebasetestapp.ui.barcode

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.firebasetestapp.R
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class MLBarcodeScannerActivity : AppCompatActivity() {

    private lateinit var svBarcode: SurfaceView
    private lateinit var tvBarcode: TextView
    private lateinit var detector: BarcodeDetector
    private lateinit var cameraSource: CameraSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ml_barcode_scanner)

        svBarcode = findViewById(R.id.sv_barcode)
        tvBarcode = findViewById(R.id.tv_barcode)

        detector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()
        detector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                var barcodes = detections?.detectedItems
                if(barcodes!!.size() > 0){
                    val builder = StringBuilder()
                    tvBarcode.post{
                        builder.append("결과출력 : ")
                        builder.append("\n")
                        builder.append(barcodes.valueAt(0).displayValue)
                        tvBarcode.text = builder.toString()
                        //tvBarcode.text = barcodes.valueAt(0).displayValue
                    }
                }
            }
        })

        cameraSource = CameraSource.Builder(this, detector).setRequestedPreviewSize(1024, 768)
            .setRequestedFps(25f).setAutoFocusEnabled(true).build()

        svBarcode.holder.addCallback(object : SurfaceHolder.Callback2{
            override fun surfaceRedrawNeeded(holder: SurfaceHolder) {}

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {}

            //당장 필요한것은 아래 두개의 메소드이다.
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }

            override fun surfaceCreated(holder: SurfaceHolder) {
                //카메라 퍼미션 체크후에 진행
                if(ContextCompat.checkSelfPermission(this@MLBarcodeScannerActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cameraSource.start(holder)
                } else {
                    ActivityCompat.requestPermissions(this@MLBarcodeScannerActivity, arrayOf(android.Manifest.permission.CAMERA), 123)
                }
            }
        })

    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 123){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                cameraSource.start(svBarcode.holder)
            } else {
                Toast.makeText(this, "스캐너는 카메라 권한이 허용되어야 사용 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detector.release()
        cameraSource.stop()
        cameraSource.release()
    }
}