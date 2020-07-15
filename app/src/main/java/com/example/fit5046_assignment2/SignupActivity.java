package com.example.fit5046_assignment2;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$");

    EditText Firstname;
    EditText Surname;
    EditText Email;
    DatePicker Dob;
    EditText Height;
    EditText Weight;
    RadioGroup radioGroup;
    RadioButton Gender;
    EditText Address;
    EditText Postcode;
    EditText Stepspermile;
    EditText Username;
    EditText Password;
    EditText ConfirmPassword;
    TextView tv_success;
    Button Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firstname = (EditText) findViewById(R.id.et_fname);
        Surname = (EditText) findViewById(R.id.et_sname);
        Email = (EditText) findViewById(R.id.et_newemail);
        Dob = (DatePicker) findViewById(R.id.datePicker);
        Height = (EditText) findViewById(R.id.et_height);
        Weight = (EditText) findViewById(R.id.et_weight);
        radioGroup = (RadioGroup) findViewById(R.id.rd_group);
        Address = (EditText) findViewById(R.id.et_address);
        Postcode = (EditText) findViewById(R.id.et_postcode);
        Stepspermile = (EditText) findViewById(R.id.et_steps);
        Username = (EditText) findViewById(R.id.et_username);
        Password = (EditText) findViewById(R.id.et_pwd);
        ConfirmPassword = (EditText) findViewById(R.id.et_confirmpwd);
        Register = (Button) findViewById(R.id.b_register);
        tv_success = (TextView) findViewById(R.id.tv_success);
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        final Spinner level = (Spinner) findViewById(R.id.spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapter);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String fname = Firstname.getText().toString();
                    String sname = Surname.getText().toString();
                    String email = Email.getText().toString();
                    Boolean isEmail = validateEmail(email);

                    String chkdob = (Dob.getYear()+"-"+(Dob.getMonth() + 1)+"-"+Dob.getDayOfMonth());
                    String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

                    String chkHeight = Height.getText().toString();

                    String chkWeight = Weight.getText().toString();

                    String address = Address.getText().toString();

                    String chkPostcode = Postcode.getText().toString();

                    String chkStepsPerMile = Stepspermile.getText().toString();

                    // converting radio button into character
                    Integer radioId = radioGroup.getCheckedRadioButtonId();
                    Gender = (RadioButton)findViewById(radioId);
                    String selectedId = Gender.getText().toString();

                    String chkLevel = level.getSelectedItem().toString();

                    int random_usrId = (int)(Math.random() * ((879 - 200)+ 1) + 200);
                    String ID = Integer.toString(random_usrId);
                    Boolean passwordStrength = isValidPassword(Password.getText().toString());
                    if(passwordStrength == true && isEmail== true) {
                        if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {

                            SignupAsyncTask getSignup = new SignupAsyncTask();
                            getSignup.execute(ID, email, chkdob, fname, sname, chkHeight, chkWeight, selectedId, address, chkPostcode, chkLevel, chkStepsPerMile);
                            System.out.println(ID + " " + email + " " + chkdob + " " + fname + " " + sname + " " + chkHeight + " " + chkWeight + " " + selectedId + " " + address + " " + chkPostcode + "  " + chkLevel + " " + chkStepsPerMile);

                        } else {
                            tv_success.setText("Password mismatch. Check your password again");
                            tv_success.setTextColor(Color.RED);
                        }
                    }
                        else
                    {
                        tv_success.setText("Either your password  or email id doesn't match the standards. Password should be minimum of 6 letters. ");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        });
    }

    public class SignupAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            Appuser appuser = null;
            Credential credential = null;
            String isUsernameExist = RestAPIClient.findByUsrname(Username.getText().toString());
            if (isUsernameExist.equals("[]")) {
                try {
                    String hashPassoword = passwordHash(Password.getText().toString());
                    appuser = new Appuser(Integer.valueOf(params[0]), params[1], new SimpleDateFormat("yyyy-MM-dd").parse(params[2]), params[3], params[4], Integer.valueOf(params[5]), Integer.valueOf(params[6]), params[7].charAt(0), params[8], Integer.valueOf(params[9]), Integer.valueOf(params[10]), Integer.valueOf(params[11]));
                    credential = new Credential(appuser, Username.getText().toString(), hashPassoword, new Date());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                RestAPIClient.createUser(appuser);
                RestAPIClient.createCredential(credential);
                return "Registration has been Successful";
            }
            else
            {
                return "Username already exist. Try other one!";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            tv_success.setText(response);
            tv_success.setTextColor(Color.GREEN);
        }
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

    public boolean isValidPassword(String password) {

        if(password.isEmpty())
        {
            tv_success.setError("Field cannot be empty");
            return false;
        }
        else if(!PASSWORD_PATTERN.matcher(password).matches())
        {
            tv_success.setError("Password too weak");
            return false;
        }
        else
        {
            tv_success.setError(null);
            return  true;
        }
    }

    public boolean validateEmail(String email)
    {
        if(email.isEmpty())
        {
            tv_success.setError("Email cannot be empty");
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            tv_success.setError("Please enter a valid email address");
            return false;
        }
        else
        {
            tv_success.setError(null);
            return true;
        }
    }

}
