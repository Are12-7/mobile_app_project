package ca.georgebrown.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MatchAdapter.MatchClickInterface {

    //Creating variables
    private RecyclerView matchRV;
    private ProgressBar loadingPB;
    private FloatingActionButton addFloatingButton;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayList<MatchModal> matchModalArrayList;
    private RelativeLayout homeRLayout;
    private MatchAdapter matchAdapter;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializing variables
        matchRV = findViewById(R.id.idMatches);
        loadingPB = findViewById(R.id.idPBLoading);
        addFloatingButton = findViewById(R.id.idFloatingBtn);
        firebaseDatabase = FirebaseDatabase.getInstance();
        //Database reference
        databaseReference = firebaseDatabase.getReference("Matches");
        matchModalArrayList = new ArrayList<>();
        homeRLayout = findViewById(R.id.idViewMatch);
        fAuth = FirebaseAuth.getInstance();
        matchAdapter = new MatchAdapter(matchModalArrayList,this,this);
        matchRV.setLayoutManager(new LinearLayoutManager(this));
        matchRV.setAdapter(matchAdapter);
        //on click for floating button
        addFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Opening new activity for add match
                Intent i = new Intent(MainActivity.this,AddMatchActivity.class);
                startActivity(i);
            }
        });
        //Initializing adapter class
        matchAdapter = new MatchAdapter(matchModalArrayList,this, this::onMatchClick);
        //Setting layout
        matchRV.setLayoutManager(new LinearLayoutManager(this));
        //Setting adapter
        matchRV.setAdapter(matchAdapter);
        //Fetching Matches from db
        getAllMatches();
    }

    //FETCHING ALL MATCHES
    private void getAllMatches(){
        //clearing list
        matchModalArrayList.clear();
        //calling add child event to read the data
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //Hiding progress bar
                loadingPB.setVisibility(View.GONE);
                //adding snapshot
                matchModalArrayList.add(snapshot.getValue(MatchModal.class));
                //notifying adapter that data has changed
                matchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ////notifying adapter when a child is changed
                loadingPB.setVisibility(View.GONE);
                matchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //notifying adapter when child is removed
                loadingPB.setVisibility(View.GONE);
                matchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //notifying adapter when child is moved
                loadingPB.setVisibility(View.GONE);
                matchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onMatchClick(int position) {
        //method to display bottom text
        displayBottomText(matchModalArrayList.get(position));
    }
    private void displayBottomText(MatchModal matchModal){
        //Creating bottom text dialog
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        //Inflating layout
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_text, homeRLayout);
        //Setting content view
        bottomSheetDialog.setContentView(layout);
        //Setting a cancelable
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setCanceledOnTouchOutside(true);
        //calling method to display bottom text
        bottomSheetDialog.show();

        //Creating variables
        ImageView matchImage = layout.findViewById(R.id.idHomeViewImg);
        TextView homeTeam = layout.findViewById(R.id.idHomeTName);
        TextView awayTeam = layout.findViewById(R.id.idAwayTName);
        TextView matchDate = layout.findViewById(R.id.idDate);
        TextView description = layout.findViewById(R.id.idDescription);
        Button editBtn = layout.findViewById(R.id.idBtnEdit);
        Button viewLocationBtn = layout.findViewById(R.id.idBtnViewLocation);

        //Setting data
        homeTeam.setText(matchModal.getHomeTeam());
        awayTeam.setText(matchModal.getAwayTeam());
        matchDate.setText(matchModal.getMatchDate());
        description.setText(matchModal.getMatchDescription());
        //Image
        Picasso.get().load(matchModal.getMatchImage()).into(matchImage);

        //on click for edit button
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //opening EditMatch
                Intent i = new Intent(MainActivity.this,EditMatchActivity.class);
                //passing modal
                i.putExtra("match",matchModal);
                startActivity(i);
            }
        });
        //on click for location button
        viewLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigating to browser for displaying match location from url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(matchModal.getMatchLocation()));
                startActivity(i);
            }
        });
    }

    //MENU


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.idLogOut:
                //Toast message when user looged out
                Toast.makeText(this,"User Logged Out",Toast.LENGTH_SHORT).show();
                //signing out user
                fAuth.signOut();
                //opening login activity
                Intent i = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}