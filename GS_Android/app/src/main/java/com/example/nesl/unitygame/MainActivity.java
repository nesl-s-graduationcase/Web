package com.example.nesl.unitygame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn1).setOnClickListener(new OnClickEvent());
        findViewById(R.id.btn2).setOnClickListener(new OnClickEvent());
        findViewById(R.id.btn3).setOnClickListener(new OnClickEvent());
        findViewById(R.id.btn4).setOnClickListener(new OnClickEvent());
        findViewById(R.id.btn5).setOnClickListener(new OnClickEvent());
        findViewById(R.id.btn6).setOnClickListener(new OnClickEvent());
        final TextView txtName=findViewById(R.id.editName);

        findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=txtName.getText().toString();
                SharedPreferences pref=getSharedPreferences("user",MODE_PRIVATE);
                pref.edit().putString("name",name).commit();
                Toast.makeText(MainActivity.this,"save name success",Toast.LENGTH_LONG).show();
            }
        });
    }
    public class OnClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn1:startActivity(new Intent(MainActivity.this,OnemanActivity.class));break;
                case R.id.btn2:startActivity(new Intent(MainActivity.this,ManymanActivity.class));break;
                case R.id.btn3:startActivity(new Intent(MainActivity.this,VoteActivity.class));break;
                case R.id.btn4:startActivity(new Intent(MainActivity.this,OnemanActivity.class));break;
                case R.id.btn5:startActivity(new Intent(MainActivity.this,OnemanActivity.class));break;
                case R.id.btn6:startActivity(new Intent(MainActivity.this,OnemanActivity.class));break;
            }
        }

    }
}