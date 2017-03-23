package com.example.administrator.learning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/*
   https://github.com/rovo89/XposedBridge/wiki/Development-tutorial
   https://github.com/rovo89/XposedBridge/wiki/Using-the-Xposed-Framework-API
*/
public class IsNameActivity extends Activity {

    Button bt;
    EditText name,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        name = (EditText) this.findViewById(R.id.name);
        pass = (EditText) this.findViewById(R.id.pass);
        bt = (Button) this.findViewById(R.id.btn);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(login(name.getText().toString(),pass.getText().toString())){
                    Intent intent1 = new Intent(IsNameActivity.this,testActivity.class);
                    startActivity(intent1);
                }else{
                    Intent intent2 = new Intent(IsNameActivity.this,Videolist.class);
                    startActivity(intent2);
                }
            }

        });
    }

    Boolean login(String name,String pass){
     if(name.equals("1") && pass.equals("1")){
       return  true;
     }else {
      return  false;
     }
    }

}