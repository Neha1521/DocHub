package com.example.bookie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    TextView camera,gallery;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.profile_fragment, null);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        final TextView name = view.findViewById(R.id.tvProfileName);
        Button signOut = view.findViewById(R.id.btnSignOut);
        CircleImageView profilePic = view.findViewById(R.id.ivProfilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera = view.findViewById(R.id.tvCamera);
                TextView galery = view.findViewById(R.id.tvGallery);
                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent1, 3);
                        //CAMERA_REQUEST_CODE = 3
                        intent1.setType("image/*");

                    }
                });
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    if(Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(auth.getCurrentUser()).getUid())){
                        name.setText(Objects.requireNonNull(dataSnapshot.getValue(User.class)).getName());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        // if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
        if (requestCode == 3 && resultCode == RESULT_OK) {

      /*   mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)

                    .start(this);

*/

            Bitmap mImageUri = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            //camera.setImageBitmap(mImageUri);
        }


        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mSelectImage.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/


    }


}
