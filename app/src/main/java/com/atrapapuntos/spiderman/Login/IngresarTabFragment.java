package com.atrapapuntos.spiderman.Login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atrapapuntos.spiderman.Main.MainActivity;
import com.atrapapuntos.spiderman.R;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class IngresarTabFragment extends Fragment implements View.OnClickListener {

    private TextView txtReset;
    private EditText edtPass;
    private AutoCompleteTextView edtEmail;
    private Button btnLogin;
    private SignInButton btnGoogle;
    private FirebaseAuth mAuth;
    private View v;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInOptions gso;
    private AlertDialog dialog;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    private OnFragmentInteractionListener mListener;

    public IngresarTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //FacebookSdk.sdkInitialize(getContext());
        mCallbackManager = CallbackManager.Factory.create();
        v=inflater.inflate(R.layout.ingresar_tab, container, false);
        cargarElementos();
        cargarGoogle();
        return v;
    }

    private void cargarGoogle()
    {
        // Configure Google Sign In
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }

    private void cargarElementos()
    {
        mAuth = FirebaseAuth.getInstance();
        edtEmail=(AutoCompleteTextView)v.findViewById(R.id.edtEmail);
        addAdapterToViews();
        edtPass=(EditText)v.findViewById(R.id.edtPass);
        btnLogin=(Button)v.findViewById(R.id.btnLogin);
        txtReset=(TextView)v.findViewById(R.id.txtReset);
        txtReset.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnGoogle = (SignInButton)v.findViewById(R.id.btnGoogle);
        btnGoogle.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnLogin:
                String email=edtEmail.getText().toString();
                String contraseña=edtPass.getText().toString();
                mAuth.signInWithEmailAndPassword(email,contraseña)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                System.out.println("signInWithEmail:onComplete:" + task.isSuccessful());
                                if (!task.isSuccessful())
                                {
                                    System.out.println("signInWithEmail:failed "+task.getException());
                                    Toast.makeText(getContext(),"Usuario no registrado :(",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    //Se dirije hacia mainActivity
                                    Toast.makeText(getContext(),"Ingreso correctamente :)",Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(getContext(),MainActivity.class);
                                    startActivity(i);
                                    getActivity().finish();
                                }
                            }
                        });
                break;
            case R.id.btnGoogle:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.txtReset:
                dialog=createLoginDialogo();
                dialog.show();
                break;
        }
    }

    public AlertDialog createLoginDialogo()
    {
        System.out.println("44 RESETT 22");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View viewDialog = inflater.inflate(R.layout.dialog_signin, null);

        builder.setView(viewDialog);

        Button cancelar = (Button) viewDialog.findViewById(R.id.btnCancelar);
        Button enviar = (Button) viewDialog.findViewById(R.id.btnEnviar);
        final EditText email=(EditText)viewDialog.findViewById(R.id.edtEmailVerificacion);

        enviar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String emailAddress =email.getText().toString();
                        if (LoginFragment.validarDominio(emailAddress))
                        {
                            auth.sendPasswordResetEmail(emailAddress)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),"Email enviado..Porfavor cambiar clave..",Toast.LENGTH_SHORT).show();
                                                dialog.cancel();
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(getContext(),"Email no válido de institución.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        cancelar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                }

        );
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed
                Toast.makeText(getContext(),"44 Fallo en logeo",Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        AuthCredential credential;
        if(LoginFragment.validarDominio(acct.getEmail()))
        {
            credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(getContext(), "Autenticacion exitosa!", Toast.LENGTH_SHORT).show();
                                getActivity().finish();
                                Intent i=new Intent(getActivity(),MainActivity.class);
                                getActivity().startActivity(i);
                            }
                            else
                            {
                                // If sign in fails, display a message to the user.
                                System.out.println("33 signInWithCredential:failure"+task.getException());
                                Toast.makeText(getContext(), "Autenticacion falló :(",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            // Firebase sign out
            mAuth.signOut();
            // Google sign out
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            Toast.makeText(getContext(), "Email no válido de Institucion..",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void addAdapterToViews() {

        Account[] accounts = AccountManager.get(getContext()).getAccounts();
        Set<String> emailSet = new HashSet<String>();
        for (Account account : accounts) {
            if (EMAIL_PATTERN.matcher(account.name).matches()) {
                emailSet.add(account.name);
            }

        }
        edtEmail.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(emailSet)));

    }
}
