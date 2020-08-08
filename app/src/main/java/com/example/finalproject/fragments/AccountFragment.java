package com.example.finalproject.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.common.ToastNotify;
import com.example.finalproject.models.User;
import com.example.finalproject.dialogs.AccountDialogFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class AccountFragment extends Fragment {
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private String userId;
    private StorageReference storageReference;
    private ImageView userPhotoView;
    private TextView userNameText;
    private TextView userEmailText;
    private TextView userPhoneText;
    private Button changeProfileButton;
    static final int GALLERY_REQUEST = 10;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userPhotoView = view.findViewById(R.id.user_photo_image);
        userNameText = view.findViewById(R.id.user_name_text);
        userEmailText = view.findViewById(R.id.user_email_text);
        userPhoneText = view.findViewById(R.id.user_phone_text);
        changeProfileButton = view.findViewById(R.id.change_profile_button);
        if (this.getArguments() != null) {
            userId = this.getArguments().getString("userId");
        }
        if (userId != null) {
            db.collection("users").document(userId)
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot,
                                            FirebaseFirestoreException e) {
                            User user = null;
                            if (documentSnapshot != null) {
                                user = documentSnapshot.toObject(User.class);
                            }
                            String name = user.getName();
                            if (!name.isEmpty()) {
                                userNameText.setText(name);
                            }
                            String phone = user.getPhone();
                            if (!phone.isEmpty()) {
                                userPhoneText.setText(phone);
                            }
                            String email = user.getEmail();
                            if (!email.isEmpty()) {
                                userEmailText.setText(email);
                            }
                            String photo = user.getPhoto();
                            if (!photo.isEmpty()) {
                                Picasso.with(getContext()).load(photo).into(userPhotoView);
                            }
                        }
                    });
        }

        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialogFragment = new AccountDialogFragment();
                Bundle args = new Bundle();
                args.putString("userId", userId);
                args.putString("name", userNameText.getText().toString());
                args.putString("phone", userPhoneText.getText().toString());
                dialogFragment.setArguments(args);
                dialogFragment.show(getActivity().getSupportFragmentManager(), "edit");
            }
        });

        userPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            if (selectedImage != null) {
                final StorageReference avatarsStorage = storageReference.child("images/" + userId);
                avatarsStorage.putFile(selectedImage)
                        .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (!task.isSuccessful()) {
                                    ToastNotify.showLongMsg(getContext(), getString(R.string.error_load_avatar));
                                }
                                return avatarsStorage.getDownloadUrl();
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                Uri uri = task.getResult();
                                db.collection("users")
                                        .document(userId)
                                        .update("photo", uri.toString())
                                ;
                            }
                        });
            }
        }

    }


}