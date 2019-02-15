package com.example.dell.cryptogram;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class forgotpassword extends AppCompatActivity {
    private TextView infochat;
    private Typeface typeface;
    private LinearLayout passwordResetLt;

    private EditText email,reset,password,retypepassword;
    private Button dynamicbt,signbt,loginbt;
    private final static String namepair="ImDoN";
    private int status=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        passwordResetLt=(LinearLayout) findViewById(R.id.passwordResetLayout);
        infochat=(TextView) findViewById(R.id.infochat);

        email=(EditText) findViewById(R.id.email_et);
        reset=(EditText) findViewById(R.id.resetcode_et);
        password=(EditText) findViewById(R.id.password_et);
        retypepassword=(EditText) findViewById(R.id.retypepass_et);

        dynamicbt=(Button) findViewById(R.id.dyna_btn);
        signbt=(Button) findViewById(R.id.signupbutton);
        loginbt=(Button) findViewById(R.id.loginbutton);

        typeface= Typeface.createFromAsset(getAssets(),"fonts/BRADHITC.TTF");
        infochat.setTypeface(typeface);
        passwordResetLt.setVisibility(View.GONE);

    }

    public void SearchButtonClickedListener(View view){
        if(passwordResetLt.getVisibility()==View.GONE){
            status=0;
        }else{
            status=1;
        }
        System.out.println("Stutus is : "+status);
        System.out.println("Visibility Test is  : "+passwordResetLt.getVisibility());
    }
    public void LoginButtonClickedListener(View view){
        Intent intent=new Intent(forgotpassword.this,MainActivity.class);
        Bundle bundle=new Bundle();
        boolean val=true;
        bundle.putBoolean(namepair,val);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void SignUpButtonClickedListener(View view){
        Intent intent=new Intent(forgotpassword.this,signupactivity.class);
        startActivity(intent);
        finish();
    }
}
