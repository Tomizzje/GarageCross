package com.example.tomizzje.garagecross.models;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

public class FirebaseDepot {
    private StorageReference storageReference;


    public FirebaseDepot() {
        storageReference = FirebaseStorage.getInstance().getReference();

    }

    public StorageReference getStorageReferenceFromUrl(String url) {
        return FirebaseStorage.getInstance().getReferenceFromUrl(url);
    }

    public StorageReference getFilePath(Uri imageUri, String uid) {
        Log.d("probaUriServer", imageUri.getLastPathSegment() + uid );
        return storageReference.child("exercises_pictures").child(uid) ;

    }
}
