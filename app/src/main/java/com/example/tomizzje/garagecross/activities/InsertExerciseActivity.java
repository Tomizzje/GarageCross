package com.example.tomizzje.garagecross.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.tomizzje.garagecross.entities.Exercise;
import com.example.tomizzje.garagecross.enums.Difficulty;
import com.google.android.gms.tasks.OnFailureListener;
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

public class InsertExerciseActivity extends BaseActivity {


    private static final int PICTURE_RESULT = 42;

    /**
     * Fields connected by the view and strings.xml
     */

    @BindView(R.id.txtTitle)
    EditText txtTitle;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

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

    @BindString(R.string.insert_exercise_title)
    String tvInfoInsert;

    @BindString(R.string.modify_exercise_title)
    String tvInfoModify;

    @BindString(R.string.insert_exercise_too_long_text)
    String txtTitleSizeToast;

    @BindString(R.string.insert_exercise_too_long_description)
    String txtDescSizeToast;

    @BindString(R.string.insert_exercise_deleted_images)
    String deletedImagesToast;

    @BindString(R.string.unknown_error_text)
    String errorToast;

    @BindString(R.string.insert_exercise_no_images_to_delete)
    String noImagesToDeleteToast;

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

        setSpinner();
        initExercise();

    }

    @Override
    protected void onResume() {
        super.onResume();

        String btnCaption = toModify ? btnDeleteExerciseText : btnResetText;
        btnDeleteExercise.setText(btnCaption);
        initOnClickListeners();
    }

    /**
     * Set spinner to choose difficulty
     */
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

    /**
     * Initialize exercise and the page whether the users would like to modify or create a new one
     */
    private void initExercise() {
        imagesUrlList = new ArrayList<>();
        imagesIdList = new ArrayList<>();

        Intent intent = getIntent();
        Exercise exercise = (Exercise) intent.getSerializableExtra(intentModifyExerciseString);
        if (exercise == null) {
            exercise = new Exercise();
            toModify = false;
            tvTitle.setText(tvInfoInsert);
            btnDeleteExercise.setText(btnResetText);
            tvPictureInfo.setVisibility(View.VISIBLE);


        } else {
            tvPictureInfo.setVisibility(View.VISIBLE);
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
            tvTitle.setText(tvInfoModify);

        }
        this.exercise = exercise;

        if(title != null){
            txtTitle.setText(title);
            txtDesc.setText(description);
        }else {
            txtTitle.setText(exercise.getTitle());
            txtDesc.setText(exercise.getDescription());
        }
        txtTitle.setSelection(txtTitle.getText().length());
    }


    /**
     * Initialize button OnClickListeners
     */

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
                startActivityForResult(Intent.createChooser(intent, selectPictureText), PICTURE_RESULT);
            }
        });

        btnDeletePicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!imagesUrlList.isEmpty()){
                    deleteImages(imagesUrlList);
                    Toast.makeText(view.getContext(), deletedImagesToast,Toast.LENGTH_LONG).show();
                    imagesUrlList = new ArrayList<>();
                    imagesIdList = new ArrayList<>();
                    initAdapter(imagesUrlList);
                }else {
                    Toast.makeText(view.getContext(), noImagesToDeleteToast,Toast.LENGTH_LONG).show();
                }

            }
        });

        btnSaveExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExercise();
            }
        });

        btnDeleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toModify) {
                    firebaseServer.deleteEntity(exercise, exercisesReference);
                    Toast.makeText(view.getContext(), exerciseDeletedToast,Toast.LENGTH_LONG).show();
                    backToList();
                    finish();
                }
                deleteImages(imagesUrlList);
                imagesUrlList = new ArrayList<>();
                imagesIdList = new ArrayList<>();
                initAdapter(imagesUrlList);
                clean();
            }
        });
    }


    /**
     *  This method is called after the user returns from choosing a picture. If resultCode is the pictureCode, then upload the image to the storage
     * @param requestCode the requestcode we sent to get a picture
     * @param resultCode the result code after the return
     * @param data the data chosen by the user
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {
            assert data != null;
            Uri imageUri = data.getData();
            uploadImage(imageUri);
        }
    }

    /**
     * Check if the user modifies or creates an exercise and check if the inputs are valid
     */
    private void saveExercise() {
        exercise.setTitle(txtTitle.getText().toString());
        exercise.setDescription(txtDesc.getText().toString());
        String difficulty= Difficulty.getDifficultyByString(spnDifficulty.getSelectedItem().toString()).name();
        HashMap<String,String> imagesUrl = getMapFromList(imagesUrlList, imagesIdList);

        if(checkTitleTextSize() && checkDescriptionTextSize()){
            Exercise saveExercise = new Exercise(exercise.getTitle(), exercise.getDescription(), firebaseLogin.getCurrentUser(), difficulty, imagesUrl);
            if(toModify){
                saveExercise.setPushId(exercise.getPushId());
                saveExercise.setPopularity(exercise.getPopularity());
                saveExercise.setRatedUsers(exercise.getRatedUsers());
                saveExercise.setFavoritedUsers(exercise.getFavoritedUsers());
                firebaseServer.updateExercise(exercisesReference, exercise.getPushId(), saveExercise);
            }else {
                firebaseServer.insertEntity(saveExercise, exercisesReference);
            }
            Toast.makeText(this, exerciseSavedToast, Toast.LENGTH_LONG).show();
            backToList();
            finish();

        }else if(!checkTitleTextSize()) {
            Toast.makeText(this, txtTitleSizeToast,Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, txtDescSizeToast,Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkDescriptionTextSize() {
        return txtDesc.getText().length() > 19 && txtDesc.getText().length() < 201 && txtDesc.getLineCount() < 10;
    }

    private boolean checkTitleTextSize() {
        return txtTitle.getText().length() > 2 && txtTitle.getText().length() < 16;
    }

    /**
     * Creates a hashmap from 2 lists
     * @param imagesUrlList the url list
     * @param imagesIdList the id list
     * @return a Hashmap
     */
    private HashMap<String,String> getMapFromList(ArrayList<String> imagesUrlList, ArrayList<String> imagesIdList ) {
        HashMap<String,String> imagesUrl = new HashMap<>();

        for(int i=0; i<imagesUrlList.size();++i){
            imagesUrl.put(imagesIdList.get(i),imagesUrlList.get(i));
        }
        return imagesUrl;
    }

    /**
     * Navigates back to the list
     */
    private void backToList() {
        Intent intent = new Intent(this, PersonalExerciseListActivity.class);
        startActivity(intent);
    }

    /**
     * This method uploads the image to the storage,saves the url and the id, and initialize the list of images
     * @param imageUri contains the data of the picture
     */
    private void uploadImage(Uri imageUri) {
        final String uniqueID = UUID.randomUUID().toString();
        final StorageReference filePath = firebaseDepot.getFilePath(uniqueID);
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

    /**
     * Delete the images from the storage
     * @param list the url list of images
     */
    private void deleteImages(ArrayList<String> list) {

        for(final String imageUrl : list) {
            StorageReference photoRef = firebaseDepot.getStorageReferenceFromUrl(imageUrl);
            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), errorToast,Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    /**
     * Initialize the adapter with the list of images
     * @param list url list of images
     */
    private void initAdapter(ArrayList<String> list) {
        initInfo(list);
        final ImageAdapter adapter = new ImageAdapter(list);
        rvImages.setAdapter(adapter);
        LinearLayoutManager exercisesLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvImages.setLayoutManager(exercisesLayoutManager);
    }

    /**
     * Set visibility for Textview depends on list size
     * @param list url list of images
     */
    private void initInfo(ArrayList<String> list) {
        tvPictureInfo.setVisibility(View.GONE);
        if(list.isEmpty()){
            tvPictureInfo.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method returns the position of the selected item
     * @param text spinner item
     * @return the position of the spinner
     */
    private int getSpinTextPosition(String text) {
        for(int i=0;i<spnDifficulty.getAdapter().getCount();++i){
            if(spnDifficulty.getAdapter().getItem(i).toString().equals(text)){
                return i;
            }
        }
        return 0;
    }

    /**
     * It cleans the EditTexts
     */
    private void clean(){
        txtTitle.setText("");
        txtDesc.setText("");
    }
}
