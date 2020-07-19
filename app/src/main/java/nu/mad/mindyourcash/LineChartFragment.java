package nu.mad.mindyourcash;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LineChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LineChartFragment extends Fragment {

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_line_chart, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // retrieve line chart
        LineChart chart = (LineChart) view.findViewById(R.id.chart);

        // set up dummy data
        List<Entry> entries = new ArrayList<>();

        // add some dummy data
        entries.add(new Entry(1,500));
        entries.add(new Entry(2,600));
        entries.add(new Entry(3,200));
        entries.add(new Entry(5,500));
        entries.add(new Entry(6,500));
        entries.add(new Entry(7,800));

        LineDataSet dataSet = new LineDataSet(entries, "Label"); // add entries to data set
        dataSet.setColor(1);
        dataSet.setValueTextColor(1); // styling ...

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }
}