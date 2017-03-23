package activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.learning.R;


/**
 * Created by Administrator on 2017/3/7.
 */
public class BitmapActivity extends Activity {

    Intent intent =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bitmap);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BitmapActivity.this,rotate00.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BitmapActivity.this,rotateXY.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BitmapActivity.this,addReflection.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BitmapActivity.this,reverseBitmap.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BitmapActivity.this,toGray.class);
                startActivity(intent);
            }
        });
    }
}
