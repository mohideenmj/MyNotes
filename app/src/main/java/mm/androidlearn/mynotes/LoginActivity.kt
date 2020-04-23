package mm.androidlearn.mynotes

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    lateinit var fullName: TextInputEditText
    lateinit var username: TextInputEditText
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var loginBtn: Button
    lateinit var fullnameText: String
    lateinit var userNameText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindViews()
        setupSharedPreference()
    }

    private fun setupSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }

    private fun bindViews() {
        loginBtn = findViewById(R.id.buttonLogin)
        fullName = findViewById(R.id.editTextFullName)
        username = findViewById(R.id.editTextUserName)
    }
}