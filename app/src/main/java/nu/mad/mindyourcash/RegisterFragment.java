package nu.mad.mindyourcash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import nu.mad.mindyourcash.models.User;

public class RegisterFragment extends Fragment {

    private EditText usernameEditText;
    private DatabaseReference databaseReference;
    private boolean isUsers;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameEditText = view.findViewById(R.id.register_username_edittext);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        isUsers = false;

        view.findViewById(R.id.register_submit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernameEditText.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameEditText.setError("Please enter a username.");
                    return;
                }

                databaseReference.child("users").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                boolean isUsernameTaken = false;
                                if (snapshot.getValue() != null) {
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        User storedUser = dataSnapshot.getValue(User.class);
                                        if (storedUser.username.equals(username)) {
                                            usernameEditText.setError("This username is taken.");
                                            isUsernameTaken = true;
                                            break;
                                        }
                                    }

                                    if (!isUsernameTaken) {
                                        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(
                                                getActivity(), new OnSuccessListener<InstanceIdResult>() {
                                                    @Override
                                                    public void onSuccess(InstanceIdResult instanceIdResult) {
                                                        String token = instanceIdResult.getToken();
                                                        User user = new User(username, token);
                                                        MainActivity.user = user;
                                                        databaseReference.child("users").child(username).setValue(user);
                                                        NavHostFragment.findNavController(RegisterFragment.this)
                                                                .navigate(R.id.action_RegisterFragment_to_SecondFragment);
                                                        Snackbar.make(getActivity().findViewById(R.id.mainactivity_layout),
                                                                "Successfully registered.", Snackbar.LENGTH_LONG).show();
                                                    }
                                                }
                                        );
                                    }
                                } else {
                                    usernameEditText.setError("App is currently offline.");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
            }
        });
    }
}