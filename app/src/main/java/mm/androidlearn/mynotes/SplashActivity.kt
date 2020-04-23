package mm.androidlearn.mynotes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {
    lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setUpSharedPreference()
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        var isLoggedIn = sharedPreferences.getBoolean(PrefConstant.IS_LOGGED_IN,false)
        if(isLoggedIn){
            val intent = Intent(this@SplashActivity,MyNotesActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent = Intent(this@SplashActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setUpSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE)
    }
}