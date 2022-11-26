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

public class RegisterActivity extends AppCompatActivity {

    //Creating variables
    private TextInputEditText inpEmail, inpPassword, inpCnfPassword;
    private Button registerBtn;
    private ProgressBar loadingPB;
    private TextView login;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initializing variables
        inpEmail = findViewById(R.id.idInputEmail);
        inpPassword = findViewById(R.id.idInputPassword);
        inpCnfPassword = findViewById(R.id.idInputCnfPwd);
        registerBtn = findViewById(R.id.idBtnRegister);
        loadingPB = findViewById(R.id.idPBLoading);
        login = findViewById(R.id.idLogin);
        fAuth = FirebaseAuth.getInstance();

        //on click for login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        //on click for register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hiding progress bar
                loadingPB.setVisibility(View.VISIBLE);

                //Retrieving data
                String userEmail = inpEmail.getText().toString();
                String password = inpPassword.getText().toString();
                String confirmPassword = inpCnfPassword.getText().toString();

                //Checking if password and confirm password match
                if(!password.equals(confirmPassword)){
                    Toast.makeText(RegisterActivity.this, "Make sure your password is the same", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userEmail) && TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword)){
                    Toast.makeText(RegisterActivity.this,"Please enter your email and password",Toast.LENGTH_SHORT).show();
                }else {
                    //Creating new user
                    fAuth.createUserWithEmailAndPassword(userEmail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                //hiding progress bar and opening login activity
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this,"User already registered",Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                //Displaying toast message
                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegisterActivity.this,"Error while registering user",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}