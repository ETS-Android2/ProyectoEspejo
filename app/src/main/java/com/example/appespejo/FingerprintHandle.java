package com.example.appespejo;


import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.Login;

public class FingerprintHandle extends FingerprintManager.AuthenticationCallback{

      /*  private Context context;

        public FingerprintHandle(Context context){
            this.context = context;
        }

        public void startAuth(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
            CancellationSignal cancellationSignal = new CancellationSignal();
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
        }

        public void onAuthenticationError(int errorCode,CharSequence errString){
            this.update("Error de autentificación" + errString,false);
        }

        public void onAuthenticationFailed(){
            this.update("Fallo autentificando",false);
        }

    public void onAuthenticationHelp(int helpcode, CharSequence helpString){
        this.update("Fallo " + helpString,false);
    }

    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result){
        this.update("Puedes acceder",true);
    }

        private void update(String s, boolean b){
            TextView paraLabel = (TextView) ((HomeActivity)context).findViewById(R.id.paraLabel);

            paraLabel.setText(s);
        }
*/
}

