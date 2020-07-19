package com.example.bookie;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class UploadFragment extends Fragment {

    private final static int PICK_PDF_CODE = 2342;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private Spinner docType;
    private EditText docName;
    private ProgressBar uploadBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.upload_fragment, null);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        docType = view.findViewById(R.id.spDoctype);
        docName = view.findViewById(R.id.etDocname);
        Button upload = view.findViewById(R.id.btnUpload);
        uploadBar = view.findViewById(R.id.pbUpload);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.support_simple_spinner_dropdown_item, getActivity().getResources().getStringArray(R.array.document_types));
        docType.setAdapter(typeAdapter);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDoc();
            }
        });

        return view;

    }

    private void getDoc(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //when the user chooses the file
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            }else{
                Toast.makeText(getActivity(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {
        uploadBar.setVisibility(View.VISIBLE);
        final String docKey = databaseReference.push().getKey();

        StorageReference sRef = storageReference.child("doc/"+docKey+".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        uploadBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "File uploaded", Toast.LENGTH_SHORT).show();
                        Upload upload = new Upload(docName.getText().toString(),
                                docType.getSelectedItem().toString(),
                                Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).
                                        getReference()).getDownloadUrl().toString());

                        assert docKey != null;
                        databaseReference.child("Books").child(docKey).setValue(upload);
                        databaseReference.child(docType.getSelectedItem().toString()).child(docKey).setValue("");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        uploadBar.setProgress((int) progress);
                        // add percentage too
                    }
                });

    }


}
