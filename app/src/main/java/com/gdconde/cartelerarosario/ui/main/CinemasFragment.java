package com.gdconde.cartelerarosario.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdconde.cartelerarosario.R;
import com.gdconde.cartelerarosario.data.model.Cinema;
import com.gdconde.cartelerarosario.ui.base.BaseFragment;
import com.gdconde.cartelerarosario.util.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by gdconde on 2/3/17.
 */

public class CinemasFragment extends BaseFragment {

    @BindView(R.id.villageTitle) TextView mVillageTitleText;
    @BindView(R.id.villageAddress) TextView mVillageAddressText;
    @BindView(R.id.villageFeatures) TextView mVillageFeaturesText;
    @BindView(R.id.villagePrices) TextView mVillagePricesText;

    private Cinema village;

    public CinemasFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentComponent().inject(this);
        FirebaseDatabase database = Util.getDatabase();
        DatabaseReference myRef = database.getReference("cinemas");

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                village = dataSnapshot.child("village").getValue(Cinema.class);
                if(village != null) {
                    mVillageTitleText.setText(village.name);
                    mVillageAddressText.setText(village.address);
                    mVillageFeaturesText.setText("Estacionamiento, Salas 3D, Salas 4D");
                    mVillagePricesText.setText("General: 150, Niños: 110");
                }
                Timber.d("Value is: %s", village);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Timber.w("Failed to read value: %s", databaseError.toException());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cinemas, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

}
