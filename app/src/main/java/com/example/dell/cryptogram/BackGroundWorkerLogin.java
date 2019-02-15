package com.example.dell.cryptogram;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class BackGroundWorkerLogin extends AsyncTask<String,Void,String> {

    LayoutInflater layoutInflater;
    AlertDialog.Builder builder;
    View view;
    Context context;
    Button loginbt;

    String checked;
    String email1;


    BackGroundWorkerLogin(Context con,Button btn,String email_test){
        context=con;
        loginbt=btn;
        email1=email_test;
    }


    @Override
    protected String doInBackground(String... params) {
        String type=params[0];
        String login_url="http://192.168.1.29/cryptochat/login.php";
        String resend_code_url="http://192.168.1.29/cryptochat/resend_code.php";
        String result="";
        if(type.equals("Login")){
            try {
                String username=params[1];
                String password=params[2];
                checked=params[3];
                //Log.i("Value of checked is ",checked);
                LocalDatabaseHelper ldh=new LocalDatabaseHelper(context);
                if(checked.equalsIgnoreCase("true")){
                    ldh.delete_Table();
                    ldh.insert_values(username,password);
                }else{
                    ldh.delete_Table();
                }

                URL url=new URL(login_url);

                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

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
            }


        }else if(type.equals("resend_code")){
            try {
                String email=params[1];
                String code=params[2];
                checked=params[3];
                //Log.i("Value of checked is ",checked);
                LocalDatabaseHelper ldh=new LocalDatabaseHelper(context);


                URL url=new URL(resend_code_url);

                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream= httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data= URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"
                        +URLEncoder.encode("code","UTF-8")+"="+URLEncoder.encode(code,"UTF-8");

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
            }

        }
        return result;
    }


    @Override
    protected void onPreExecute() {

    }


    public void layoutdefined(int resources){
        layoutInflater=LayoutInflater.from(context);
        view=layoutInflater.inflate(resources,null);
        builder=new AlertDialog.Builder(context);
    }

    @Override
    protected void onPostExecute(String result) {

        Log.i("Details : ",result);
        boolean exist=false;

        if(result.equals("Incorrect Login!")){
            exist=false;
            layoutdefined(R.layout.custom_layout_alertdialog);
            InvalidMessageAlertDialog("Login Incorrect!");
        }else if(result.equals("Verify the account!")){
            exist=true;
            layoutdefined(R.layout.verification_layout_alertdialog);
            builder.setPositiveButton("Verify!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ButtonOriginalLayoutChange();
                    dialog.cancel();
                }
            });
            builder.setNegativeButton("Resend Code!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ButtonOriginalLayoutChange();
                    BackGroundWorkerLogin backGroundWorkerLogin=new BackGroundWorkerLogin(context,loginbt,email1);
                   // backGroundWorkerLogin.execute("resend_code",)
                    dialog.cancel();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    ButtonOriginalLayoutChange();
                }
            });
            builder.setView(view);
            builder.show();
        }else if(result.equals("Login Sucessful!")){
            layoutdefined(R.layout.custom_layout_alertdialog);
            InvalidMessageAlertDialog("Suceessfully!");
        }else{
            layoutdefined(R.layout.custom_layout_alertdialog);
            InvalidMessageAlertDialog("Server Error!");
        }

       // ButtonOriginalLayoutChange();
    }

    private void InvalidMessageAlertDialog(String values){
        TextView textView=view.findViewById(R.id.msg);
        ImageView imageView=view.findViewById(R.id.logoimg);
        textView.setText(values);
        imageView.setImageResource(R.drawable.rederror);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
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


    public void ButtonOriginalLayoutChange(){
        loginbt.setBackground(context.getResources().getDrawable(R.drawable.buttonselector));
        loginbt.setTextColor(context.getResources().getColor(R.color.button_text_color));
        loginbt.setText("Login Now");
        loginbt.setEnabled(true);
    }

}
