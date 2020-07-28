package nu.mad.mindyourcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

// resource: https://abhiandroid.com/ui/listview
public class PurchasesListViewAdapter extends BaseAdapter {

    private Context context;
    private Object[] purchaseNamesArray;
    private Object[] costArray;
    private LayoutInflater layoutInflater;

    public PurchasesListViewAdapter(Context context, Object[] purchaseNamesArray, Object[] costArray) {
        this.context = context;
        this.purchaseNamesArray = purchaseNamesArray;
        this.costArray = costArray;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return purchaseNamesArray.length;
    }

    @Override
    public Object getItem(int i) {
        return purchaseNamesArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.purchases_listview, null);
        TextView purchaseName = view.findViewById(R.id.purchases_listview_purchaseName);
        TextView cost = view.findViewById(R.id.purchases_listview_cost);

        String currentCost = costArray[i].toString();
        if (currentCost.indexOf('.') == currentCost.length() - 2) {
            currentCost += "0";
        }

        purchaseName.setText(purchaseNamesArray[i].toString());
        cost.setText(currentCost);
        return view;
    }
}
