package com.example.administrator.learning;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.DataOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import activity.BitmapActivity;

public class MainActivity extends AppCompatActivity {

    Button hook,refuse_service,error,floatmunu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
;
        hook = (Button) this.findViewById(R.id.btn6);
        refuse_service= (Button) this.findViewById(R.id.btn8);
        error = (Button) this.findViewById(R.id.btn9);
        floatmunu = (Button) this.findViewById(R.id.btn10);
        hook.setOnClickListener(onClickListener);
        refuse_service.setOnClickListener(onClickListener);
        error.setOnClickListener(onClickListener);
        floatmunu.setOnClickListener(onClickListener);

        findViewById(R.id.btn_bitmap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, BitmapActivity.class);
                startActivity(it);
            }
        });

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn6:
                    Intent intent6 = new Intent(MainActivity.this, IsNameActivity.class);
                    startActivity(intent6);
                    break;
                case R.id.btn9:
                    Intent i = new Intent(MainActivity.this,Error.class);
                    startActivity(i);
                    break;


                //本地拒绝服务
 /*               case R.id.btn7:
                    Intent intent = new Intent();
                    intent.putExtra("pushentity", new SelSerializableData());
                    ComponentName componentName = new ComponentName("com.cubic.autohome","com.cubic.autohome.LogoActivity");
                    intent.setComponent(componentName);
                    startActivity(intent);
                    break;*/
                //scheme url
                case R.id.btn8:
                    List<String> cmds = new ArrayList<String>();
                    cmds.add("am start -n com.cubic.autohome/.LogoActivity");
                    try {
                        doCmds(cmds);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btn10:
                    Intent it = new Intent();
                    it.setClass(MainActivity.this,flaotmenu.class);
                    startActivity(it);

            }
        }
    };



    public static void doCmds(List<String> cmds) throws Exception {
        Process process = Runtime.getRuntime().exec("su");
        DataOutputStream os = new DataOutputStream(process.getOutputStream());
        for (String tmpCmd : cmds) {
            os.writeBytes(tmpCmd+"\n");
        }
        os.writeBytes("exit\n");
        os.flush();
        os.close();
        process.waitFor();
    }

    static class SelSerializableData implements Serializable{
        private static final long serialVersionUID = 42L;
        public SelSerializableData(){
            super();
        }
    }

}