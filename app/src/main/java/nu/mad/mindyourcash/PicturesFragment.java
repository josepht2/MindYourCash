package nu.mad.mindyourcash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class PicturesFragment extends Fragment {

    private StorageReference storageReference;
    private Button button;
    private ListView listView;
    private List<byte[]> pictureArray;
    private PicturesFragment picturesFragment;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.pictures_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // resource: https://stackoverflow.com/questions/40885860/how-to-save-bitmap-to-firebase
        storageReference = FirebaseStorage.getInstance().getReference();

        button = view.findViewById(R.id.pictures_button);
        listView = view.findViewById(R.id.pictures_listview);

        pictureArray = new ArrayList<>();

        picturesFragment = this;

        renderPictures();

        // resource: https://www.tutlane.com/tutorial/android/android-camera-app-with-examples
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        view.findViewById(R.id.pictures_button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(PicturesFragment.this)
                        .navigate(R.id.action_PicturesFragment_to_PurchasesFragment);
            }
        });
    }

    // resource: https://www.tutlane.com/tutorial/android/android-camera-app-with-examples
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");

                // resource: https://firebase.google.com/docs/storage/android/upload-files
                //resource: https://stackoverflow.com/questions/40885860/how-to-save-bitmap-to-firebase
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                final StorageReference imageReference = storageReference
                        .child("users").child(MainActivity.user.username)
                        .child("accounts").child(MainActivity.account)
                        .child("pictures").child(bitmap.toString());
                UploadTask uploadTask = imageReference.putBytes(byteArray);
                uploadTask
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                picturesFragment.renderPictures();
                                Snackbar.make(getActivity().findViewById(R.id.pictures_constraintlayout),
                                        "Successfully added picture.", Snackbar.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(getActivity().findViewById(R.id.pictures_constraintlayout),
                                        "Failed to add picture.", Snackbar.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }

    private void renderPictures() {
        // resource: https://stackoverflow.com/questions/37335102/how-to-get-a-list-of-all-files-in-cloud-storage-in-a-firebase-app
        storageReference
                .child("users").child(MainActivity.user.username)
                .child("accounts").child(MainActivity.account)
                .child("pictures").listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                pictureArray = new ArrayList<>();

                for (StorageReference imageReference : listResult.getItems()) {
                    // resource: https://firebase.google.com/docs/storage/android/download-files
                    imageReference.getBytes(1024 * 1024)
                            .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] byteArray) {
                                    pictureArray.add(byteArray);
                                    PicturesListViewAdapter picturesListViewAdapter = new PicturesListViewAdapter(getContext(),
                                            pictureArray.toArray());
                                    listView.setAdapter(picturesListViewAdapter);
                                }
                            });
                }
            }
        });
    }
}