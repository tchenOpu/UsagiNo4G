package net.alwaysdata.woyanoro.usagino4g;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BrowserActivity extends Activity{

    private String messageStack = "";
//    private String telephoneNum = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
/**
        if (MainActivity.theme == 0) {
            setContentView(R.layout.activity_browser_black);
        } else if(MainActivity.theme == 1){
            setContentView(R.layout.activity_browser);
        }
        */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            messageStack = extras.getString("msg");
//            telephoneNum = extras.getString("num");
        }
        Spanned htmlpage = Html.fromHtml(messageStack);
        TextView code = (TextView) findViewById(R.id.code);
        code.setMovementMethod(new ScrollingMovementMethod());
        code.setText(htmlpage);
        Button get_code = (Button) findViewById(R.id.get_code);
        get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent codeIntent = new Intent(BrowserActivity.this, CodeActivity.class);
                codeIntent.putExtra("code", messageStack);
                codeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(codeIntent);
            }
        });
    }
}
