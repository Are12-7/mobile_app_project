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

import java.util.HashMap;
import java.util.Map;

public class EditMatchActivity extends AppCompatActivity {


    private TextInputEditText inpHomeTeam, inpAwayTeam, inpDate, inpImage, inpLocation, inpDescription;
    private Button updateMatchBtn, deleteMatchBtn;
    private ProgressBar loadingPB;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String matchID;
    private MatchModal matchModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);
        firebaseDatabase = FirebaseDatabase.getInstance();
        inpHomeTeam = findViewById(R.id.idInputHomeTeam);
        inpAwayTeam = findViewById(R.id.idInputAwayTeam);
        inpDate = findViewById(R.id.idInputDate);
        inpImage = findViewById(R.id.idInputImage);
        inpLocation = findViewById(R.id.idInputLocation);
        inpDescription = findViewById(R.id.idInputDescription);
        updateMatchBtn = findViewById(R.id.idBtnEditMatch);
        deleteMatchBtn = findViewById(R.id.idBtnDeleteMatch);
        loadingPB = findViewById(R.id.idPBLoading);

        //Getting modal class
        matchModal = getIntent().getParcelableExtra("match");
        if(matchModal != null){
            //Setting data
            inpHomeTeam.setText(matchModal.getHomeTeam());
            inpAwayTeam.setText(matchModal.getAwayTeam());
            inpDate.setText(matchModal.getMatchDate());
            inpImage.setText(matchModal.getMatchImage());
            inpLocation.setText(matchModal.getMatchLocation());
            inpDescription.setText(matchModal.getMatchDescription());
            matchID = matchModal.getMatchID();
        }
        //Initializing db
        databaseReference = firebaseDatabase.getReference("Matches").child(matchID);
        //on click for update button
        updateMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Progress bar visible
                loadingPB.setVisibility(View.VISIBLE);
                //Getting data
                String homeTeam = inpHomeTeam.getText().toString();
                String awayTeam = inpAwayTeam.getText().toString();
                String matchDate = inpDate.getText().toString();
                String matchImage = inpImage.getText().toString();
                String matchLocation = inpLocation.getText().toString();
                String matchDescription = inpDescription.getText().toString();

                //Map for passing data using key and value pair
                Map<String,Object> map = new HashMap<>();
                map.put("homeTeam",homeTeam);
                map.put("awayTeam",awayTeam);
                map.put("matchDate",matchDate);
                map.put("matchImage",matchImage);
                map.put("matchLocation",matchLocation);
                map.put("matchDescription",matchDescription);
                map.put("matchID",matchID);

                //Calling db reference
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingPB.setVisibility(View.GONE);
                       //adding map to db
                        databaseReference.updateChildren(map);
                        //Displaying toast message
                        Toast.makeText(EditMatchActivity.this,"Match Updated", Toast.LENGTH_SHORT).show();
                        //opening new activity after updating match
                        startActivity(new Intent(EditMatchActivity.this,MainActivity.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Failure toast message
                        Toast.makeText(EditMatchActivity.this,"Error while updating match", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        //on click for delete button
        deleteMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMatch();
            }
        });
    }
    private void deleteMatch(){
        //metho to delete match
        databaseReference.removeValue();
        //Toast message
        Toast.makeText(this,"Match Deleted", Toast.LENGTH_SHORT).show();
        //opening main activity
        startActivity(new Intent(EditMatchActivity.this,MainActivity.class));
    }
}