package ovi.fh.homepoly;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.ParseInstallation;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signUpModeActive = true;
    TextView longInTextView;
    EditText usernameEditText;
    EditText passwordEditText;

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            signUpClicked(view);
        }
        return false;
    }
    @Override

    public void onClick(View view) {
        if (view.getId() == R.id.loginTextView){
            Button signUpButton = findViewById(R.id.signUpButton);
            if (signUpModeActive){
                signUpModeActive = false;
                signUpButton.setText("Login");
                longInTextView.setText("or Signup");

            }else {
                signUpModeActive = true;
                signUpButton.setText("signUp");
                longInTextView.setText("or LogIn");

            }

        }else if (view.getId() == R.id.logoImageView || view.getId() == R.id.relativeLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }

    public void signUpClicked(View view){

        if (usernameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")){
            Toast.makeText(MainActivity.this,"A username and a password required",Toast.LENGTH_SHORT).show();
        }else {
            if (signUpModeActive) {
                ParseUser user = new ParseUser();
                user.setUsername(usernameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SignUp", "Success");
                            showUserList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null){
                            Log.i("Login","login ok");
                            showUserList();
                        }else {
                            Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_logoff);


        longInTextView = findViewById(R.id.loginTextView);
        longInTextView.setOnClickListener(this);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        ImageView logoImageView = findViewById(R.id.logoImageView);
        ConstraintLayout constraintLayout = findViewById(R.id.relativeLayout);

        if (ParseUser.getCurrentUser() != null){
            showUserList();
        }

        logoImageView.setOnClickListener(this);
        constraintLayout.setOnClickListener(this);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Properties");
        query.getInBackground("Ga5tOxH7Wm", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null){

                }else {
                    e.printStackTrace();
                }
            }
        });

    }

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), BordMakerMain.class);
        startActivity(intent);
    }

}
