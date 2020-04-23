package mm.androidlearn.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText fullName,username;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Button loginBtn;
    String  fullnameText;
    String  userNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindViews();
        setupSharedPreference();

        View.OnClickListener clickaction = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullnameText = fullName.getText().toString();
                userNameText = username.getText().toString();

                if(!TextUtils.isEmpty(fullnameText) && !TextUtils.isEmpty(userNameText)){
                    Intent intent = new Intent(LoginActivity.this,MyNotesActivity.class);
                    intent.putExtra(AppConstant.FULLNAME,fullnameText);
                    startActivity(intent);
                    saveLoginStatus();
                    saveFullName(fullnameText);
                }
                else{
                    Toast.makeText(LoginActivity.this,"FullName and User Name can't be left empty",Toast.LENGTH_SHORT).show();
                }

            }
        };

        loginBtn.setOnClickListener(clickaction);
    }

    private void saveFullName(String name) {
        editor = sharedPreferences.edit();
        editor.putString(PrefConstant.FULL_NAME,name);
        editor.apply();
    }

    private void saveLoginStatus() {
       editor = sharedPreferences.edit();
       editor.putBoolean(PrefConstant.IS_LOGGED_IN,true);
       editor.apply();
    }

    private void setupSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREF_NAME,MODE_PRIVATE);
    }

    void bindViews(){
        loginBtn = findViewById(R.id.buttonLogin);
        fullName = (TextInputEditText) findViewById(R.id.editTextFullName);
        username = (TextInputEditText) findViewById(R.id.editTextUserName);
    }
}
