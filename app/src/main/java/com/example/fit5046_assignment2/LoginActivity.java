package com.example.fit5046_assignment2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView Name;
    private EditText UserName;
    private TextView Password;
    private EditText UserPassword;
    private Button Submit;
    private Button Signup;
    private TextView Info;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Name = (TextView) findViewById(R.id.tv_email);
        UserName = (EditText) findViewById(R.id.et_email);
        Password = (TextView) findViewById(R.id.tv_password);
        UserPassword = (EditText) findViewById(R.id.et_password);
        Submit = (Button) findViewById(R.id.b_submit);
        Info = (TextView) findViewById(R.id.tv_info);
        Signup = (Button) findViewById(R.id.b_signup);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value)
                {
                    // Show Password
                    UserPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    // Hide Password
                    UserPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String username = UserName.getText().toString();
                    Boolean chkemail = validateEmail(username);
                    String password = UserPassword.getText().toString();
                    String hashPassoword = passwordHash(password);
                    Boolean chkpassword = validatePassword(hashPassoword);
                    if (username.isEmpty() || password.isEmpty()) {
                        Info.setText("Username or Password cannot be empty");
                    }
                    else {
                        LoginAsyncTask getLogin = new LoginAsyncTask();
                        getLogin.execute(username, hashPassoword);
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(registration);

            }
        });
    }

    public static String passwordHash(String password)
    {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateEmail(String username) {
        if (username.isEmpty()) {
            UserName.setError("Username cannot be empty.");
            return false;
        }
         else {
            UserName.setError(null);
            return true;
        }
    }

    public boolean validatePassword(String password) {
        if (password.isEmpty()) {
            UserPassword.setError("Password must be entered");
            return false;
        } else {
            UserPassword.setError(null);
            return true;
        }
    }

    public class LoginAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            //System.out.println("Hey");
            return RestAPIClient.findByUsrCredentials(params[0],params[1]);
        }
        @Override
        protected void onPostExecute (String credentials){
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, Object>>() {}.getType();
            Map<String, Object> data = gson.fromJson(credentials, mapType);
            if(!credentials.isEmpty() && (Boolean) data.get("loginStatus") == true) {
                System.out.println("Entering OnPost method");
                String firstName = data.get("firstname").toString();
                String userId = data.get("userid").toString();
                String address = data.get("usraddress").toString();
                String postcode = data.get("usrpostcode").toString();
                String usrsteps = data.get("usrsteps").toString();
                    Intent newpage = new Intent(LoginActivity.this, MainActivity.class);
                    newpage.putExtra("isFromLogin",true);
                    newpage.putExtra("firstname",firstName);
                    newpage.putExtra("userid",userId);
                    newpage.putExtra("usraddress",address);
                    newpage.putExtra("usrpostcode",postcode);
                    newpage.putExtra("usrsteps",usrsteps);

                    newpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    newpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    newpage.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(newpage);
                    finish();
                //}
                // else if
            }
            else
            {
                String information = " Username or Password does not match with our records. Please check your credentials again.";
                Info.setText(information);
                Info.setTextColor(Color.RED);
            }
        }
    }

}