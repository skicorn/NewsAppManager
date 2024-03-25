package com.example.newsappmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsappmanager.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.List;

public class Show_User_Info_Activity extends AppCompatActivity {
    TextView user_UID;
    TextView userEmail, userPassword,UserID;
    Button deleteUser;
    ImageButton back;
    //Data
    User user;
    Intent intent;
    FirebaseAuth auth;
    AlertDialog.Builder alertDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_info);
        //find view
        alertDialog = new AlertDialog.Builder(Show_User_Info_Activity.this);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        deleteUser = findViewById(R.id.user_delete);
        back = findViewById(R.id.user_back);
        intent = getIntent();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Show_User_Info_Activity.this,List_User_Activity.class);
                startActivity(i);
            }
        });
        if (intent.hasExtra("userdata")){
            user = (User) intent.getSerializableExtra("userdata");
            String UD =user.getUID();
            userPassword.setText(user.getPassword());
            userEmail.setText(user.getEmail());
            String mail = user.getEmail();
            String pass = user.getPassword();
            auth=FirebaseAuth.getInstance();
            auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                }
            });
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
            String Email = user.getEmail();}
            // Sử dụng địa chỉ email ở đây
            // Ví dụ: Hiển thị địa chỉ email trong TextView


        }
        //delete user
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDeleteProfile();
            }
        });
    }

    private void onClickDeleteProfile() {

        FirebaseUser UserPoint = FirebaseAuth.getInstance().getCurrentUser();
        String MailChangeEncode = UtilsEncode.encodeEmailToNumber(user.getEmail());
        String UID = "User/"+MailChangeEncode;
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference nodeRef = databaseRef.child(UID);
        alertDialog.setTitle("ARE YOU SURE TO DELETE?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UserPoint != null) {
                    UserPoint.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Show_User_Info_Activity.this, "Xóa User thành công", Toast.LENGTH_SHORT).show();
                                        nodeRef.removeValue();
                                        Intent i = new Intent(Show_User_Info_Activity.this,List_User_Activity.class);
                                        startActivity(i);
                                        FirebaseAuth.getInstance().signOut();

                                    } else {
                                        Toast.makeText(Show_User_Info_Activity.this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Show_User_Info_Activity.this, "DELETE CANCEL", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }
}