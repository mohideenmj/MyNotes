package mm.androidlearn.mynotes

import android.content.Context
import android.content.Intent
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

        val clickAction = object : View.OnClickListener{
            override fun onClick(p0: View?) {
                val fullname = fullName.text.toString()
                val username= username.text.toString()

                if(fullname.isNotEmpty() && username.isNotEmpty()){
                    val intent = Intent(this@LoginActivity,MyNotesActivity::class.java)
                    intent.putExtra(AppConstant.FULLNAME,fullname)
                    startActivity(intent)
                    saveLoginStatus()
                    saveFullName(fullname)
                }
            }
        }

        loginBtn.setOnClickListener(clickAction)
    }

    private fun saveFullName(fullname: String) {
        editor = sharedPreferences.edit()
        editor.putString(PrefConstant.FULL_NAME,fullname)
        editor.apply()

    }

    private fun saveLoginStatus() {
        editor = sharedPreferences.edit()
        editor.putBoolean(PrefConstant.IS_LOGGED_IN, true)
        editor.apply()
    }
}