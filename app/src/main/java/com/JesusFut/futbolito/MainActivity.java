package com.JesusFut.futbolito;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private final float[] rotationSensorReading = new float[3];
    ImageView ballon;
    ImageView porteria1;
    ImageView porteria2;
    TextView goles1;
    TextView goles2;
    ConstraintLayout cancha;
    int width;
    int height;
    private SensorManager sensorManager;
    private Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cancha = findViewById(R.id.cancha);
        ballon = findViewById(R.id.ballon);
        porteria1 = findViewById(R.id.porteria1);
        porteria2 = findViewById(R.id.porteria2);
        goles1 = findViewById(R.id.txtGoles1);
        goles2 = findViewById(R.id.txtGoles2);


        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor acelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acelerometer != null) {
            sensorManager.registerListener(this, acelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, rotationSensorReading,
                    0, rotationSensorReading.length);
        }
        Log.d("MySensor", rotationSensorReading[1] + "");
        width = cancha.getWidth(); // ancho absoluto en pixels
        height = cancha.getHeight(); // alto absoluto en pixels
        if (ballon.getX() < 0) {
            ballon.setX(0);
        } else if ((ballon.getX() + ballon.getHeight()) > width && width != 0) {
            ballon.setX(width - ballon.getHeight());
        } else {
            ballon.setX(ballon.getX() - (rotationSensorReading[0] * 10));
        }
        Log.d("Position", (ballon.getY() + ballon.getWidth()) + "");
        if (ballon.getY() < 0) {
            ballon.setY(0);
        } else if ((ballon.getY() + ballon.getWidth()) > height && height != 0) {
            ballon.setY(height - ballon.getWidth());
        } else {
            ballon.setY(ballon.getY() + (rotationSensorReading[1] * 10));
        }

        if (ballon.getX() + ballon.getWidth() - 15 >= porteria1.getX() && ballon.getX() - 15 <= porteria1.getX() + porteria1.getWidth()) {
            if (ballon.getY() + 15 >= porteria1.getY() && ballon.getY() + 15 <= porteria1.getY() + porteria1.getHeight()) {
                goles2.setText(((Integer.parseInt(goles2.getText().toString())) + 1) + "");
                ballon.setX(width / 2);
                ballon.setY(height / 2);
                Log.d("Position", "Gol en la porteria 1");
            }
        }
        if (ballon.getX() + ballon.getWidth() - 15 >= porteria2.getX() && ballon.getX() - 15 <= porteria2.getX() + porteria2.getWidth()) {
            if (ballon.getY() + ballon.getHeight() - 15 >= porteria2.getY() && ballon.getY() - 15 <= porteria2.getY() + porteria2.getHeight()) {
                goles1.setText(((Integer.parseInt(goles1.getText().toString())) + 1) + "");
                ballon.setX(width / 2);
                ballon.setY(height / 2);
                Log.d("Position", "Gol en la porteria 2");
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}