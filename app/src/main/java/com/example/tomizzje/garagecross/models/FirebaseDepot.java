package com.example.tomizzje.garagecross.models;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseDepot {
    private StorageReference storageReference;

    public FirebaseDepot() {
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    /**
     * Returns the referenec of the image
     * @param url of the image
     * @return storagereference
     */
    public StorageReference getStorageReferenceFromUrl(String url) {
        return FirebaseStorage.getInstance().getReferenceFromUrl(url);
    }

    /**
     *  Returns storagereference of the id
     * @param uid of the file
     * @return storagereference
     */
    public StorageReference getFilePath(String uid) {
        return storageReference.child("exercises_pictures").child(uid) ;

    }
}
