package android.example.musicshop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        setTitle("About Shop");

        TextView inform = findViewById(R.id.infotextview);

        Intent intent = getIntent();
        if(intent != null){
            inform.setText(intent.getStringExtra("info"));
        }
    }
}
