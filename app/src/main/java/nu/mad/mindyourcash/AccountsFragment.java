package nu.mad.mindyourcash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nu.mad.mindyourcash.models.User;

public class AccountsFragment extends Fragment implements View.OnClickListener {

    private User user;
    private List<String> accountNamesList;
    private Set<String> accountNamesSet;
    private DatabaseReference databaseReference;
    private ListView listView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.accounts_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.user = MainActivity.user;
        this.accountNamesList = new ArrayList<>();
        this.accountNamesSet = new HashSet<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listView = view.findViewById(R.id.accounts_listview);

        renderAccounts(view);

        view.findViewById(R.id.accounts_fab).setOnClickListener(this);
    }

    /**
     * onClick for LinkCollectorFragment.
     * If the floating action button is clicked, then it will bring up the dialog to add a link.
     *
     * @param view the view associated with this onClick
     * @author Joseph Triolo
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.accounts_fab) {
            DialogFragment dialogFragment = new AddAccountDialogFragment(this.user, this.accountNamesSet);
            dialogFragment.show(getParentFragmentManager(), "AddAccountDialogFragment");
        }
    }

    public void renderAccounts(final View view) {
        databaseReference.child("users").child(user.username).child("accounts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                accountNamesList.add(dataSnapshot.child("accountName")
                                        .getValue().toString());
                                accountNamesSet.add(dataSnapshot.child("accountName")
                                        .getValue().toString()
                                        .replaceAll(" ", "")
                                        .replaceAll("\n", "")
                                        .replaceAll("\t", "")
                                        .toLowerCase());
                            }

                            // resource: https://abhiandroid.com/ui/listview
                            AccountsListViewAdapter accountsListViewAdapter = new
                                    AccountsListViewAdapter(getContext(), accountNamesList.toArray(),
                                    AccountsFragment.this);
                            listView.setAdapter(accountsListViewAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}