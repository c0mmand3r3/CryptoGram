package com.example.dell.cryptogram;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.Random;


public class signupactivity extends AppCompatActivity {
    private final static String namepair="ImDoN";

    String name,email_address,password,retypePassword;
    int val;

    private EditText fulname,email,pssword,retypepss;
    private Button signup,login,forgotpassword;
    private ProgressBar progressBar;
    private TextView infochat;
    private Typeface typeface;

    private Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupactivity);

        fulname=(EditText) findViewById(R.id.fullname_et);
        email=(EditText) findViewById(R.id.username_et);
        pssword=(EditText) findViewById(R.id.password_et);
        retypepss=(EditText) findViewById(R.id.retypepass_et);

        progressBar=(ProgressBar) findViewById(R.id.progress_bar);

        signup=(Button) findViewById(R.id.signupbutton);
        login=(Button) findViewById(R.id.loginbutton);
        forgotpassword=(Button) findViewById(R.id.forgotpasswordbutton);

        infochat=(TextView) findViewById(R.id.infochat);
        typeface= Typeface.createFromAsset(getAssets(),"fonts/BRADHITC.TTF");
        infochat.setTypeface(typeface);

    }
    public void LoginUpButtonClickedListener(View view){
        Intent intent=new Intent(signupactivity.this,MainActivity.class);
        Bundle bundle=new Bundle();
        boolean val=true;
        bundle.putBoolean(namepair,val);
        intent.putExtras(bundle);
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
    public void SignUpButtonClickedListener(View view){
        PatternRecognizor patternRecognizor=new PatternRecognizor();
        name=fulname.getText().toString();
        email_address=email.getText().toString();
        password=pssword.getText().toString();
        retypePassword=retypepss.getText().toString();
        boolean proceed=true;
        if(name.equals("")){
            ErrorShow(fulname);
            proceed=false;
        }else{
            ErrorHide(fulname);
        }
        if(name.length()<=3){
            ErrorShow(fulname);
            proceed=false;
        }else{
            ErrorHide(fulname);
        }
        if(!patternRecognizor.validate(email_address)){
            ErrorShow(email);
            proceed=false;
        }else{
            ErrorHide(email);
        }
        if(password.length()<8){
            ErrorShow(pssword);
            proceed=false;
        }else{
            ErrorHide(pssword);
        }
        if(retypePassword.length()<8){
            ErrorShow(retypepss);
            proceed=false;
        }else{
            ErrorHide(retypepss);
        }
        if(proceed==true){
            ErrorHide(retypepss);
            if(password.equals(retypePassword)){
                val=generateVerificationCodeForEachSignup();
                ButtonChangeDuringSigning();
                ChangeEditextBackgroundWhole();

                //handler.postDelayed(new Runnable() {
                 //   @Override
                //    public void run() {
                        String type="Signup";
                        signupbackground sbk=new signupbackground(signupactivity.this,signup);
                        sbk.execute(type,signupactivity.this.name,signupactivity.this.email_address,signupactivity.this.password,String.valueOf(signupactivity.this.val));
                  //      ButtonOriginalLayoutChange();
                //    }
               // },1500);
            }else{
                ErrorShow(retypepss);
            }
        }
    }
    public void ForgotPasswordButtonClickedListener(View view){
        Intent intent=new Intent(signupactivity.this,forgotpassword.class);
        startActivity(intent);
        finish();
    }
    @SuppressLint("NewApi")
    private void ChangeEditextBackgroundWhole(){
        fulname.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.editabletextcolorchangeintolock));
        email.setBackground(getResources().getDrawable(R.drawable.editabletextcolorchangeintolock));
        pssword.setBackground(getResources().getDrawable(R.drawable.editabletextcolorchangeintolock));
        retypepss.setBackground(getResources().getDrawable(R.drawable.editabletextcolorchangeintolock));
    }
    @SuppressLint("NewApi")
    public void ButtonChangeDuringSigning(){
        signup.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.button_bg_pressed));
        signup.setTextColor(getApplicationContext().getResources().getColor(R.color.whitecolor));
        signup.setText("Signing Now");
        signup.setEnabled(false);
    }

    public void ButtonOriginalLayoutChange(){
        signup.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.buttonselector));
        signup.setTextColor(getApplicationContext().getResources().getColor(R.color.button_text_color));
        signup.setText("Signup Now");
        signup.setEnabled(true);
    }
    public int generateVerificationCodeForEachSignup(){
        Random random=new Random();
        int value=random.nextInt(90000)+10000;
        return value;
    }

}
