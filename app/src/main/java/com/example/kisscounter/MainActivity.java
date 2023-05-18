package com.example.kisscounter;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void ReadTextFile(View view) throws IOException {
        String string = "";
        StringBuilder stringBuilder = new StringBuilder();
        File sdcard = Environment.getExternalStoragePublicDirectory("Downloads");
        File file = new File(sdcard,"J.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        while (true) {
            try {
                if ((string = reader.readLine()) == null) break;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            stringBuilder.append(string).append(" ");
            textView.setText(stringBuilder);
        }
        reader.close();
        Toast.makeText(getBaseContext(), stringBuilder.toString(), Toast.LENGTH_LONG).show();
    }
}