package com.example.futbolito

import android.R
import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Float.min


@RequiresApi(Build.VERSION_CODES.R)
class Ball(context: Context?) : View(context), SensorEventListener {
    var size: Float = 80F
    var x1 : Float = 0F
    var y1 : Float = 0F
    var z1 : Float = 0F
    var flag: Boolean = false
    var valido1 : Boolean = false
    var valido2 : Boolean = false
    lateinit var bmpCancha : Bitmap
    lateinit  var scaledBmpCancha : Bitmap
    lateinit var bmpBalon : Bitmap
    lateinit var scaledBmpBalon : Bitmap

    init{

        var sm = context?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var acelerometro = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sm.registerListener(this, acelerometro, SensorManager.SENSOR_DELAY_GAME)
        var display = context?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        var width = display.currentWindowMetrics.bounds.width()
        var height = display.currentWindowMetrics.bounds.height()

        bmpCancha = BitmapFactory.decodeResource(resources, com.example.futbolito.R.drawable.cancha)
        scaledBmpCancha = Bitmap.createScaledBitmap(bmpCancha, width, height-(height*0.08).toInt(), false)

        bmpBalon = BitmapFactory.decodeResource(resources, com.example.futbolito.R.drawable.balon)
        scaledBmpBalon = Bitmap.createScaledBitmap(bmpBalon, size.toInt(), size.toInt(), false)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        x1 -= event?.values?.get(0)!!
        y1 += event?.values?.get(1)!!
        z1 = event?.values?.get(2)!!
        Log.d("coordenadas",x1.toString()+" "+y1.toString())

        //COLISIONES
        if(x1>=(width-size)){
            x1 = width- size
        }else if(x1<size){
            x1 = size
        }

        if(y1>=(height-size)){
            y1 = height- size
            valido1 = false
            valido2 = false
        }else if(y1<size){
            y1 = size
            valido1 = false
            valido2 = false
        }

        if(y1>=size*3){
            valido1 = true
        }
        if( y1<=(height-(height*0.08))){
            valido2 = true
        }



        //PORTERIA ARRIBA
        if(x1>=(width/2)-100.toFloat() && x1<=(width/2)+50.toFloat() && y1>size+50 && y1<=size+80 && valido1){
            Toast.makeText(context,"GOL EQUIPO 1!",Toast.LENGTH_SHORT).show()
            x1 = (width/2).toFloat()
            y1 = (height/2-(height*0.08)).toFloat()
        }
        //PORTERIA ABAJO
        if(x1>=(width/2)-100.toFloat() && x1<=(width/2)+90.toFloat() && y1>=(height-(height*0.08)) && y1<height-(height*0.07) && valido2){
            Toast.makeText(context,"GOL EQUIPO 2!",Toast.LENGTH_SHORT).show()
            x1 = (width/2).toFloat()
            y1 = (height/2-(height*0.08)).toFloat()
        }




        invalidate()



    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var painter = Paint()
        canvas!!.drawBitmap(scaledBmpCancha, (0).toFloat(), (0).toFloat(), painter)
        if(flag==false){
            x1 = (width/2).toFloat()
            y1 = (height/2-(height*0.08)).toFloat()
            flag = true
        }
        canvas!!.drawBitmap(scaledBmpBalon, x1, y1, painter)

        painter.setColor(Color.WHITE)
    }
}