package nu.mad.mindyourcash;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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

public class PurchasesFragment extends Fragment implements View.OnClickListener {

    private User user;
    private String account;
    private List<String> purchaseNamesList;
    private List<Double> costList;
    private List<String> categoryList;
    private List<String> dateList;
    private DatabaseReference databaseReference;
    private ListView listView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.purchases_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.user = MainActivity.user;
        this.account = MainActivity.account;
        this.purchaseNamesList = new ArrayList<>();
        this.costList = new ArrayList<>();
        this.categoryList = new ArrayList<>();
        this.dateList = new ArrayList<>();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.listView = view.findViewById(R.id.purchases_listview);

        renderPurchases();

        view.findViewById(R.id.purchases_fab).setOnClickListener(this);
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
        if (id == R.id.purchases_fab) {
            DialogFragment dialogFragment = new AddPurchaseDialogFragment(this.user, this.account);
            dialogFragment.show(getParentFragmentManager(), "AddPurchaseDialogFragment");
        }
    }

    public void renderPurchases() {
        databaseReference.child("users").child(user.username)
                .child("accounts").child(account)
                .child("purchases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        purchaseNamesList.add(dataSnapshot.child("purchaseName")
                                .getValue().toString());
                        costList.add(Double.parseDouble(dataSnapshot.child("cost")
                                .getValue().toString()));
                        categoryList.add(dataSnapshot.child("category").getValue().toString());
                        dateList.add(dataSnapshot.child("date").getValue().toString());
                    }

                    // resource: https://abhiandroid.com/ui/listview
                    PurchasesListViewAdapter purchasesListViewAdapter = new
                            PurchasesListViewAdapter(getContext(), purchaseNamesList.toArray(), costList.toArray(),
                            categoryList.toArray(), dateList.toArray());
                    listView.setAdapter(purchasesListViewAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}