package nu.mad.mindyourcash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

// resource: https://abhiandroid.com/ui/listview
public class PicturesListViewAdapter extends BaseAdapter {

    private Context context;
    private PicturesFragment picturesFragment;
    private Object[] pictureArray;
    private Object[] pictureNameArray;
    private LayoutInflater layoutInflater;

    public PicturesListViewAdapter(Context context, PicturesFragment picturesFragment,
                                   Object[] pictureArray, Object[] pictureNameArray) {
        this.context = context;
        this.picturesFragment = picturesFragment;
        this.pictureArray = pictureArray;
        this.pictureNameArray = pictureNameArray;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return pictureArray.length;
    }

    @Override
    public Object getItem(int i) {
        return pictureArray[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.pictures_listview, null);
        ImageView picture = view.findViewById(R.id.pictures_listview_image);
        ImageButton delete = view.findViewById(R.id.pictures_listview_button_delete);
        byte[] byteArray = (byte[]) pictureArray[i];
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        picture.setImageBitmap(bitmap);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseStorage.getInstance().getReference()
                        .child("users").child(MainActivity.user.username)
                        .child("accounts").child(MainActivity.account)
                        .child("pictures").child(pictureNameArray[i].toString())
                        .delete().addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                picturesFragment.renderPictures();
                            }
                        }
                );
                Snackbar.make(picturesFragment.getView().findViewById(R.id.pictures_constraintlayout),
                        "Successfully removed picture.", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
