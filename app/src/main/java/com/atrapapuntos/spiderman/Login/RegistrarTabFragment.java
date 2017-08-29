package com.atrapapuntos.spiderman.Login;

import android.accounts.Account;
import android.accounts.AccountManager;
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
import android.widget.Toast;

import com.atrapapuntos.spiderman.Main.MainActivity;
import com.atrapapuntos.spiderman.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class RegistrarTabFragment extends Fragment implements View.OnClickListener {

    private View v;
    private EditText edtPass,edtRepetir;
    private AutoCompleteTextView edtEmail;
    private Button btnRegistrarse;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);


    private OnFragmentInteractionListener mListener;

    public RegistrarTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v=inflater.inflate(R.layout.registrar_tab, container, false);
        cargarElementos();
        return v;
    }

    private void cargarElementos()
    {
        mAuth = FirebaseAuth.getInstance();
        edtEmail=(AutoCompleteTextView)v.findViewById(R.id.edtEmail);
        addAdapterToViews();
        edtPass=(EditText)v.findViewById(R.id.edtPass);
        edtRepetir=(EditText)v.findViewById(R.id.edtRepetir);
        btnRegistrarse=(Button)v.findViewById(R.id.btnRegistrarse);
        btnRegistrarse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.btnRegistrarse:
                final String email=edtEmail.getText().toString();
                if (LoginFragment.validarDominio(email)) {
                    final String contraseña = edtPass.getText().toString();
                    String repetir = edtRepetir.getText().toString();
                    if (contraseña.compareTo(repetir) == 0) {
                        mAuth.createUserWithEmailAndPassword(email, contraseña)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        System.out.println("Nombre:" + email + " Contraseña:" + contraseña);
                                        System.out.println("createUserWithEmail:onComplete:" + task.isSuccessful());
                                        if (!task.isSuccessful())
                                        {
                                            try {
                                                throw task.getException();
                                            } catch(FirebaseAuthWeakPasswordException e) {
                                                Toast.makeText(getContext(), "Contraseña muy débil..", Toast.LENGTH_SHORT).show();
                                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                                Toast.makeText(getContext(), "Email inválido..", Toast.LENGTH_SHORT).show();
                                            } catch(FirebaseAuthUserCollisionException e) {
                                                Toast.makeText(getContext(), "Usuario ya registrado!", Toast.LENGTH_SHORT).show();
                                            } catch(Exception e) {
                                            }
                                        }
                                        else {
                                            enviarMailConfirmacion();
                                            Toast.makeText(getContext(), "Usuario registrado..", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(getContext(), MainActivity.class);
                                            startActivity(i);
                                            getActivity().finish();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getContext(), "Debe escribir la misma clave..", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "Email de Institución no válida..", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void enviarMailConfirmacion()
    {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(getActivity(), new OnCompleteListener()
                {
                    @Override
                    public void onComplete(@NonNull Task task)
                    {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getContext(),
                                    "Verificación de email enviada.." + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            System.out.println("sendEmailVerification"+task.getException());
                            Toast.makeText(getContext(),
                                    "Falló envío de verificación de email..",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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