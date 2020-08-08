package nu.mad.mindyourcash;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.mad.mindyourcash.models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {

    private User user;
    private String account;
    private DatabaseReference databaseReference;
    private HashMap<String, Double> categoryToCostTotalMap = new HashMap<>();

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set top text to reflect account name
        this.user = MainActivity.user;
        this.account = MainActivity.account;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();

        // set pie chart heading
        TextView pieChartHeading = view.findViewById(R.id.pie_chart_heading);
        String heading = "Cost Distribution For ";
        if (MainActivity.account != null) {
            heading += this.account;
        } else {
            heading += "Account";
        }
        pieChartHeading.setText(heading);

        this.computePieData(view);
    }

    public void computePieData(final View view) {
        databaseReference.child("users").child(user.username)
                .child("accounts").child(this.account)
                .child("purchases").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        Double cost = Double.parseDouble(dataSnapshot.child("cost").getValue().toString());
                        String category = dataSnapshot.child("category").getValue().toString();

                        // check if category already in map, if yes, then increment the cost
                        if (categoryToCostTotalMap.containsKey(category)) {
                            categoryToCostTotalMap.put(category,
                                    categoryToCostTotalMap.get(category) + cost);
                        } else {
                            // otherwise add the category and initialize with cost
                            categoryToCostTotalMap.put(category, cost);

                        }
                    }
                }

                // retrieve line chart
                PieChart pieChart = view.findViewById(R.id.pieChart);

                // set up account data
                List<PieEntry> pieChartEntries = new ArrayList<>();

                // go through the categoryToCosts map, and populate pieChartEntries
                for (Map.Entry<String, Double> categoryToCost : categoryToCostTotalMap.entrySet()) {
                    float floatCost = (float) (double) categoryToCost.getValue();

                    pieChartEntries.add(new PieEntry(floatCost, categoryToCost.getKey()));
                }

                // make sure num of colors is same as num categories
                int[] colorsArray = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.LTGRAY, Color.YELLOW, Color.CYAN};

                PieDataSet pieDataSet = new PieDataSet(pieChartEntries, "");

                pieDataSet.setColors(colorsArray);

                PieData pieData = new PieData(pieDataSet);

                // increase value text size
                pieData.setValueTextSize(20f);

                pieChart.setData(pieData);

                pieChart.invalidate(); // refresh
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}