package nu.mad.mindyourcash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import nu.mad.mindyourcash.models.User;

public class FirstFragment extends Fragment {

    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        // resource: https://stackoverflow.com/questions/51929290/is-it-possible-to-set-startdestination-conditionally-using-android-navigation-ar/51961843
        NavHostFragment navHostFragment = (NavHostFragment) getActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        final NavController navController = navHostFragment.getNavController();

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                getActivity(), new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        final String token = instanceIdResult.getToken();

                        databaseReference.child("users").addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                            User storedUser = dataSnapshot.getValue(User.class);
                                            if (storedUser.token.equals(token)) {
                                                MainActivity.user = storedUser;

                                                // resource: https://stackoverflow.com/questions/51929290/is-it-possible-to-set-startdestination-conditionally-using-android-navigation-ar
                                                navController.navigate(R.id.action_FirstFragment_to_AccountsFragment);

                                                break;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                }
                        );
                    }
                }
        );

        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_RegisterFragment);
            }
        });

        view.findViewById(R.id.line_chart_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_LineChartFragment);
            }
        });
    }
}