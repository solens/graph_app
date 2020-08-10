package com.example.graph_app;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WeightViewModel mWeightViewModel;
    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WeightListAdapter adapter = new WeightListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWeightViewModel = new ViewModelProvider(this).get(WeightViewModel.class);


        mWeightViewModel.getAllWeights().observe(this, new Observer<List<DatedPoint>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChanged(@Nullable final List<DatedPoint> weights) {
                // Update the cached copy of the words in the adapter.
                adapter.setWeights(weights);
                createGraph(weights);
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewWeightActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWeightViewModel.deleteAll();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle intentExtras = data.getExtras();
            DatedPoint weight = (DatedPoint) intentExtras.getParcelable("com.example.android.graph_app.REPLY");
            mWeightViewModel.insert(weight);
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createGraph(List<DatedPoint> allDatedPoints){
        Date x;
        double y;
        PointsGraphSeries series = new PointsGraphSeries<DataPoint>();
        GraphView graph = (GraphView) findViewById(R.id.graph);
        ZoneId defaultZoneId = ZoneId.systemDefault();

        for(DatedPoint datedPoint:allDatedPoints){
            LocalDate localDate = datedPoint.getMeasurementDate().toLocalDate();
            x = Date.from(localDate.atStartOfDay(defaultZoneId).toInstant());
            y = datedPoint.getWeight();
            series.appendData(new DataPoint(x,y),true,allDatedPoints.size());
        }
        series.setColor(Color.MAGENTA);
        graph.removeAllSeries();
        graph.addSeries(series);
        double maxWeight = series.getHighestValueY();
        double minWeight = series.getLowestValueY();
        double maxDate = series.getHighestValueX();
        double minDate = series.getLowestValueX();


//        graph.getViewport().setMinX(minDate-1);
//        graph.getViewport().setMaxX(maxDate+1);
        graph.getViewport().setMinY((Math.ceil((maxWeight+(maxWeight*0.1))/10))*10);
        graph.getViewport().setMaxY((Math.floor((minWeight-(maxWeight*0.1))/10))*10);
//
        graph.getViewport().setYAxisBoundsManual(true);
//        graph.getViewport().setXAxisBoundsManual(true);
        // set date label formatter
        graph.getGridLabelRenderer().resetStyles();
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

// set manual x bounds to have nice steps
       /* graph.getViewport().setMinX(d1.getTime());
        graph.getViewport().setMaxX(d3.getTime());
        graph.getViewport().setXAxisBoundsManual(true);*/

// as we use dates as labels, the human rounding to nice readable numbers
// is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);
    }
}