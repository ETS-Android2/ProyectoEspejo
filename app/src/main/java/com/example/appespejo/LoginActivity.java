package com.example.appespejo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.firebase.ui.auth.AuthUI;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Arrays;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

//    ----------------------------Definiciones de variables------------------------------
//    -----------------------------------------------------------------------------------

    Context context;
    GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private final static int RC_SIGH_IN_GOOGLE = 2;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CallbackManager callbackManager;
    LoginButton loginButton;
    FirebaseUser usuarioo;
    CallbackManager mCallbackManager;
    private TextView mParaLabel;
//    private AccessTokenTracker accessTokenTracker;
    private SharedPreferences preferences;

//    ------------------------------Al empezar actividad---------------------------------
//    -----------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        usuarioo = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();
        mCallbackManager = CallbackManager.Factory.create();
        preferences = getSharedPreferences("Preferences", MODE_PRIVATE);
        FacebookSdk.sdkInitialize(LoginActivity.this);
//        --------------Si usuario ya esta logeado te envia directamente a Home--------------
        if(usuarioo!=null && usuarioo.isEmailVerified())
        {
            startActivity(new Intent(this, HomeActivity.class));
            Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            Log.d("Demo" , mAuth.getCurrentUser().getEmail());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Usuario UID", mAuth.getCurrentUser().getUid());
            editor.commit();
        }

//        ----------Else nos deja entrar a login activity y y ejecutar lo necesario----------
        else {
            Log.d("Demo" , "No esta entrado ni un usuario");
            setupbd();
            context = this;
            createRequest();
            }
        }

//    -------------------------funciones de facebook-------------------------------------
//    -----------------------------------------------------------------------------------

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("Demo", "handleFacebookAccessToken: " + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Demo", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                            if(mAuth.getUid() == null){
                                String[] prueba = mAuth.getCurrentUser().getDisplayName().split(" ");
                                Log.d("Demo", "Nombre " + prueba[0]);
                                Log.d("Demo", "Apellidos " + prueba[1]);


                                Map<String, Object> userFacebook = new HashMap<>();
                                userFacebook.put("Apellido", prueba[1]);
                                userFacebook.put("Email", mAuth.getCurrentUser().getEmail().toString());
                                userFacebook.put("Nombre", prueba[0]);
                                userFacebook.put("photoUrl", mAuth.getCurrentUser().getPhotoUrl().toString());

                                db.collection("Users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .set(userFacebook);

                            }

                            Log.d("Demo", "Email " + mAuth.getCurrentUser().getEmail());
                            Log.d("Demo", "Name " + mAuth.getCurrentUser().getDisplayName());
                            Log.d("Demo", "UID " + mAuth.getCurrentUser().getUid());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Demo", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
//    --------------------------funciones de Google--------------------------------------
//    -----------------------------------------------------------------------------------

    private void createRequest(){

// Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager = CallbackManager.Factory.create();
        callbackManager.onActivityResult(requestCode, resultCode, data);


        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGH_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("Demo", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Demo", "Google sign in failed", e);
            }
        }

    }

    // GetContent creates an ActivityResultLauncher<String> to allow you to pass
    // in the mime type you'd like to allow the user to select

    private void signInGoogle() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGH_IN_GOOGLE);
        }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if(mAuth.getUid() == null){
                                String[] prueba = mAuth.getCurrentUser().getDisplayName().split(" ");
                                Log.d("Demo", "Nombre " + prueba[0]);
                                Log.d("Demo", "Apellidos " + prueba[1]);


                                Map<String, Object> userGoogle = new HashMap<>();
                                userGoogle.put("Apellido", prueba[1]);
                                userGoogle.put("Email", mAuth.getCurrentUser().getEmail());
                                userGoogle.put("Nombre", prueba[0]);
                                userGoogle.put("photoUrl", mAuth.getCurrentUser().getPhotoUrl().toString());

                                db.collection("Users")
                                        .document(mAuth.getCurrentUser().getUid())
                                        .set(userGoogle);

                            }

                            Log.d("Demo", mAuth.getCurrentUser().getEmail() + " Ha entrado");
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Demo", "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

//    -----------------------------------------------------------------------------------
//    -----------------------------------------------------------------------------------

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

//    ------------------Funcion main donde definimos todos onClick-----------------------
//    -----------------------------------------------------------------------------------

    private void setupbd() {

        Button signUpButton = this.findViewById(R.id.signUpButton);
        TextView register = this.findViewById(R.id.register);
        ImageButton google = this.findViewById(R.id.googleLogin);
        LoginButton facebookLB = this.findViewById(R.id.login_button);
        TextView anonimo = this.findViewById(R.id.anonimo);
        TextView recuperar = this.findViewById(R.id.Recuperar);
        TextInputEditText textLogin = this.findViewById(R.id.login);
        TextInputEditText textPassword = this.findViewById(R.id.password);
        ImageView logo = this.findViewById(R.id.fotoUsuario);
        mAuth = FirebaseAuth.getInstance();
        usuarioo = FirebaseAuth.getInstance().getCurrentUser();


        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        facebookLB.setReadPermissions("email", "public_profile");
        facebookLB.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Demo", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("Demo", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Demo", "facebook:onError", error);
            }
        });

        textPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    Toast.makeText(LoginActivity.this, "Enter", Toast.LENGTH_SHORT).show();

                    String inputName = textLogin.getText().toString();
                    String inputPassword = Objects.requireNonNull(textPassword.getText()).toString();

                    if(!inputName.isEmpty() || !inputPassword.isEmpty()){
                        mAuth.signInWithEmailAndPassword(inputName,inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    if(!mAuth.getCurrentUser().isEmailVerified()){
                                        Toast.makeText(getApplicationContext(), "Verifica tu correo electronico", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                }else{
                                    Toast.makeText(LoginActivity.this, "Incorrecto usuario o/y contrasena", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    if(inputName.isEmpty() || inputPassword.isEmpty()){
                        Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                    }
                    if(!isEmailValid(inputName)){
                        Toast.makeText(LoginActivity.this, "Incierta tu email por favor", Toast.LENGTH_SHORT).show();
                    }

                    return true;

                }
                return false;
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener(){ //para logear

            @Override
            public void onClick(View v) {
                String inputName = textLogin.getText().toString();
                String inputPassword = Objects.requireNonNull(textPassword.getText()).toString();

                if(!inputName.isEmpty() || !inputPassword.isEmpty()){
                        mAuth.signInWithEmailAndPassword(inputName,inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){

                                        if(!mAuth.getCurrentUser().isEmailVerified()){
                                            Toast.makeText(getApplicationContext(), "Verifica tu correo electronico", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }else{
                                        Toast.makeText(LoginActivity.this, "Incorrecto usuario o/y contrasena", Toast.LENGTH_SHORT).show();
                                    }
                            }
                        });
                }
                if(inputName.isEmpty() || inputPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                }
                if(!isEmailValid(inputName)){
                    Toast.makeText(LoginActivity.this, "Incierta tu email por favor", Toast.LENGTH_SHORT).show();
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }

        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecuperarContr.class);
                startActivity(intent);
            }
        });

        facebookLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Initialize Facebook Login button
                mCallbackManager = CallbackManager.Factory.create();
                facebookLB.setReadPermissions("email", "public_profile");
                facebookLB.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Demo", "facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Demo", "facebook:onCancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Demo", "facebook:onError", error);
                    }
                });
            }
        });

        anonimo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInAnonymously()
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Demo", "signInAnonymously:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Demo", "signInAnonymously:failure", task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
    }



    private void updateUI(FirebaseUser user) {
        if(user!=null){
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }else{
            Toast.makeText(LoginActivity.this, "Sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }

}