package nu.mad.mindyourcash;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PieChartFragment extends Fragment {

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

        // retrieve line chart
        PieChart pieChart = view.findViewById(R.id.pieChart);

        // set up dummy data
        List<PieEntry> entries = new ArrayList<>();

        // add some dummy data
        entries.add(new PieEntry(10,"Rent"));
        entries.add(new PieEntry(20,"Food"));
        entries.add(new PieEntry(34,"Travel"));
        entries.add(new PieEntry(53,"School"));

        // make sure num of colors is same as num categories
        int[] colorsArray = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA};

        PieDataSet pieDataSet = new PieDataSet(entries, "");

        pieDataSet.setColors(colorsArray);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);

        pieChart.invalidate(); // refresh
    }
}