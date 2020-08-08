package nu.mad.mindyourcash;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;

// resource: https://abhiandroid.com/ui/listview
public class PurchasesListViewAdapter extends BaseAdapter {

    private Context context;
    private Object[] purchaseNamesArray;
    private Object[] costArray;
    private Object[] categoryArray;
    private Object[] dateArray;
    private LayoutInflater layoutInflater;

    public PurchasesListViewAdapter(Context context, Object[] purchaseNamesArray, Object[] costArray,
                                    Object[] categoryArray, Object[] dateArray) {
        this.context = context;
        this.purchaseNamesArray = purchaseNamesArray;
        this.costArray = costArray;
        this.categoryArray = categoryArray;
        this.dateArray = dateArray;
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
        TextView category = view.findViewById(R.id.purchases_listview_category);
        TextView date = view.findViewById(R.id.purchases_listview_date);

        String currentCost = costArray[i].toString();
        if (currentCost.indexOf('.') == currentCost.length() - 2) {
            currentCost += "0";
        }

        purchaseName.setText(purchaseNamesArray[i].toString());
        cost.setText(currentCost);
        category.setText(categoryArray[i].toString());

        Date savedDate = new Date(dateArray[i].toString());
        String dateString = (savedDate.getMonth() + 1) + "/" + savedDate.getDate() + "/"
                + (savedDate.getYear() + 1900);

        date.setText(dateString);
        return view;
    }
}
