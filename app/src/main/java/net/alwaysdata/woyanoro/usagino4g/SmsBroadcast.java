package net.alwaysdata.woyanoro.usagino4g;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class SmsBroadcast extends BroadcastReceiver {

    public static String messageStack = "";
    public String phoneNumberStack = "";
    public static String temp = "";
    public String msg_manquants = "";
    String chaine = "";
    private static boolean simplify = false;
    private static boolean zeroify = false;

    public void onReceive(Context context, Intent intent) {
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    final String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();

                    if (MainActivity.mode==1){
                        if (message.equals("end")){
                            temp = messageStack;
                            messageStack = "";
                            String message_tri = "";
                            String nb_messages = temp.substring(5,10);
                            int int_messagees = Integer.valueOf(nb_messages);
                            int compteur_sms = 1;
                            int curseur_texte = 0;
                            while (curseur_texte < temp.length() && compteur_sms <= int_messagees){
                                String strActu = temp.substring(curseur_texte, curseur_texte+160);
                                if (strActu.substring(0,5).equals(String.format("%05d", compteur_sms))){
                                    message_tri = message_tri+strActu.substring(10);
                                    curseur_texte = curseur_texte+160;
                                    compteur_sms = compteur_sms+1;
                                }
                                else {
                                    int curseur_secondaire = curseur_texte+160;
                                    int secondaire = 1;
                                    while (curseur_secondaire<temp.length() && secondaire == 1) {
                                        String strSecondaire = temp.substring(curseur_secondaire, curseur_secondaire + 160);
                                        if (strSecondaire.substring(0, 5).equals(String.format("%05d", compteur_sms))) {
                                            message_tri = message_tri + strSecondaire.substring(10);
                                            compteur_sms = compteur_sms + 1;
                                            secondaire = 0;
                                        } else {
                                            curseur_secondaire = curseur_secondaire + 160;
                                        }
                                    }
                                    if (secondaire==1) {
                                        msg_manquants = msg_manquants + String.format("%05d", compteur_sms);
                                        message_tri = message_tri + repeat("-", 160);
                                        compteur_sms = compteur_sms + 1;
                                    }
                                }
                            }
                            System.out.println(curseur_texte+":"+compteur_sms+":"+msg_manquants);
                            Intent intentsms = new Intent(context.getApplicationContext(), BrowserActivity.class);
                            intentsms.putExtra("msg", message_tri);
                            intentsms.putExtra("num", phoneNumber);
                            intentsms.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intentsms);
                        }
                        else if (phoneNumberStack.equals("")) {
                            phoneNumberStack = phoneNumber;
                            messageStack = messageStack + message;
                            Toast.makeText(context,"UsagiClient:"+(messageStack.length()/160), Toast.LENGTH_SHORT).show();
                        }
                        else if (phoneNumberStack.equals(phoneNumber)) {
                            messageStack = messageStack + message;
                            Toast.makeText(context,"UsagiClient:"+(messageStack.length()/160), Toast.LENGTH_SHORT).show();
                        }
                    }

                    else if (MainActivity.mode==2){
                        Toast.makeText(context,"Server:"+messageStack, Toast.LENGTH_LONG).show();
                        if(message.equals("end")){
                            temp = messageStack;
                            messageStack= "";
                            new Thread(new Runnable() {
                                public void run() {
                                    chaine = getCode(temp, phoneNumber);
                                }
                            }).start();
                        }
                        else if (phoneNumberStack.equals(phoneNumber)) {
                            messageStack = messageStack + message.substring(1);
                            simplify = (message.substring(0,1).equals("#"));
                            zeroify = (message.substring(0,1).equals("0"));
                        }
                        else {
                            phoneNumberStack = phoneNumber;
                            messageStack = message.substring(1);
                            simplify = (message.substring(0,1).equals("#"));
                            zeroify = (message.substring(0,1).equals("0"));
                        }
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {}
    }

    public static String getCode(String url, String phoneNumber){
        String code = "";
        if(urlExists(url)){
            BufferedReader in = null;
            try{
                URL site = new URL(url);
                in = new BufferedReader(new InputStreamReader(site.openStream())); //CA RESTE ICI
                String inputLine;
                while ((inputLine = in.readLine()) != null){
                    code = code + "\n" + (inputLine);
                }
                in.close();
            }
            catch (IOException ex) {
                System.out.println("Erreur dans l'ouverture de l'URL : " + ex);
            }
            finally{
                try {
                    in.close();
                }
                catch (IOException ex) {
                    System.out.println("Erreur dans la fermeture du buffer : " + ex);
                }
            }
        }
        else {
            System.out.println("Noob");
        }

        code = code.replaceAll("[^\\x00-\\x7F]", "");

        if (simplify) {
            int coupe1 = code.toLowerCase().indexOf("<head>");
            int coupe2 = code.toLowerCase().indexOf("</head>");
            if (coupe1>=0 && coupe2 >=0) {
                code = code.substring(0,coupe1)+code.substring(coupe2+7);
            }
        }

        if (zeroify) {
            int coupe1 = code.toLowerCase().indexOf("<head>");
            int coupe2 = code.toLowerCase().indexOf("</head>");
            if (coupe1>=0 && coupe2 >=0) {
                code = code.substring(0,coupe1)+code.substring(coupe2+7);
            }
            Spanned span = Html.fromHtml(code);
            code = span.toString();
            code = code.replaceAll("[^\\x00-\\x7F]", "");
        }

        int i = 0;
        int nb_sms = (code.length()/150)+1;
        while (i < code.length()-150){
            String subcode = code.substring(i, i+150);
            subcode = String.format("%05d", (i/150)+1)+String.format("%05d", nb_sms)+subcode;
            SmsManager manager = SmsManager.getDefault();
            manager .sendTextMessage(phoneNumber, null, subcode, null, null);
            System.out.println("code :"+subcode);
            i+=150;
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){}
        }
        String subcode = code.substring(i);
        subcode = String.format("%05d", (i/150)+1)+String.format("%05d", nb_sms)+subcode;
        while (subcode.length()<160) {
            subcode = subcode + "p";
        }
        SmsManager manager = SmsManager.getDefault();
        manager .sendTextMessage(phoneNumber, null, subcode, null, null);
        manager .sendTextMessage(phoneNumber, null, "end", null, null);
        System.out.println("code :"+subcode);
        return code;
    }

    public static boolean urlExists(String url) {
        try {
            URL site = new URL(url);
            try {
                site.openStream();
                return true;
            } catch (IOException ex) {
                return false;
            }
        } catch (MalformedURLException ex) {
            return false;
        }
    }

    public static String repeat(String val, int count){
        StringBuilder buf = new StringBuilder(val.length() * count);
        while (count-- > 0) {
            buf.append(val);
        }
        return buf.toString();
    }
}