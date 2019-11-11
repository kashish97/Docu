package com.appsnipp.modernlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CertiActivity extends AppCompatActivity   {
    Button b1;
    Button b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b1 = (Button) findViewById(R.id.ed1);
        b2 = (Button) findViewById(R.id.editText);

        setContentView(R.layout.activity_certi);
        fileopen1();
    }

    public void fileopen1(){
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                // Update with mime types
                intent.setType("*/*");

                // Update with additional mime types here using a String[].
                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

                // Only pick openable and local files. Theoretically we could pull files from google drive
                // or other applications that have networked files, but that's unnecessary for this example.
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                // REQUEST_CODE = <some-integer>
                startActivityForResult(intent, 169);
            }


        });
    }
}
