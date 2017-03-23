package activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.administrator.learning.R;

import java.io.File;

import utils.BitmapUtils;

/**
 * Created by Administrator on 2017/3/7.
 */
public class rotateXY extends Activity {
    Button btn = null;
    ImageView imageView = null;
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate5);
        btn = (Button) findViewById(R.id.btn1_rotate);
        imageView = (ImageView) findViewById(R.id.image_rotate);

        String filePath = "/storage/emulated/0/sogou/mcdcooper/iconcache/actbg_1080.jpg";
        File file = new File(filePath);
        bitmap = BitmapUtils.getBitmap(file);
        imageView.setImageBitmap(bitmap);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmaps =  BitmapUtils.addReflection(bitmap,699);
                imageView.setImageBitmap(bitmaps);
            }
        });

    }
}
