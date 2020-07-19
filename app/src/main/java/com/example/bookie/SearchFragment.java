package com.example.bookie;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.DownloadManager;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

public class SearchFragment extends Fragment {

    private Spinner docTypeSpinner;
    private List<String> docKeyList;
    private List<String> doc;
    private DownloadManager downloadManager;
    private StorageReference storageReference;
    private ArrayAdapter<String> docsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, null);

        docTypeSpinner = view.findViewById(R.id.spDocType);
        ListView docListView = view.findViewById(R.id.lvDocList);
        doc = new ArrayList<>();

        docKeyList = new ArrayList<>();
        docsAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.support_simple_spinner_dropdown_item, doc);

        docListView.setAdapter(docsAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.support_simple_spinner_dropdown_item, getActivity().getResources().getStringArray(R.array.document_types));
        docTypeSpinner.setAdapter(typeAdapter);


        storageReference = FirebaseStorage.getInstance().getReference().child("doc");
        downloadManager = (DownloadManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().DOWNLOAD_SERVICE);

        docTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(docTypeSpinner.getSelectedItem().toString());
                final DatabaseReference name = FirebaseDatabase.getInstance().getReference().child("Books");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        doc.clear();
                        docKeyList.clear();
                        for(final DataSnapshot dataSnapshot: snapshot.getChildren()){

                            docKeyList.add(dataSnapshot.getKey());
                            name.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dataSnapshot1:snapshot.getChildren()){
                                        if(Objects.equals(dataSnapshot1.getKey(), dataSnapshot.getKey())){
                                            doc.add(Objects.requireNonNull(dataSnapshot1.getValue(Upload.class)).getName());
                                            System.out.println(Objects.requireNonNull(dataSnapshot1.getValue(Upload.class)).getName());
                                            docsAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        docListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = docKeyList.get(position);
                System.out.println(key);

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

        return view;
    }
}
