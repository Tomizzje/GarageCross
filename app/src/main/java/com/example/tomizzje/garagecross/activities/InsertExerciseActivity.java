package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.tomizzje.garagecross.R;
import com.example.tomizzje.garagecross.adapters.ImagesAdapter;
import com.example.tomizzje.garagecross.models.Exercise;
import com.example.tomizzje.garagecross.utils.ExerciseUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InsertExerciseActivity extends MenuBaseActivity {


    private static final int PICTURE_RESULT = 42;

    @BindView(R.id.txtTitle) EditText txtTitle;
    @BindView(R.id.txtDesc) EditText txtDesc;

    @BindView(R.id.numberPicker) NumberPicker numberPicker;

    @BindView(R.id.btnUpload) Button btnUpload;
    @BindView(R.id.btnDeletePicture) Button btnDeletePicture;
    @BindView(R.id.btnSaveExercise) Button btnSaveExercise;
    @BindView(R.id.btnDeleteExercise) Button btnDeleteExercise;

    @BindView(R.id.rvImages) RecyclerView rvImages;

    private  Exercise exercise;

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
        initExercise();
    }


    @Override
    protected void onResume() {
        super.onResume();

        String btnCaption = toModify ? "Feladat törlése" : "Reset";
        btnDeleteExercise.setText(btnCaption);
        initOnClickListeners();
    }


    private void initExercise() {
        imagesUrlList = new ArrayList<>();
        imagesIdList = new ArrayList<>();

        setNumberPicker();
        Intent intent = getIntent();
        Exercise exercise = (Exercise) intent.getSerializableExtra("ModifyExercise");
        if (exercise == null) {
            exercise = new Exercise();
            toModify = false;
            btnDeleteExercise.setText("Reset");

        } else {
            if(exercise.getPicturesUrl() != null) {

                for(Map.Entry<String,String> entry : exercise.getPicturesUrl().entrySet()) {
                    imagesIdList.add(entry.getKey());
                    imagesUrlList.add(entry.getValue());
                }
                initAdapter(imagesUrlList);
            }
            numberPicker.setValue(ExerciseUtils.getDifficultyNumber(exercise.getDifficulty()));

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


    private void initOnClickListeners() {
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = txtTitle.getText().toString();
                description = txtDesc.getText().toString();



                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert picture"), PICTURE_RESULT);
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
                Toast.makeText(getBaseContext(), "Exercise saved!", Toast.LENGTH_LONG).show();
                clean();
                backToList();
            }
        });

        btnDeleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
                if(toModify) {
                    firebaseServer.deleteExercise("exercises", exercise.getUid());
                    Toast.makeText(view.getContext(), "Exercise DELETED!",
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

            Uri imageUri = data.getData();
            Log.d("probaUriActivityResult",  data.getData().toString());
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

        String difficulty = ExerciseUtils.getDifficultyResult(numberPicker.getValue());
        // TODO
        HashMap<String,String> imagesUrl = getMapFromList(imagesUrlList, imagesIdList);

        Exercise saveExercise = new Exercise(exercise.getTitle(), exercise.getDescription(), firebaseLogin.getCurrentUser(), difficulty, imagesUrl);
        if(toModify){
            saveExercise.setUid(exercise.getUid());
            saveExercise.setPopularity(exercise.getPopularity());
            saveExercise.setFavoritedUsers(exercise.getFavoritedUsers());
            firebaseServer.updateExercise("exercises", exercise.getUid(), saveExercise);
        }else {
            firebaseServer.insertExercise(saveExercise, "exercises");
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

    private void setNumberPicker() {
        String [ ] levels = {"könnyű","középhaladó","haladó","profi"};
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setDisplayedValues(levels);
        numberPicker.setMaxValue(3);
        numberPicker.setMinValue(0);
        numberPicker.setWrapSelectorWheel(true);
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
        final ImagesAdapter adapter = new ImagesAdapter(list);
        rvImages.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(exercisesLayoutManager);
    }
}
