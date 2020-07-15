package nu.mad.mindyourcash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
                String username = usernameEditText.getText().toString().trim();

                if (username.isEmpty()) {
                    usernameEditText.setError("Please enter a username.");
                    return;
                }

                databaseReference.child("users").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                isUsers = snapshot.getValue() != null;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );

                if (isUsers) {
                    databaseReference.child("users").child(username).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.getValue() != null) {
                                        usernameEditText.setError("This username is taken.");
                                    } else {
                                        NavHostFragment.findNavController(RegisterFragment.this)
                                                .navigate(R.id.action_RegisterFragment_to_SecondFragment);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );
                } else {
                    usernameEditText.setError("App is currently offline.");
                }
            }
        });
    }
}