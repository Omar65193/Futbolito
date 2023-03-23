package com.example.futbolito

import android.content.Context
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.R)
class Ball(context: Context?) : View(context), SensorEventListener {
    var size: Float = 80F
    var x1 : Float = 0F
    var y1 : Float = 0F
    var vx1 : Float = 0F
    var vy1 : Float = 0F
    var gx1 : Float = 0F
    var gy1 : Float = 0F
    var lx1 : Float = 0F
    var ly1 : Float = 0F
    var direccionX = -1f;
    var direccionY = 1f;
    var contEq1: Int = 0
    var contEq2: Int = 0
    var flag: Boolean = false
    var valido1 : Boolean = false
    var valido2 : Boolean = false
    var bmpCancha : Bitmap
    var scaledBmpCancha : Bitmap
    var bmpBalon : Bitmap
    var scaledBmpBalon : Bitmap
    var pincel = Paint()

    init{
        pincel.setColor(Color.WHITE)
        pincel.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD))
        pincel.textSize = 60f
        pincel.textAlign = Paint.Align.CENTER
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
        val alpha: Float = 0.8f

        // Isolate the force of gravity with the low-pass filter.
        gx1 = alpha * gx1 + (1 - alpha) * event!!.values[0]
        gy1 = alpha * gy1 + (1 - alpha) * event!!.values[1]

        // Remove the gravity contribution with the high-pass filter.
        lx1 += event.values[0] - gx1
        ly1 += event.values[1] - gy1

        vx1 = event?.values?.get(0)!!
        vy1 = event?.values?.get(1)!!
        actualizar()
    }

    private fun actualizar() {
        x1 += direccionX*lx1
        y1 += direccionY*ly1

        //COLISIONES
        if(x1>=(width-size)){
            direccionX *= -1f
        }else if(x1<=0.toFloat()){
            direccionX *= -1f
        }
        /*else if(x1>(width*0.4) && x1<width-(width*0.4)){
            direccionX = -1f
        }*/

        if(y1>=(height-size)){
            direccionY *= -1f
            valido1 = false
            valido2 = false
        }else if(y1<=0.toFloat()){
            direccionY *= -1f ;
            valido1 = false
            valido2 = false
        }
        /*else if(y1>(height*0.4) && y1<height-(height*0.4)){
            direccionY = 1f
        }*/

        if(y1>=size*3){
            valido1 = true
        }
        if( y1<=(height-(height*0.08))){
            valido2 = true
        }
        //PORTERIA ARRIBA
        if(x1>=(width/2)-100.toFloat() && x1<=(width/2)+50.toFloat() && y1>size+50 && y1<=size+80 && valido1){
            //Toast.makeText(context,"GOL EQUIPO 1!",Toast.LENGTH_SHORT).show()
            contEq1++
            x1 = (width/2).toFloat()
            y1 = (height/2-(height*0.08)).toFloat()
        }
        //PORTERIA ABAJO
        if(x1>=(width/2)-100.toFloat() && x1<=(width/2)+90.toFloat() && y1>=(height-(height*0.08)) && y1<height-(height*0.07) && valido2){
            //Toast.makeText(context,"GOL EQUIPO 2!",Toast.LENGTH_SHORT).show()
            contEq2++
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
        canvas!!.drawText("Equipo 1: "+contEq1+"   -   Equipo 2: "+contEq2,width/2f, 55f, pincel)
        canvas!!.drawText("1",width/2f, height/6f, pincel)
        canvas!!.drawText("2",width/2f, height - (height/6f - 59f), pincel)

        painter.setColor(Color.WHITE)
    }
}