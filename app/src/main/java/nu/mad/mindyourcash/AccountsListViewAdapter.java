package nu.mad.mindyourcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

// resource: https://abhiandroid.com/ui/listview
public class AccountsListViewAdapter extends BaseAdapter {

    private Context context;
    private Object[] accountNamesArray;
    private Object[] accountTotalsArray;
    private LayoutInflater layoutInflater;
    private Fragment fragment;

    public AccountsListViewAdapter(Context context, Object[] accountNamesArray, Object [] accountTotalsArray, Fragment fragment) {
        this.context = context;
        this.accountNamesArray = accountNamesArray;
        this.accountTotalsArray = accountTotalsArray;
        this.layoutInflater = LayoutInflater.from(context);
        this.fragment = fragment;
    }

    @Override
    public int getCount() {
        return accountNamesArray.length;
    }

    @Override
    public Object getItem(int i) {
        return accountNamesArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.accounts_listview, null);
        TextView accountName = view.findViewById(R.id.accounts_listview_textview);
        TextView total = view.findViewById(R.id.accounts_total_listview_textview);
        ImageButton delete = view.findViewById(R.id.accounts_listview_button_delete);
        accountName.setText(accountNamesArray[i].toString());

        String accountTotal = accountTotalsArray[i].toString();
        if (accountTotal.indexOf('.') == accountTotal.length() - 2) {
            accountTotal += "0";
        }

        total.setText("$" + accountTotal);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.account = accountNamesArray[i].toString();
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_AccountsFragment_to_PurchasesFragment);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference()
                        .child("users").child(MainActivity.user.username)
                        .child("accounts").child(accountNamesArray[i].toString()).removeValue();
                Snackbar.make(v,
                        "Successfully removed account.", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }

}
