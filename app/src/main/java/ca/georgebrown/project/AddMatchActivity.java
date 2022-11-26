package ca.georgebrown.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddMatchActivity extends AppCompatActivity {

    private TextInputEditText inpHomeTeam, inpAwayTeam, inpDate, inpImage, inpLocation, inpDescription;
    private Button addMatchBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String matchID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_match);

        //Initializing variables
        inpHomeTeam = findViewById(R.id.idInputHomeTeam);
        inpAwayTeam = findViewById(R.id.idInputAwayTeam);
        inpDate = findViewById(R.id.idInputDate);
        inpImage = findViewById(R.id.idInputImage);
        inpLocation = findViewById(R.id.idInputLocation);
        inpDescription = findViewById(R.id.idInputDescription);
        addMatchBtn = findViewById(R.id.idBtnAddMatch);
        loadingPB = findViewById(R.id.idPBLoading);
        //Initialized firebase
        firebaseDatabase = FirebaseDatabase.getInstance();
        //database reference
        databaseReference = firebaseDatabase.getReference("Matches");

        //on click for add match button
        addMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                //Retrieving data
                String homeTeam = inpHomeTeam.getText().toString();
                String awayTeam = inpAwayTeam.getText().toString();
                String matchDate = inpDate.getText().toString();
                String matchImage = inpImage.getText().toString();
                String matchLocation = inpLocation.getText().toString();
                String matchDescription = inpDescription.getText().toString();
                matchID = homeTeam;

                //passing data
                MatchModal matchModal = new MatchModal(matchID,homeTeam,awayTeam,matchDate,matchImage,matchLocation,matchDescription);
                //calling add value event to pass data to firebase
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                        //Setting data in database
                        databaseReference.child(matchID).setValue(matchModal);
                        //Toast Message
                        Toast.makeText(AddMatchActivity.this,"Match Added..", Toast.LENGTH_SHORT).show();
                        //Starting Main Activity
                        startActivity(new Intent(AddMatchActivity.this,MainActivity.class));
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Failure message
                        Toast.makeText(AddMatchActivity.this,"Error " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}