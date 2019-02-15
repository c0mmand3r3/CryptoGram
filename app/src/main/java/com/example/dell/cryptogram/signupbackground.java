package com.example.dell.cryptogram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class signupbackground extends AsyncTask<String,Void,String> {

    Context context;

    private final static String namepair="ImDoN";
    private Handler handler=new Handler();
    LayoutInflater layoutInflater;
    View view;
    AlertDialog.Builder builder;
    Button signup_btn;

    signupbackground(Context con,Button btn){
        context=con;
        signup_btn=btn;
    }

    @Override
    protected String doInBackground(String... params) {
        String type=params[0];
        String result="";
        String login_url="http://192.168.1.29/cryptochat/signup.php";
        if(type.equals("Signup")){
            try {
                String name=params[1].toLowerCase();
                String email=params[2].toLowerCase();
                String password=params[3];
                String verificationcode=params[4];

                Log.i("Name : ",name);
                Log.i("Email : ",email);
                Log.i("Password : ",password);
                Log.i("Verification Code : ",verificationcode);
               // password=encryptor.encrypt(password,email);

                URL url=new URL(login_url);

                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"
                        +URLEncoder.encode("verificationcode","UTF-8")+"="+URLEncoder.encode(verificationcode,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String line="";
                while((line=bufferedReader.readLine())!=null){
                    result+=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    @Override
    protected void onPreExecute() {
        layoutInflater=LayoutInflater.from(context);
        view=layoutInflater.inflate(R.layout.custom_layout_alertdialog,null);


        builder=new AlertDialog.Builder(context);
    }


    @Override
    protected void onPostExecute(String result) {
        boolean exist=false;
        TextView textView=view.findViewById(R.id.msg);
        ImageView imageView=view.findViewById(R.id.logoimg);
        if(result.equals("Email Already Existed!")){
            textView.setText(result);
            imageView.setImageResource(R.drawable.errorwhite);
        }else if(result.equals("You have signed up sucessfully!")){
            exist=true;
            textView.setText(result+"\nVerification Code has been sent to your Email!");
            imageView.setImageResource(R.drawable.whitecorrect);
        }else if(result.equals("Verification Code can't be send!")){
            textView.setText(result);
            imageView.setImageResource(R.drawable.rederror);
        }
        else{
            textView.setText(result);
            imageView.setImageResource(R.drawable.rederror);
        }
        final boolean finalExist = exist;
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                if(finalExist ==true) {
                    Intent intent = new Intent(context, MainActivity.class);
                    Bundle bundle = new Bundle();
                    boolean val = true;
                    bundle.putBoolean(namepair, val);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        builder.setView(view);
        builder.show();
        ButtonOriginalLayoutChange();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public void ButtonOriginalLayoutChange() {
        signup_btn.setBackground(context.getResources().getDrawable(R.drawable.buttonselector));
        signup_btn.setTextColor(context.getResources().getColor(R.color.button_text_color));
        signup_btn.setText("Signup Now");
        signup_btn.setEnabled(true);
    }
}
