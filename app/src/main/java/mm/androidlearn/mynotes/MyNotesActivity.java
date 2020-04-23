package mm.androidlearn.mynotes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import adapter.NotesAdapter;
import clicklisteners.ItemClickListener;
import model.Notes;

public class MyNotesActivity extends AppCompatActivity {
    FloatingActionButton actionButton;
    TextView textviewTitle,textviewDescription;
    String TAG ="MyNotesActivity";
    SharedPreferences sharedPreferences;
    RecyclerView recyclerviewNotes;
    ArrayList<Notes> notesList = new ArrayList<Notes>();
    String fullname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);
        setUpSharedPreference();
        bindViews();
        getIntentData();


        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,"Clicked on Floating action button");
                setUpDialogBox();
            }
        });

        getSupportActionBar().setTitle(fullname);
    }

    private void setUpSharedPreference() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREF_NAME,MODE_PRIVATE);
        recyclerviewNotes = findViewById(R.id.recyclerNotes);
    }

    void bindViews(){
        actionButton = findViewById(R.id.fabAddNotes);
       /* textviewTitle =  findViewById(R.id.textviewTitle);
        textviewDescription = findViewById(R.id.textviewDescription);*/
    }

    void getIntentData(){
        Intent intent = getIntent();
        fullname = intent.getStringExtra(AppConstant.FULLNAME);
        if(TextUtils.isEmpty(fullname)){
            fullname = sharedPreferences.getString(PrefConstant.FULL_NAME,"");
        }
    }

    private void setUpDialogBox() {
        View view = LayoutInflater.from(MyNotesActivity.this).inflate(R.layout.add_notes_dialog,null);
        final EditText editextTitle = view.findViewById(R.id.editTextTitle);
        final EditText edittextDesc = view.findViewById(R.id.editTextDescription);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).setCancelable(false).create();

         view.findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          /* textviewTitle.setText(editextTitle.getText().toString());
           textviewDescription.setText(edittextDesc.getText().toString());*/
           String titleText = editextTitle.getText().toString();
           String descriptionText = edittextDesc.getText().toString();
            if(!TextUtils.isEmpty(titleText)&&!TextUtils.isEmpty(descriptionText)) {
                Notes notes = new Notes();
                notes.setTitle(titleText);
                notes.setDescription(descriptionText);
                notesList.add(notes);
            }
            else{
                Toast.makeText(MyNotesActivity.this,"Title or Description cant be empty",Toast.LENGTH_SHORT).show();
            }
           setUpRecyclerview();

           Log.d(TAG, String.valueOf(notesList.size()));
           dialog.hide();
        }
         });
        dialog.show();
    }

    public void setUpRecyclerview(){
        NotesAdapter notesAdapter = new NotesAdapter(notesList, new ItemClickListener() {
            @Override
            public void onClick(Notes notes) {
                Log.d(TAG,notes.getTitle());
                Log.d(TAG,notes.getDescription());
                Intent intent = new Intent(MyNotesActivity.this,DetailsActivity.class);
                intent.putExtra(AppConstant.TITLE,notes.getTitle());
                intent.putExtra(AppConstant.DESCRIPTION,notes.getDescription());
                startActivity(intent);

            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyNotesActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerviewNotes.setLayoutManager(linearLayoutManager);
        recyclerviewNotes.setAdapter(notesAdapter);
    }
}
