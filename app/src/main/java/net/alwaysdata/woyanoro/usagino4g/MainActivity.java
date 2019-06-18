package net.alwaysdata.woyanoro.usagino4g;

    import android.app.Activity;
    import android.content.SharedPreferences;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.preference.PreferenceManager;
    import android.telephony.SmsManager;
    import android.widget.CheckBox;
    import android.widget.EditText;
    import android.widget.TextView;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.content.Intent;
    import android.view.View;
    import android.widget.Button;
    import android.widget.Toast;

public class MainActivity extends Activity {

    private AutoCompleteTextView editURL = null;
    private Button browse = null;
    private Button parametres = null;
    public static CheckBox simplifyBox = null;
    public static CheckBox zeroBox = null;
    public static EditText num_tel = null;
    public static Intent i=null;
    public static boolean themechange = false;

    public static int mode = 1; // 0 = nothing; 1 = client; 2 = server ; 3 = both?
    //theme blanc/noir
    public static int theme = 1; // 0 = noir ; 1 = blanc

    //

    // Notre liste de mots que connaîtra l'AutoCompleteTextView
    private static final String[] URLs = new String[]{
            "http://www.google.com", "http://mail.google.com", "http://www.pso-world.com/forums/", "http://puyonexus.com/forum/", "www.google.com", "mail.google.com", "www.pso-world.com/forums/", "puyonexus.com/forum/", "woyanoro.alwaysdata.net"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (MainActivity.theme == 0) {
            setContentView(R.layout.activity_main_black);
        } else if (MainActivity.theme == 1) {
            setContentView(R.layout.activity_main);
        }
        browse = (Button) findViewById(R.id.button_browse);
        parametres = (Button) findViewById(R.id.button_parametres);
        simplifyBox = (CheckBox) findViewById(R.id.simplifybox);
        zeroBox = (CheckBox) findViewById(R.id.zerobox);
        num_tel = (EditText) findViewById(R.id.editPhone);
        //pref num tel autoset

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        num_tel.setText(prefs.getString("TelNumber", ""));

        //


    }
    @Override
    protected void onResume(){
        super.onResume();
        browse = (Button) findViewById(R.id.button_browse);
        parametres = (Button) findViewById(R.id.button_parametres);

        i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        //
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("CodeSimplify",true)){
            simplifyBox.setChecked(false);
        }

        simplifyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simplifyBox.isChecked()){
                    zeroBox.setChecked(false);
                }
            }
        });
        zeroBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zeroBox.isChecked()){
                    simplifyBox.setChecked(false);
                }
            }
        });
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode==1){
                    TextView numerotel = (TextView) findViewById(R.id.editPhone);
                    String numtel = numerotel.getText().toString();
                    if (numtel.length()==10){
                        System.out.print("OK");
                        TextView adresse = (TextView) findViewById(R.id.editURL);
                        String lien = adresse.getText().toString();
                        if (simplifyBox.isChecked()){
                            lien = "#"+lien;
                        }
                        else if (zeroBox.isChecked()){
                            lien = "0"+lien;
                        }
                        else {
                            lien = "-"+lien;
                        }
                        SmsManager manager = SmsManager.getDefault();
                        manager .sendTextMessage(numtel, null, lien, null, null);
                        manager .sendTextMessage(numtel, null, "end", null, null);
                        Toast.makeText(MainActivity.this, "Requête envoyée à "+numtel, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Le numero "+numtel+" n'a pas 10 chiffres.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MainActivity.this, "Le mode n'est pas réglé sur client.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        parametres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ActiviteParametres = new Intent(MainActivity.this, Parametres.class);
                startActivity(ActiviteParametres);
            }
        });

        // On récupère l'AutoCompleteTextView déclaré dans notre layout
        editURL = (AutoCompleteTextView) findViewById(R.id.editURL);
        editURL.setThreshold(0);

        // On associe un adaptateur à notre liste…
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, URLs);
        // … puis on indique que notre AutoCompleteTextView utilise cet adaptateur
        editURL.setAdapter(adapter);
    }





    @Override
    protected void onRestart(){
        super.onRestart();
        if (themechange ==true) {
            themechange = false;
            if (MainActivity.theme == 0) {
                setContentView(R.layout.activity_main_black);
            } else if (MainActivity.theme == 1) {
                setContentView(R.layout.activity_main);
            }
        }

    }


}