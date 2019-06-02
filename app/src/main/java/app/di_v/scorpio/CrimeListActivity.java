package app.di_v.scorpio;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import app.di_v.scorpio.crime.Crime;
import app.di_v.scorpio.crime.CrimeLab;

/**
 *
 * @author di-v
 */
public class CrimeListActivity extends AppCompatActivity {
    private static final String TAG = "CrimeListActivity";

    private RecyclerView mCrimeRecyclerView;
    private CrimeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_list);

        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, " mAuth: " + mAuth + "; ");

        if (mAuth == null) {
            Log.d(TAG, " start LoginActivity ");
            Intent intentLogin = new Intent(this, LoginActivity.class);
            startActivity(intentLogin);
            this.finish();
        }

        mCrimeRecyclerView = findViewById(R.id.recycler_view_crime);
        mCrimeRecyclerView.setHasFixedSize(true);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        updateUI();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Crime crime = new Crime();
                CrimeLab.get(CrimeListActivity.this).addCrime(crime);
                Intent intent = CrimeActivity.newIntent(CrimeListActivity.this, crime.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_crime_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_crime:
                searchDialogStart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void searchDialogStart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_search, null))
            .setTitle(R.string.search_crime);
                //.setIcon(R.drawable.ic_)
        builder.setPositiveButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.setNegativeButton(R.string.search,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(this);
        List<Crime> crimes = crimeLab.getCrimes();

        if (mAdapter == null) {
            mAdapter = new CrimeListAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
    }
}
