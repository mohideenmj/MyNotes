package mm.androidlearn.mynotes

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {
    lateinit var textTitle: TextView
    lateinit var textDesc: TextView
    var TAG = "DetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        bindViews()
        setIntentData()
    }

    fun bindViews() {
        textTitle = findViewById(R.id.textTitleInDetail)
        textDesc = findViewById(R.id.textDesc)
    }

    fun setIntentData() {
        val intent = intent
        Log.d(TAG, intent.getStringExtra(AppConstant.TITLE))
        val title = intent.getStringExtra(AppConstant.TITLE)
        val description = intent.getStringExtra(AppConstant.DESCRIPTION)
        textTitle.text = title
        textDesc.text = description
    }
}