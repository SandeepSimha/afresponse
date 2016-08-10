package com.company.sandeep.afpromotion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import pojo.Promotions;

/**
 * Created by chsan_000 on 8/3/2016.
 */
public class FirstActivity extends AppCompatActivity {
    TextView txtTitle, txtDescription;
    ImageView descriptionImage;
    Button button;
    Promotions p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);

        getSupportActionBar().setTitle("promotion card");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = (TextView) findViewById(R.id.title);
        txtDescription = (TextView) findViewById(R.id.description);
        descriptionImage = (ImageView) findViewById(R.id.description_imageView);
        button = (Button) findViewById(R.id.my_button);

        p = (Promotions) getIntent().getSerializableExtra("data");

        txtTitle.setText(p.getTitle());
        txtDescription.setText(p.getDescription());
        Glide.with(FirstActivity.this).load(p.getImage())
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(descriptionImage);

        button.setText(p.getButton().get(0).getTitle());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FirstActivity.this, WebviewActivity.class);
                intent.putExtra("webview", p.getButton().get(0).getTarget());
                startActivity(intent);
            }
        });
    }
}
