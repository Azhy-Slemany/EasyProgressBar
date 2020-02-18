package com.example.easyprogressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button startAnimations;
    EasyProgressBar pb, pb1, pb2, pb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startAnimations = findViewById(R.id.button);
        pb = findViewById(R.id.progressBar);
        pb1 = findViewById(R.id.progressBar1);
        pb2 = findViewById(R.id.progressBar2);
        pb3 = findViewById(R.id.progressBar3);

        startAnimations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setValue(0);
                pb1.setValue(0);
                pb2.setValue(0);
                pb3.setValue(0);

                pb.addValueAnimated(100, 2, 120, new Runnable() {
                    @Override
                    public void run() {
                        pb1.addValueAnimated(100, 1, 60, new Runnable() {
                            @Override
                            public void run() {
                                pb2.addValueAnimated(100, 0.5, 60, new Runnable() {
                                    @Override
                                    public void run() {
                                        pb3.addValueAnimated(100, 0.25, 60, new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(MainActivity.this, "Game Initialization Completed", Toast.LENGTH_SHORT).show();
                                                startAnimations.setText("Start Again");
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
    }
}
