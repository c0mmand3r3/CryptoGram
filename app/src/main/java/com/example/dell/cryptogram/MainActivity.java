package com.example.dell.cryptogram;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private CheckBox password_rem;
    private EditText usrname,pssword;
    private Button loginbt,signupbt,forgotpassword;
    private boolean valueTOshow=false;
    private TextView infochat;
    private Typeface typeface;
    private RelativeLayout relay1,relay2;
    private Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            relay1.setVisibility(View.VISIBLE);
            relay2.setVisibility(View.VISIBLE);
        }
    };


    private String usr,pass;

    LocalDatabaseHelper ldh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ldh=new LocalDatabaseHelper(this);
        ldh.list_all_values();

        relay1=(RelativeLayout) findViewById(R.id.relay1);
        relay2=(RelativeLayout) findViewById(R.id.relay2);

        usrname=(EditText) findViewById(R.id.username);
        pssword=(EditText) findViewById(R.id.password);
        password_rem=(CheckBox) findViewById(R.id.remember_password);

        loginbt=(Button) findViewById(R.id.loginbutton);
        signupbt=(Button) findViewById(R.id.signupbutton);
        forgotpassword=(Button) findViewById(R.id.forgotpasswordbutton);

        Intent responsibleIntent=getIntent();
        Bundle bundle=responsibleIntent.getExtras();
        if(bundle!=null) {
            valueTOshow = bundle.getBoolean("ImDoN");
        }
        if(!valueTOshow==true){
            relay1.setVisibility(View.GONE);
            relay2.setVisibility(View.GONE);
        }
        String[] values;
        values=ldh.get_remember_login();
        if(values!=null){
            usrname.setText(values[0]);
            pssword.setText(values[1]);
            ChangeEditextBackgroundWhole();
            password_rem.setChecked(true);
        }
        infochat=(TextView) findViewById(R.id.infochat);
        typeface=Typeface.createFromAsset(getAssets(),"fonts/BRADHITC.TTF");
        infochat.setTypeface(typeface);

        handler.postDelayed(runnable,2000);

    }
    public void LoginButtonClickedListener(View view){
        PatternRecognizor patternRecognizor=new PatternRecognizor();
        usr=usrname.getText().toString();
        pass=pssword.getText().toString();
        boolean proceed=true;
        if(!patternRecognizor.validate(usr)){
            ErrorShow(usrname);
            proceed=false;
        }else{
            ErrorHide(usrname);
        }
        if(pass.length()<8){
            ErrorShow(pssword);
            proceed=false;
        }else{
            ErrorHide(pssword);
        }
        if(proceed==true){
            ButtonChangeDuringSigning();
            ChangeEditextBackgroundWhole();

            //handler.postDelayed(new Runnable() {
            //    @Override
            //    public void run() {
                    String type="Login";
                    BackGroundWorkerLogin backGroundWorkerLogin=new BackGroundWorkerLogin(MainActivity.this,loginbt,MainActivity.this.usr);
                    backGroundWorkerLogin.execute(type,MainActivity.this.usr,MainActivity.this.pass,String.valueOf(password_rem.isChecked()),String.valueOf(password_rem.isChecked()));
             //   }
            //},1500);
        }
    }
    private void ChangeEditextBackgroundWhole(){
        usrname.setBackground(getResources().getDrawable(R.drawable.editabletextcolorchangeintolock));
        pssword.setBackground(getResources().getDrawable(R.drawable.editabletextcolorchangeintolock));
    }
    private void ButtonChangeDuringSigning(){
        loginbt.setBackground(getResources().getDrawable(R.drawable.button_bg_pressed));
        loginbt.setTextColor(getResources().getColor(R.color.whitecolor));
        loginbt.setText("Logging Now");
        loginbt.setEnabled(false);
    }

    public void ButtonOriginalLayoutChange(){
        loginbt.setBackground(getResources().getDrawable(R.drawable.buttonselector));
        loginbt.setTextColor(getResources().getColor(R.color.button_text_color));
        loginbt.setText("Login Now");
        loginbt.setEnabled(true);
    }

    public void SignUpButtonClickedListener(View view){
        Intent intent=new Intent(getApplicationContext(),signupactivity.class);
        startActivity(intent);
        finish();
    }

    public void ForgotPasswordButtonClickedListener(View view){
        Intent intent=new Intent(getApplicationContext(),forgotpassword.class);
        startActivity(intent);
        finish();
    }
    private void ErrorShow(EditText editText){
        Drawable dr = getResources().getDrawable(R.drawable.error_icons_editext);
        //add an error icon to yur drawable files
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

        editText.setCompoundDrawables(null,null,dr,null);
    }
    private void ErrorHide(EditText editText){
        Drawable dr = getResources().getDrawable(R.drawable.correct);
        //add an error icon to yur drawable files
        dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());

        editText.setCompoundDrawables(null,null,dr,null);
    }
}
