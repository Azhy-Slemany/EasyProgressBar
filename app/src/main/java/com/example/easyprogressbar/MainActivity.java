package com.example.easyprogressbar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

        EasyProgressBar progressBar = new EasyProgressBar(this, Color.WHITE, Color.GREEN, EasyProgressBar.LEFT_TO_RIGHT);
        progressBar.setBorderColor(Color.WHITE);
        RelativeLayout parent = findViewById(R.id.parent);
        parent.addView(progressBar);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.height = 10;
        layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        progressBar.setLayoutParams(layoutParams);

        progressBar.addValueAnimated(100, 3, 100);
    }
}
