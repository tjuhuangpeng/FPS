package activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.learning.R;

import java.io.File;

import utils.BitmapUtils;

/**
 * Created by Administrator on 2017/3/7.
 */
public class toGray extends Activity {
    ImageView imageView=null;
    ImageView desImageView = null;
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate4);

        String filePath = "/storage/emulated/0/sogou/mcdcooper/iconcache/actbg_1080.jpg";
        File file = new File(filePath);
        bitmap = BitmapUtils.getBitmap(file);

        imageView= (ImageView) findViewById(R.id.image_rotate);
        desImageView= (ImageView) findViewById(R.id.image_rotates);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        desImageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageBitmap(bitmap);

        findViewById(R.id.btn1_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                    Bitmap bitmaps =  BitmapUtils.toGray(bitmap);
                    desImageView.setImageBitmap(bitmaps);
                }
                else{
                    Toast.makeText(toGray.this,"is null",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
