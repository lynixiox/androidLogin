package com.example.tk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.os.AsyncTask;
import android.content.Intent;

import android.os.AsyncTask;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class TestActivity extends AppCompatActivity {

    //variables for UI
    EditText userName, userPassword;
    TextView stateInfo, stateDef, stateHeading;
    Button btnSignIn, btnRegister;

    int iterations = 500;
    int keyLength = 128;
    byte[] saltBytes;

    String URL= "http://10.0.2.2//test/index.php";

    JSONParser jsonParser = new JSONParser();

    boolean register = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        userName = (EditText)findViewById(R.id.txt_userName);
        userPassword = (EditText)findViewById(R.id.txt_Password);
        stateInfo = (TextView) findViewById(R.id.txt_info);
        stateDef = (TextView) findViewById(R.id.txt_state);
        stateHeading = (TextView) findViewById(R.id.txt_signupregister);

        btnSignIn = (Button)findViewById(R.id.btnLogin);
        //btnRegister = (Button)findViewById(R.id.btn_Register);




        SwitchLogin();
        btnSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Login login = new Login();
                char[] passwordChars = userPassword.getText().toString().toCharArray();
                saltBytes = userName.getText().toString().getBytes();
                byte[] hashedBytes = HashPassword(passwordChars, saltBytes, iterations, keyLength);
                String password = new String(Hex.encodeHex(hashedBytes));


                if(!register) {

                    login.execute(userName.getText().toString().trim(),password, "");
                }
                else
                {
                    login.execute(userName.getText().toString().trim(),password, "Register");
                }
            }
        });



    }

    public void onClick(View view) {

        SwitchLogin();

    }

    private class Login extends AsyncTask<String, String, JSONObject>
    {


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... strings) {

            String register = strings[2];
            String password = strings[1];
            String name = strings[0];

            ArrayList parameters = new ArrayList();
            parameters.add(new BasicNameValuePair("username",name));
            parameters.add(new BasicNameValuePair("password",password));
            parameters.add(new BasicNameValuePair("register",register));

            JSONObject json = jsonParser.RequestHTTP(URL, "POST",parameters);

            return json;
        }
        protected void onPostExecute(JSONObject result)
        {
            try {
                if (result != null) {
                    Toast.makeText(getApplicationContext(),result.getString("message"),Toast.LENGTH_LONG).show();
                    //test string
                    if(result.getString("success").equals("1"))
                    {

                        Intent intent = new Intent(TestActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "woopsie, unable to get datas", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public static byte[] HashPassword(final char[] password, final byte[] salt, final int iterations, final int lengthOfKey)
    {
        try
        {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterations, lengthOfKey);
            SecretKey secretKey = secretKeyFactory.generateSecret(pbeKeySpec);
            byte[] hash = secretKey.getEncoded();
            return hash;

        }  catch (NoSuchAlgorithmException | InvalidKeySpecException e)
        {
            throw new RuntimeException(e);
        }
    }
    public void SwitchLogin()
    {
        if(!register)
        {
            stateHeading.setText("Sign Up");
            btnSignIn.setText("Register");
            stateInfo.setText("Already have an account?");
            stateDef.setText("Login");
            register = true;
        }
        else
        {
            stateHeading.setText("Sign In");
            btnSignIn.setText("Login");
            stateInfo.setText("Not a member?");
            stateDef.setText("Register");
            register = false;
        }

    }
}
