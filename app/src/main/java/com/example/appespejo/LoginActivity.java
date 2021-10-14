package com.example.appespejo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

//    ----------------------------Definiciones de variables------------------------------
//    -----------------------------------------------------------------------------------
    Context context;
    public GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private final static int RC_SIGH_IN_GOOGLE = 2;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    FirebaseUser usuarioo;

//    ------------------------------Al empezar actividad---------------------------------
//    -----------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        usuarioo = FirebaseAuth.getInstance().getCurrentUser();

//        --------------Si usuario ya esta logeado te envia directamente a Home--------------
        if(usuarioo!=null && usuarioo.isEmailVerified())
        {
            startActivity(new Intent(this, HomeActivity.class));
            Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            Log.d("Demo" , mAuth.getCurrentUser().getEmail());
        }

//        ----------Else nos deja entrar a login activity y y ejecutar lo necesario----------
        else
            {
            Log.d("Demo" , "No esta entrado ni un usuario");
//            Toast.makeText(LoginActivity.this, mAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
            setupbd();
//          facebook();
            context = this;
            createRequest();
            }
        }

//    -------------------------funciones de facebook-------------------------------------
//    -----------------------------------------------------------------------------------

        private void facebook(){
            callbackManager = CallbackManager.Factory.create();

            LoginButton loginButton = findViewById(R.id.login_button);
            loginButton.setPermissions(Arrays.asList("name,email,user_gender"));

            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    Log.d("Demo","Login successfull");
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }

                @Override
                public void onCancel() {
                    Log.d("Demo","Login onCancel");
                }

                @Override
                public void onError(@NonNull FacebookException e) {
                    Log.d("Demo","Login onError");
                }
            });
        }


//    AccessToken accessToken = AccessToken.getCurrentAccessToken();
//    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null){
                    LoginManager.getInstance().logOut();
                }
            }
        };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.startTracking();
    }

//    -----------------------------------------------------------------------------------
//    -----------------------------------------------------------------------------------



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
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

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

//        ---------------------------------------------
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject object, @Nullable GraphResponse graphResponse) {
                Log.d("Demo", object.toString());
            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("fields","id, name, email, gender");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    // GetContent creates an ActivityResultLauncher<String> to allow you to pass
    // in the mime type you'd like to allow the user to select
    /**   ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
          new ActivityResultCallback<Uri>() {

                @Override
                public void onActivityResult(int requestCode, int resultCode, Intent data) {

//     -------------------------------------------------------------------------------------------
//     --------------------------------------facebook---------------------------------------------
//     -------------------------------------------------------------------------------------------

                    callbackManager.onActivityResult(requestCode, resultCode, data);

                    GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(@Nullable JSONObject object, @Nullable GraphResponse graphResponse) {
                            Log.d("Demo", object.toString());
                        }
                    });

                    Bundle bundle = new Bundle();
                    bundle.putString("fields","id, name, email, gender");
                    graphRequest.setParameters(bundle);
                    graphRequest.executeAsync();

//      -------------------------------------------------------------------------------------------
//      -------------------------------------------------------------------------------------------


//      -------------------------------------------------------------------------------------------
//      ----------------------------------------google---------------------------------------------
//      -------------------------------------------------------------------------------------------

                    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                    if (requestCode == RC_SIGN_IN) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        try {
                            // Google Sign In was successful, authenticate with Firebase
                            GoogleSignInAccount account = task.getResult(ApiException.class);
//                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                            firebaseAuthWithGoogle(account.getIdToken());
                        } catch (ApiException e) {
                            // Google Sign In failed, update UI appropriately
//                            Log.w(TAG, "Google sign in failed", e);
                        }
                    }
                }
            });

//      -------------------------------------------------------------------------------------------
//      -------------------------------------------------------------------------------------------




//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//
//        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(@Nullable JSONObject object, @Nullable GraphResponse graphResponse) {
//                Log.d("Demo", object.toString());
//            }
//        });
//
//        Bundle bundle = new Bundle();
//        bundle.putString("fields","id, name, email, gender");
//        graphRequest.setParameters(bundle);
//        graphRequest.executeAsync();
//    }

          **/

        private void signInGoogle() {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGH_IN_GOOGLE);
        }

//    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    int requestCode, resultCode;
//                    Intent data = new Intent();
//                    if (result.getResultCode() == 123) {
//                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//                        try {
//                            // Google Sign In was successful, authenticate with Firebase
//                            GoogleSignInAccount account = task.getResult(ApiException.class);
////                                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
//                            firebaseAuthWithGoogle(account.getIdToken());
//                        } catch (ApiException e) {
//                            // Google Sign In failed, update UI appropriately
////                                Log.w(TAG, "Google sign in failed", e);
//                        }
//
//                    } else if (result.getResultCode() == 321) {
//                        // ToDo : Do your stuff...
//                    }
//                }
//            });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
//                            Toast.makeText(LoginActivity.this, "signInWithCredential:success", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Demo", mAuth.getCurrentUser().getEmail() + " Ha entrado");
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Toast.makeText(LoginActivity.this, "signInWithCredential:failure", Toast.LENGTH_SHORT).show();
                            Log.w("Demo", "signInWithCredential:failure", task.getException());
//                            updateUI(null);
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
        ImageButton facebook = this.findViewById(R.id.facebook);
        TextView recuperar = this.findViewById(R.id.Recuperar);
        TextInputEditText textLogin = this.findViewById(R.id.login);
        TextInputEditText textPassword = this.findViewById(R.id.textInputEditText);
        ImageView logo = findViewById(R.id.imageView2);
        mAuth = FirebaseAuth.getInstance();
        usuarioo = FirebaseAuth.getInstance().getCurrentUser();

        signUpButton.setOnClickListener(new View.OnClickListener(){ //para logear

            @Override
            public void onClick(View v) {
                String inputName = textLogin.getText().toString();
                String inputPassword = textPassword.getText().toString();

                if(!inputName.isEmpty() || !inputPassword.isEmpty()){
                        mAuth.signInWithEmailAndPassword(inputName,inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()){
                                        if( usuarioo!=null ){
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Log.d("Demo","Necesitas verificar tu email");
//                                            Toast.makeText(LoginActivity.this, "Necesitas verificar tu email", Toast.LENGTH_SHORT).show();
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
//                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
//                startActivity(intent);
            }
        });

        recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecuperarContr.class);
                startActivity(intent);
            }
        });

    }
}