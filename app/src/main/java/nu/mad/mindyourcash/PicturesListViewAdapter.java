package nu.mad.mindyourcash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// resource: https://abhiandroid.com/ui/listview
public class PicturesListViewAdapter extends BaseAdapter {

    private Context context;
    private Object[] pictureArray;
    private LayoutInflater layoutInflater;

    public PicturesListViewAdapter(Context context, Object[] pictureArray) {
        this.context = context;
        this.pictureArray = pictureArray;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.pictures_listview, null);
        ImageView picture = view.findViewById(R.id.pictures_listview_image);
        byte[] byteArray = (byte[]) pictureArray[i];
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        picture.setImageBitmap(bitmap);
        return view;
    }
}
