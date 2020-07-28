package nu.mad.mindyourcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

// resource: https://abhiandroid.com/ui/listview
public class AccountsListViewAdapter extends BaseAdapter {

    private Context context;
    private Object[] accountNamesArray;
    private LayoutInflater layoutInflater;
    private Fragment fragment;

    public AccountsListViewAdapter(Context context, Object[] accountNamesArray, Fragment fragment) {
        this.context = context;
        this.accountNamesArray = accountNamesArray;
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
        accountName.setText(accountNamesArray[i].toString());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.account = accountNamesArray[i].toString();
                NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_AccountsFragment_to_PurchasesFragment);
            }
        });
        return view;
    }

}
