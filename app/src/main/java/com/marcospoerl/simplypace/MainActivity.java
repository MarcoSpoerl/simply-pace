package com.marcospoerl.simplypace;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.marcospoerl.simplypace.adapter.TermAdapter;
import com.marcospoerl.simplypace.model.Calculator;
import com.marcospoerl.simplypace.model.DistanceTerm;
import com.marcospoerl.simplypace.model.PaceMode;
import com.marcospoerl.simplypace.model.PaceTerm;
import com.marcospoerl.simplypace.model.Term;
import com.marcospoerl.simplypace.model.TimeTerm;
import com.marcospoerl.simplypace.views.HolderView;
import com.woxthebox.draglistview.DragListView;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.marcospoerl.simplypace.model.PaceMode.KMH;
import static com.marcospoerl.simplypace.model.PaceMode.MIN;
import static com.marcospoerl.simplypace.model.PaceMode.MIN100;

public class MainActivity extends AppCompatActivity implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2,
        HmsPickerDialogFragment.HmsPickerDialogHandlerV2 {

    private static final String DISTANCE_KEY = "distance";
    private static final String TIME_KEY = "time";
    private static final String PACE_KEY = "pace";
    private static final String DISTANCE_POS_KEY = "distancePos";
    private static final String TIME_POS_KEY = "timePos";
    private static final String PACE_POS_KEY = "pacePos";
    private static final String PACE_MODE_KEY = "paceMode";

    private final DistanceTerm distanceTerm = new DistanceTerm();
    private final TimeTerm timeTerm = new TimeTerm();
    private final PaceTerm paceTerm = new PaceTerm();

    private TermAdapter termAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createListAdapter();
        setupListView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_help:
                startHelpActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        if  (Term.TYPE_DISTANCE == reference){
            this.distanceTerm.setDistanceInKilometer(fullNumber);
        } else if (Term.TYPE_PACE == reference){
            this.paceTerm.setPace(fullNumber);
        }
        calculateTerm();
    }

    @Override
    public void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes, int seconds) {
        final long totalSeconds = TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minutes) + seconds;
        if (Term.TYPE_TIME == reference) {
            this.timeTerm.setTimeInSeconds(BigDecimal.valueOf(totalSeconds));
        } else if (Term.TYPE_PACE == reference) {
            this.paceTerm.setPace(BigDecimal.valueOf(totalSeconds));
        }
        calculateTerm();
    }

    private PaceMode getPaceMode(){
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        PaceMode paceMode = PaceMode.valueOf(preferences.getString(PACE_MODE_KEY, PaceMode.MIN.toString()));
        return paceMode;
    }

    private void updatePaceMode(PaceMode paceMode){
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PACE_MODE_KEY, paceMode.toString());
        editor.apply();

        paceTerm.setPaceMode(getPaceMode());
        calculateTerm();
    }


    private void createListAdapter() {
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        this.distanceTerm.setDistanceInMeter(new BigDecimal(preferences.getString(DISTANCE_KEY, "0")));
        this.timeTerm.setTimeInSeconds(new BigDecimal(preferences.getString(TIME_KEY, "0")));
        this.paceTerm.setSecondsPerKilometer(new BigDecimal(preferences.getString(PACE_KEY, "0")));
        this.paceTerm.setPaceMode(getPaceMode());
        this.termAdapter = new TermAdapter();
        this.termAdapter.add(this.distanceTerm, preferences.getInt(DISTANCE_POS_KEY, 0));
        this.termAdapter.add(this.timeTerm, preferences.getInt(TIME_POS_KEY, 1));
        this.termAdapter.add(this.paceTerm, preferences.getInt(PACE_POS_KEY, 2));
        this.termAdapter.notifyDataSetChanged();
        this.termAdapter.setOnItemLongClickedListener(new TermAdapter.OnItemLongClickedListener() {
            @Override
            public void onItemLongClicked(HolderView holderView) {
                if (holderView.getTerm().getType() == Term.TYPE_PACE){
                    showPaceModePicker();
                }
            }
        });
        this.termAdapter.setOnItemClickedListener(new TermAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(HolderView holderView) {
                if (holderView.isEditable()) {
                    switch (holderView.getTerm().getType()) {
                        case Term.TYPE_DISTANCE:
                            showDistancePicker();
                            break;
                        case Term.TYPE_TIME:
                            showTimePicker();
                            break;
                        case Term.TYPE_PACE:
                            showPacePicker();
                            break;
                    }
                }
            }
        });
    }

    private void setupListView() {
        final DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_divider));

        final DragListView dragListView = findViewById(R.id.drag_list_view);
        dragListView.setCanDragHorizontally(false);
        dragListView.setLayoutManager(new LinearLayoutManager(this));
        dragListView.getRecyclerView().addItemDecoration(itemDecoration);
        dragListView.setAdapter(this.termAdapter, true);
    }

    private void saveState() {
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DISTANCE_KEY, this.distanceTerm.getDistanceInMeter().toPlainString());
        editor.putString(TIME_KEY, this.timeTerm.getTimeInSeconds().toPlainString());
        editor.putString(PACE_KEY, this.paceTerm.getSecondsPerKilometer().toPlainString());
        editor.putInt(DISTANCE_POS_KEY, this.termAdapter.getPosition(this.distanceTerm));
        editor.putInt(TIME_POS_KEY, this.termAdapter.getPosition(this.timeTerm));
        editor.putInt(PACE_POS_KEY, this.termAdapter.getPosition(this.paceTerm));
        editor.commit();
    }

    private void startHelpActivity() {
        final Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    private void calculateTerm() {
        final Term term1 = this.termAdapter.getItemList().get(0);
        final Term term2 = this.termAdapter.getItemList().get(1);
        final Term term3 = this.termAdapter.getItemList().get(2);
        Calculator.calculate(term1, term2, term3);
        this.termAdapter.notifyDataSetChanged();
    }

    private void showPaceModePicker(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_pace_mode_dialog);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(R.array.pace_modes_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case 0:
                        updatePaceMode(MIN);
                        break;
                    case 1:
                        updatePaceMode(KMH);
                        break;
                    case 2:
                        updatePaceMode(MIN100);
                        break;
                }
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDistancePicker() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_distance_dialog);
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setItems(R.array.distance_array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    showCustomDistancePicker();
                } else {
                    final String[] distances = getResources().getStringArray(R.array.distance_array);
                    final String selectedDistance = distances[i].replaceAll("[^\\d.]+", "");
                    final BigDecimal distanceInKm = new BigDecimal(selectedDistance);
                    distanceTerm.setDistanceInKilometer(distanceInKm);
                    calculateTerm();
                }
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showCustomDistancePicker() {
        final NumberPickerBuilder npb = new NumberPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setDecimalVisibility(View.VISIBLE)
                .setPlusMinusVisibility(View.INVISIBLE)
                .setMaxNumber(BigDecimal.valueOf(999));
        npb.show();
    }

    private void showTimePicker() {
        final HmsPickerBuilder hpb = new HmsPickerBuilder()
                .setFragmentManager(getSupportFragmentManager())
                .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                .setReference(Term.TYPE_TIME);
        hpb.show();
    }

    private void showPacePicker() {
        if (getPaceMode().equals(PaceMode.MIN) || getPaceMode().equals(MIN100)) {
            new HmsPickerBuilder()
                    .setFragmentManager(getSupportFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setReference(Term.TYPE_PACE).show();
        } else {
            new NumberPickerBuilder()
                    .setFragmentManager(getSupportFragmentManager())
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                    .setReference(Term.TYPE_PACE).show();
        }
    }
}
