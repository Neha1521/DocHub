package com.example.bookie;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class DownloadFragment extends Fragment {

    private ListView docListView;
    private List<String> docKeyList;
    private DownloadManager downloadManager;
    private StorageReference storageReference;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.download_fragment, null);

        docListView = view.findViewById(R.id.lvDocList);
        docKeyList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Books");
        storageReference = FirebaseStorage.getInstance().getReference().child("doc");
        downloadManager = (DownloadManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().DOWNLOAD_SERVICE);

        docListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = docKeyList.get(position);

                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            2);
                }


                storageReference.child(key+".pdf").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri uri1 = Uri.parse(uri.toString());
                        DownloadManager.Request request = new DownloadManager.Request(uri1);

                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        request.setDestinationInExternalFilesDir(getActivity(), DIRECTORY_DOWNLOADS, "trial.pdf");
                        downloadManager.enqueue(request);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), (CharSequence) e, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                List<String> doc = new ArrayList<>();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    docKeyList.add(dataSnapshot.getKey());
                    doc.add(Objects.requireNonNull(dataSnapshot.getValue(Upload.class)).getName());
                }

                ArrayAdapter<String> docsAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, doc);

                docListView.setAdapter(docsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
