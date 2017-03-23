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


public class addReflection extends Activity {
    ImageView imageView=null;
    ImageView desImageView = null;
    Bitmap bitmap=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rotate2);

        String filePath = "/storage/emulated/0/sogou/mcdcooper/iconcache/actbg_1080.jpg";
        File file = new File(filePath);
        bitmap = BitmapUtils.getBitmap(file);

        imageView= (ImageView) findViewById(R.id.image_rotate);
        desImageView= (ImageView) findViewById(R.id.image_rotates);
        imageView.setImageBitmap(bitmap);

        findViewById(R.id.btn1_rotate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                    Bitmap bitmaps =  BitmapUtils.addReflection(bitmap,300);
                    desImageView.setImageBitmap(bitmaps);
                }
                else{
                    Toast.makeText(addReflection.this,"is null",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn1_rotate2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                    Bitmap bitmaps =  BitmapUtils.addReflection(bitmap,900);
                    desImageView.setImageBitmap(bitmaps);
                }
                else{
                    Toast.makeText(addReflection.this,"is null",Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn1_rotate3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bitmap!=null){
                    Bitmap bitmaps =  BitmapUtils.addReflection(bitmap,-300);
                    desImageView.setImageBitmap(bitmaps);
                }
                else{
                    Toast.makeText(addReflection.this,"is null",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
