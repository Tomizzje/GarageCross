package com.example.tomizzje.garagecross.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.entities.Record;

import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertRecordActivity extends BaseActivity {

    /**
     * Fields connected by the view and strings.xml
     */

    @BindView(R.id.tvDate)
    TextView tvDate;

    @BindView(R.id.etDescription)
    EditText etDescription;

    @BindView(R.id.btnSave)
    Button btnSave;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    @BindString(R.string.insert_record_select_date_text)
    String dateText;

    @BindString(R.string.insert_record_no_date_toast)
    String noDateToastText;

    @BindString(R.string.insert_record_no_description_toast)
    String noDescriptionToastText;

    @BindString(R.string.database_reference_records)
    String recordsReference;

    @BindString(R.string.intent_bundle_key_modify_record)
    String intentRecordString;

    @BindString(R.string.insert_record_too_long_description)
    String tooLongDescriptionToastText;

    @BindString(R.string.insert_record_deleted_toast)
    String deletedRecordToast;

    @BindString(R.string.insert_record_saved_toast)
    String savedRecordToast;

    @BindString(R.string.insert_record_modified_toast)
    String modifiedRecordToast;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    private boolean toModify = false;

    private Record record;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_record);
        ButterKnife.bind(this);
        initRecord();
    }

    /**
     * Initialize the Record depends on the user's intention
     */
    private void initRecord() {
        Intent intent = getIntent();
        Record getRecord = (Record) intent.getSerializableExtra(intentRecordString);
        if(getRecord != null){
            tvDate.setText(getRecord.getDate());
            etDescription.setText(getRecord.getDescription());

            etDescription.setSelection(getRecord.getDescription().length()); //Set focus for EditText
            toModify = true;
        }else {
            getRecord = new Record();
        }
        this.record = getRecord;

    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatePicker();
        initSave();
        initDelete();
    }

    /**
     * Initialize delete button, and delete the record from database
     */
    private void initDelete() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(toModify){
                   firebaseServer.deleteEntity(record, recordsReference);
                   Toast.makeText(view.getContext(), deletedRecordToast,
                           Toast.LENGTH_LONG).show();
                   backToList();
               }else {
                   etDescription.setText("");
               }
            }
        });
    }

    /**
     * Initalize save button, check the inputs, and save to database
     */
    private void initSave() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(tvDate.getText().equals(dateText)) && etDescription.getText().length() > 0 && etDescription.getLineCount() < 9) {
                    String currentUser = firebaseLogin.getCurrentUser();
                    String date = tvDate.getText().toString();
                    String description = etDescription.getText().toString();
                    Record saveRecord = new Record(currentUser,date,description);
                    if(toModify){
                        saveRecord.setPushId(record.getPushId());
                        firebaseServer.modifyEntity(saveRecord, recordsReference);
                        Toast.makeText(view.getContext(), modifiedRecordToast,
                                Toast.LENGTH_LONG).show();
                    }else {
                        firebaseServer.insertEntity(saveRecord, recordsReference);
                        Toast.makeText(view.getContext(), savedRecordToast,
                                Toast.LENGTH_LONG).show();
                    }
                    backToList();
                }else if(tvDate.getText().equals(dateText)){
                    Toast.makeText(view.getContext(), noDateToastText,
                            Toast.LENGTH_LONG).show();
                }else if(etDescription.getText().length() < 1 ){
                    Toast.makeText(view.getContext(), noDescriptionToastText,
                            Toast.LENGTH_LONG).show();
                }else if(etDescription.getLineCount() >=0){
                    Toast.makeText(view.getContext(), tooLongDescriptionToastText,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Initialize datepicker dialogue
     */
    private void initDatePicker() {
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        InsertRecordActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener,
                        year,
                        month,
                        day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = year + "/" + month + "/" + day;
                tvDate.setText(date);
            }
        };
    }

    /**
     * Navigate back to the list
     */
    private void backToList() {
        Intent intent = new Intent(this, WeightLiftingDiaryActivity.class);
        startActivity(intent);
    }
}
