package net.alwaysdata.woyanoro.usagino4g;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Player5 on 05/06/2017
 */

public class CodeActivity extends Activity {
    private String messageStack = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_black);

        if (MainActivity.theme == 0) {
            setContentView(R.layout.activity_code_black);
        } else if(MainActivity.theme == 1){
            setContentView(R.layout.activity_code);
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            messageStack = extras.getString("code");
        }
        TextView code = (TextView) findViewById(R.id.code2);
        code.setMovementMethod(new ScrollingMovementMethod());
        code.setText(messageStack);
        Button get_browser = (Button) findViewById(R.id.get_browser);
        get_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
