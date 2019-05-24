package app.di_v.scorpio;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import app.di_v.scorpio.crime.Crime;
import app.di_v.scorpio.crime.CrimeLab;
import app.di_v.scorpio.crime.CrimeMedia;

/**
 * CrimeActivity for a detailed crime description
 * @author di-v
 */
public class CrimeActivity extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final String EXTRA_CRIME_ID = "app.di_v.scorpio.crime_id";

    private RecyclerView mCrimeRecyclerView;
    private CrimeMediaAdapter mAdapter;

    private Crime mCrime;
    private CrimeMedia mCrimeMedia;
    private File mPhotoFile;
    private EditText mTitleField;
    private EditText mDescriptionField;
    private Button mDateButton;
    private ImageButton mPhotoBtn;
    private ImageButton mVideoBtn;
    private ImageButton mAudioBtn;


    public static Intent newIntent(Context packageContext, UUID id) {
        Intent intent = new Intent(packageContext, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime);

        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mCrime = CrimeLab.get(CrimeActivity.this).getCrime(crimeId);

        mCrimeRecyclerView = findViewById(R.id.recyclerView_crime);
        mCrimeRecyclerView.setHasFixedSize(true);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));

        // Title
        mTitleField = findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
                CrimeLab.get(CrimeActivity.this).updateCrime(mCrime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Description
        mDescriptionField = findViewById(R.id.crime_description);
        mDescriptionField.setText(mCrime.getDescription());
        mDescriptionField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setDescription(s.toString());
                CrimeLab.get(CrimeActivity.this).updateCrime(mCrime);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = mCrime.getDate();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CrimeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                mCrime.setDate(new GregorianCalendar(year, month, dayOfMonth).getTime());
                                CrimeLab.get(CrimeActivity.this).updateCrime(mCrime);
                                updateDate();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Photo btn
        mPhotoBtn = findViewById(R.id.add_crime_photo);
        PackageManager packageManager = getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = captureImage.resolveActivity(packageManager) != null;
        mPhotoBtn.setEnabled(canTakePhoto);

        mPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCrimeMedia = new CrimeMedia(mCrime.getId());
                mPhotoFile = CrimeLab.get(CrimeActivity.this).getMediaFile(mCrimeMedia);
                Uri uri = FileProvider.getUriForFile(CrimeActivity.this,
                        "app.di_v.scorpio.file_provider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = CrimeActivity.this
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    CrimeActivity.this.grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_IMAGE_CAPTURE);
            }
        });

       // Video btn
        mVideoBtn = findViewById(R.id.add_crime_video);
        mVideoBtn.setEnabled(false);

        // Audio btn
        mAudioBtn = findViewById(R.id.add_crime_audio);
        mAudioBtn.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_crime, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:
                deleteCrime();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteCrime() {
        CrimeLab.get(CrimeActivity.this).deleteCrime(mCrime);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            Uri uri = FileProvider.getUriForFile(CrimeActivity.this,
                    "app.di_v.scorpio.file_provider",
                    mPhotoFile);

            CrimeActivity.this.revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            CrimeLab.get(CrimeActivity.this).addMedia(mCrimeMedia);

            updateUI();
        }
    }

    private void updateDate() {
        mDateButton.setText(DateFormat.format("dd.MM.yyyy", mCrime.getDate()).toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(this);
        List<CrimeMedia> crimes = crimeLab.getMedia(mCrime.getId());

        if (mAdapter == null) {
            mAdapter = new CrimeMediaAdapter(crimes);
            mCrimeRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }
    }
}
