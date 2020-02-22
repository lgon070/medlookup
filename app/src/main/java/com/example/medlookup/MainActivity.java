package com.example.medlookup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Button searchButton;
    private EditText searchQuery;
    private ImageView imageBG;
    private Button backButton;
    private TextView coveredByTextView;
    private TextView errorMsg;
    private TextView providersTextView;
    final private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchButton = findViewById(R.id.searchButton);
        searchQuery = findViewById(R.id.searchQueryEdit);
        imageBG = findViewById(R.id.medCoverImg);
        errorMsg = findViewById(R.id.errorMsg);
        backButton = findViewById(R.id.backButton);
        coveredByTextView = findViewById(R.id.coveredByTextView);
        providersTextView = findViewById(R.id.providersTextView);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchQuery.getText().toString().equals("")){
                    errorMsg.setVisibility(View.VISIBLE);
                }
                else{
                    if(errorMsg.getVisibility() == View.VISIBLE){
                        errorMsg.setVisibility(View.GONE);
                    }
                    String filter = searchQuery.getText().toString();
                    retrieveData(filter);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToSearch();
            }
        });

    }

    public void retrieveData(final String filter) {
        String childSelected = filter.toLowerCase();

        Query query = databaseReference.child("allMedicines").child(childSelected);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String providers = "";
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        String provider = snap.getValue(String.class);
                        providers = providers + "[" + provider + "]\n";
                    }

                } else {
                        providers = "No Providers Available";
                }
                displayData(filter, providers);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void displayData(String query, String providers){
        searchButton.setVisibility(View.GONE);
        searchQuery.setVisibility(View.GONE);
        imageBG.setVisibility(View.GONE);


        coveredByTextView.setText(query + " is Provided By:");
        providersTextView.setText(providers);

        backButton.setVisibility(View.VISIBLE);
        coveredByTextView.setVisibility(View.VISIBLE);
        providersTextView.setVisibility(View.VISIBLE);





    }

    public void returnToSearch(){
        backButton.setVisibility(View.GONE);
        coveredByTextView.setVisibility(View.GONE);
        providersTextView.setVisibility(View.GONE);

        searchQuery.getText().clear();
        searchButton.setVisibility(View.VISIBLE);
        searchQuery.setVisibility(View.VISIBLE);
        imageBG.setVisibility(View.VISIBLE);

    }

}
