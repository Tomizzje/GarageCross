package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.ImageAdapter;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.example.tomizzje.garagecross.entities.Exercise;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertExerciseActivity extends MenuBaseActivity {


    private static final int PICTURE_RESULT = 42;

    @BindView(R.id.txtTitle)
    EditText txtTitle;

    @BindView(R.id.txtDesc)
    EditText txtDesc;

    @BindView(R.id.spnDifficulty)
    Spinner spnDifficulty;

    @BindView(R.id.btnUpload)
    Button btnUpload;

    @BindView(R.id.btnDeletePicture)
    Button btnDeletePicture;

    @BindView(R.id.btnSaveExercise)
    Button btnSaveExercise;

    @BindView(R.id.btnDeleteExercise)
    Button btnDeleteExercise;

    @BindView(R.id.tvPictureInfo)
    TextView tvPictureInfo;

    @BindView(R.id.rvImages)
    RecyclerView rvImages;

    @BindString(R.string.btnReset)
    String btnResetText;

    @BindString(R.string.btnDeleteExercise)
    String btnDeleteExerciseText;

    @BindString(R.string.database_reference_exercises)
    String exercisesReference;

    @BindString(R.string.intent_bundle_key_modify_exercise)
    String intentModifyExerciseString;

    @BindString(R.string.insert_exercise_save_toast)
    String exerciseSavedToast;

    @BindString(R.string.insert_exercise_delete_toast)
    String exerciseDeletedToast;

    @BindString(R.string.insert_exercise_select_picture)
    String selectPictureText;

    private Exercise exercise;

    private ArrayList<String> imagesUrlList;
    private ArrayList<String> imagesIdList;

    private boolean toModify;

    private  String title = null;
    private  String description = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_exercise);
        ButterKnife.bind(this);

        //TODO
        setSpinner();
        initExercise();

    }

    private void setSpinner() {
        final String[] difficulties = Difficulty.getDifficultyValuesString();
        AdapterView.OnItemSelectedListener itemClickListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
        spnDifficulty.setOnItemSelectedListener(itemClickListener);
        ArrayAdapter aa = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, difficulties);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDifficulty.setAdapter(aa);
    }


    @Override
    protected void onResume() {
        super.onResume();

        String btnCaption = toModify ? btnDeleteExerciseText : btnResetText;
        btnDeleteExercise.setText(btnCaption);
        initOnClickListeners();
    }

    @Override
    public void onBackPressed() {

        if(!imagesUrlList.isEmpty()){
            deleteImages(imagesUrlList);
        }
        super.onBackPressed();
    }

    private void initExercise() {
        imagesUrlList = new ArrayList<>();
        imagesIdList = new ArrayList<>();

        Intent intent = getIntent();
        Exercise exercise = (Exercise) intent.getSerializableExtra(intentModifyExerciseString);
        if (exercise == null) {
            exercise = new Exercise();
            toModify = false;
            btnDeleteExercise.setText(btnResetText);
            tvPictureInfo.setVisibility(View.VISIBLE);

        } else {
            if(exercise.getPicturesUrl() != null) {

                for(Map.Entry<String,String> entry : exercise.getPicturesUrl().entrySet()) {
                    imagesIdList.add(entry.getKey());
                    imagesUrlList.add(entry.getValue());
                }
                initAdapter(imagesUrlList);
            }

            String text = Difficulty.getDifficultyByName(exercise.getDifficulty()).toString();
            int position = getSpinTextPosition(text);
            spnDifficulty.setSelection(position);

            toModify = true;
        }
        this.exercise = exercise;

        if(title != null){
            txtTitle.setText(title);
            txtDesc.setText(description);
        }else {
            txtTitle.setText(exercise.getTitle());
            txtDesc.setText(exercise.getDescription());
        }
    }

    private int getSpinTextPosition(String text) {
        for(int i=0;i<spnDifficulty.getAdapter().getCount();++i){
            if(spnDifficulty.getAdapter().getItem(i).toString().equals(text)){
                return i;
            }
        }
        return 0;
    }


    private void initOnClickListeners() {
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = txtTitle.getText().toString();
                description = txtDesc.getText().toString();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, selectPictureText), PICTURE_RESULT);
            }
        });

        btnDeletePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                deleteImages(imagesUrlList);
                imagesUrlList = new ArrayList<>();
                imagesIdList = new ArrayList<>();
                initAdapter(imagesUrlList);
            }
        });

        btnSaveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExercise();
                Toast.makeText(getBaseContext(), exerciseSavedToast, Toast.LENGTH_LONG).show();
                clean();
                backToList();
            }
        });

        btnDeleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                if(toModify) {
                    firebaseServer.deleteEntity(exercise, exercisesReference);
                    Toast.makeText(view.getContext(), exerciseDeletedToast,
                            Toast.LENGTH_LONG).show();
                    backToList();
                }
                deleteImages(imagesUrlList);
                clean();
            }
        });
    }

    //TODO

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            assert data != null;
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    private void clean() {
        txtTitle.setText("");
        txtDesc.setText("");
        txtTitle.requestFocus();
    }

    private void saveExercise() {
        exercise.setTitle(txtTitle.getText().toString());
        exercise.setDescription(txtDesc.getText().toString());


        String difficulty= Difficulty.getDifficultyByString(spnDifficulty.getSelectedItem().toString()).name();
        // TODO
        HashMap<String,String> imagesUrl = getMapFromList(imagesUrlList, imagesIdList);

        Exercise saveExercise = new Exercise(exercise.getTitle(), exercise.getDescription(), firebaseLogin.getCurrentUser(), difficulty, imagesUrl);
        if(toModify){
            saveExercise.setPushId(exercise.getPushId());
            saveExercise.setPopularity(exercise.getPopularity());
            saveExercise.setFavoritedUsers(exercise.getFavoritedUsers());
            firebaseServer.updateExercise(exercisesReference, exercise.getPushId(), saveExercise);
        }else {
            firebaseServer.insertEntity(saveExercise, exercisesReference);
        }


    }

    private HashMap<String,String> getMapFromList(ArrayList<String> imagesUrlList, ArrayList<String> imagesIdList ) {
        HashMap<String,String> imagesUrl = new HashMap<>();

        for(int i=0; i<imagesUrlList.size();++i){
            imagesUrl.put(imagesIdList.get(i),imagesUrlList.get(i));
        }
        return imagesUrl;
    }

    private void backToList() {
        Intent intent = new Intent(this, PersonalExerciseListActivty.class);
        startActivity(intent);
    }

    private void uploadImage(Uri imageUri) {
        final String uniqueID = UUID.randomUUID().toString();
        final StorageReference filePath = firebaseDepot.getFilePath(imageUri, uniqueID);
        filePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        imagesIdList.add(uniqueID);
                        imagesUrlList.add(uri.toString());
                        initAdapter(imagesUrlList);
                    }
                });
            }
        });
    }

    private void deleteImages(ArrayList<String> list) {
        for(final String imageUrl : list) {
            StorageReference photoRef = firebaseDepot.getStorageReferenceFromUrl(imageUrl);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                   //TODO

                }
            });
        }
    }

    private void initAdapter(ArrayList<String> list) {
        initInfo(list);
        final ImageAdapter adapter = new ImageAdapter(list);
        rvImages.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(exercisesLayoutManager);
    }

    private void initInfo(ArrayList<String> list) {
        tvPictureInfo.setVisibility(View.GONE);
        if(list.isEmpty()){
            tvPictureInfo.setVisibility(View.VISIBLE);
        }
    }
}
