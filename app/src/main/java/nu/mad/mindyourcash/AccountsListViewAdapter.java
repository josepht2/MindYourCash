package nu.mad.mindyourcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// resource: https://abhiandroid.com/ui/listview
public class AccountsListViewAdapter extends BaseAdapter {

    private Context context;
    private Object[] accountNamesArray;
    private LayoutInflater layoutInflater;

    public AccountsListViewAdapter(Context context, Object[] accountNamesArray) {
        this.context = context;
        this.accountNamesArray = accountNamesArray;
        layoutInflater = LayoutInflater.from(context);
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.accounts_listview, null);
        TextView accountName = view.findViewById(R.id.accounts_listview_textview);
        accountName.setText(accountNamesArray[i].toString());
        return view;
    }

}
