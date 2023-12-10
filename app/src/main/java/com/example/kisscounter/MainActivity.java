package com.example.kisscounter;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 1;
    private Uri uri;
    private ActivityResultLauncher<Intent> openDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openDocumentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            uri = data.getData();
                        }
                    }
                }
        );
    }

    public void openFilePicker(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        openDocumentLauncher.launch(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                uri = resultData.getData();
                readTextFromUri(uri);
            }
        }
    }

    private void readTextFromUri(Uri uri) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            TextView textView = findViewById(R.id.textContent);
            textView.setText(stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSearchButtonClick(View view) {
        EditText editText = findViewById(R.id.searchWord);
        String searchWord = editText.getText().toString();

        if (uri != null && !searchWord.isEmpty()) {
            countOccurrences(uri, searchWord);
        } else {
            Toast.makeText(this, "Please select a file and enter a search word", Toast.LENGTH_SHORT).show();
        }
    }

    private void countOccurrences(Uri uri, String searchWord) {
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                count += countMatches(line, searchWord);
            }
            TextView resultTextView = findViewById(R.id.resultText);
            String result = getString(R.string.occurrences_count, count);
            resultTextView.setText(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int countMatches(String text, String word) {
        int count = 0;
        int index = 0;

        while ((index = text.indexOf(word, index)) != -1) {
            count++;
            index += word.length();
        }

        return count;
    }
}