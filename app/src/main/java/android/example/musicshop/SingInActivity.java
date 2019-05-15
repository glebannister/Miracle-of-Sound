package android.example.musicshop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingInActivity extends AppCompatActivity {

    private static final String TAG = "SingInActivity";

    private FirebaseAuth Auth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;
    private TextView toggleLoginSingUpTextView;
    private Button loginSingUpButton;

    FirebaseDatabase database;
    DatabaseReference usersReference;

    private boolean loginModActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_in);

        Auth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repeatPasswordEditText = findViewById(R.id.repeatPasswordEditText);
        toggleLoginSingUpTextView = findViewById(R.id.toggleLoginSingUpTextView);
        loginSingUpButton = findViewById(R.id.loginSingUpButton);

        loginSingUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSingUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });

        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("USERS");


        if (Auth.getCurrentUser() != null){
            Intent mainIntent = new Intent(SingInActivity.this, MainActivity.class);
            mainIntent.putExtra("UserEMail", emailEditText.getText().toString().trim());
            startActivity(mainIntent);
        }
    }

    private void loginSingUpUser(String email, String password) {
        if (loginModActive){
            if (passwordEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Password mast be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please, input Email", Toast.LENGTH_SHORT).show();
            } else {
                Auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    Intent mainIntent = new Intent(SingInActivity.this, MainActivity.class);
                                    mainIntent.putExtra("UserEMail", emailEditText.getText().toString().trim());
                                    startActivity(mainIntent);
                                    FirebaseUser user = Auth.getCurrentUser();
                                    createUser(user);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SingInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }

        } else {
            if (!passwordEditText.getText().toString().trim().equals(repeatPasswordEditText.getText().toString().trim())
            ) {
                Toast.makeText(this, "Password don't match", Toast.LENGTH_SHORT).show();
            } else if (passwordEditText.getText().toString().trim().length() < 6) {
                Toast.makeText(this, "Password mast be at least 6 characters", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please, input Email", Toast.LENGTH_SHORT).show();
            } else {
                Auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    //Intent orderIntend = new Intent(MainActivity.this, OrderActivity.class);
                                    Intent mainIntent = new Intent(SingInActivity.this, MainActivity.class);
                                    mainIntent.putExtra("UserEMail", emailEditText.getText().toString().trim());
                                    startActivity(mainIntent);
                                    FirebaseUser user = Auth.getCurrentUser();
                                    //createUser(user);
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SingInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });

            }

        }
    }

    private void createUser(FirebaseUser firebaseUser) {
        Users user = new Users();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());

        usersReference.push().setValue(user);
    }

    public void toggleLoginMod(View view) {
        if (loginModActive){
            loginModActive = false;
            loginSingUpButton.setText("Sign Up");
            toggleLoginSingUpTextView.setText("Or, log In");
            repeatPasswordEditText.setVisibility(View.VISIBLE);
        } else {
            loginModActive = true;
            loginSingUpButton.setText("Sign In");
            toggleLoginSingUpTextView.setText("Or, Sing Up");
            repeatPasswordEditText.setVisibility(View.GONE);
        }
    }
}
