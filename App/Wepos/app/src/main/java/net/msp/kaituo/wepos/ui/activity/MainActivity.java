package net.msp.kaituo.wepos.ui.activity;

import android.app.Application;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.msp.kaituo.wepos.R;

public class MainActivity extends AppCompatActivity {

    private Button Click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Click = (Button) findViewById(R.id.Click);
        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                Toast.makeText(MainActivity.this,"!!",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
