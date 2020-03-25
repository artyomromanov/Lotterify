package com.example.lotterify.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lotterify.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivityJava extends AppCompatActivity {
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.btnParseHTML);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHtmlFromWeb();
            }
        });
    }
    private void getHtmlFromWeb() {

        new Thread(new Runnable() {

            @Override
            public void run() {
                final StringBuilder stringBuilder = new StringBuilder();

                try {
                    Document doc = Jsoup.connect("https://www.national-lottery.co.uk/results/euromillions/draw-history").get();
                    String title = doc.title();
                    Elements links = doc.select("span[class=\"table_cell_block\"]");
                    stringBuilder.append(title).append("\n");
                    for (Element link : links) {
                        stringBuilder.append("\n").append("Link : ").append(link.attr("href")).append("\n").append("Text : ").append(link.text());
                    }
                } catch (IOException e) {
                    stringBuilder.append("Error : ").append(e.getMessage()).append("\n");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(stringBuilder.toString());
                    }
                });
            }
        }).start();
    }

}
