package ca.georgebrown.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Creating variables

    private TextInputEditText inpEmail, inpPassword;
    private Button loginBtn;
    private ProgressBar loadingPB;
    private TextView newUser;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Initializing variables
        inpEmail = findViewById(R.id.idInputEmail);
        inpPassword = findViewById(R.id.idInputPassword);
        loginBtn = findViewById(R.id.idBtnLogin);
        loadingPB = findViewById(R.id.idPBLoading);
        newUser = findViewById(R.id.idNewUser);
        fAuth = FirebaseAuth.getInstance();

        //on click for new user
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        //on click for login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hiding our progress bar
                loadingPB.setVisibility(View.VISIBLE);
                //retrieving data
                String email = inpEmail.getText().toString();
                String password = inpPassword.getText().toString();
                // Validating input
                if(TextUtils.isEmpty(email) && TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"Please enter your username and password",Toast.LENGTH_SHORT).show();
                    return;
                }
                    //sign in method
                    fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //hiding progress bar
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this, "Login Successful",Toast.LENGTH_SHORT).show();
                                // opening main activity.
                                Intent i  = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(i);
                                finish();
                            }else{
                                //hiding progress bar and displaying toast message
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(LoginActivity.this,"Please enter your email and password or create account",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        //checking if the user is already sign in.
        FirebaseUser user = fAuth.getCurrentUser();
        if(user != null){
            //If not null, open main activity
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }
    }
}

