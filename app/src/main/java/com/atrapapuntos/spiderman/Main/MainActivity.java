package com.atrapapuntos.spiderman.Main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atrapapuntos.spiderman.OnBoarding.OnBoardingActivity;
import com.atrapapuntos.spiderman.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        validarLogin();
        cargarElementos();
    }

    private void cargarElementos()
    {
        btnLogout=(Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnLogout:
                FirebaseAuth.getInstance().signOut();
                break;
        }
    }

    private void validarLogin()
    {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                {
                    TextView txtTexto=(TextView)findViewById(R.id.txtTexto);
                    txtTexto.setText("Verifico : "+user.isEmailVerified());
                    //uid=user.getUid();
                    //txtNombre=(TextView)findViewById(R.id.txtNombre);
                    if(user.getDisplayName()==null)//si no tiene nombre, solo email
                    {
                        //int a=user.getEmail().indexOf("@");
                        //String nombre=user.getEmail().substring(0,a);
                        //txtNombre.setText(nombre);
                    }
                    else//si viene con nombre
                    {
                        //int a=user.getDisplayName().indexOf(" ");
                        //String nombre=user.getDisplayName().substring(0,a);
                        //txtNombre.setText(nombre);
                    }
                    //cargarFotoPerfil(""+user.getPhotoUrl());
                    // User is signed in
                    System.out.println("onAuthStateChanged:signed_in:" + user.getUid());
                }
                else
                {
                    // User is signed out
                    System.out.println("onAuthStateChanged:signed_out");
                    //Voy a onBoarding tutorial y login
                    Intent i=new Intent(getApplicationContext(),OnBoardingActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //OnBoarding Shared Preferences (porsiacaso)
     //   // Get the shared preferences
     //   SharedPreferences preferences =  getSharedPreferences("my_preferences", MODE_PRIVATE);
     //   // Check if onboarding_complete is false
     //   if(!preferences.getBoolean("onboarding_complete",false)) {
     //   // Start the onboarding Activity
     //   Intent onboarding = new Intent(this, OnboardingActivity.class);
     //   startActivity(onboarding);
     //   // Close the main Activity
     //   finish();
     //   return;
}
