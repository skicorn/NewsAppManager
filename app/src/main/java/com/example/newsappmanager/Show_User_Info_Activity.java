package com.example.newsappmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsappmanager.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Show_User_Info_Activity extends AppCompatActivity {
    TextView user_UID;
    EditText userEmail, userPassword,UserID;
    Button updateUser, deleteUser;
    ImageButton back;
    //Data
    User user;
    Intent intent;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_info);
        //find view
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        updateUser = findViewById(R.id.user_update);
        deleteUser = findViewById(R.id.user_delete);
        back = findViewById(R.id.user_back);
        intent = getIntent();
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
                    Toast.makeText(Show_User_Info_Activity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                }
            });
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String Email = user.getEmail();
            // Sử dụng địa chỉ email ở đây
            // Ví dụ: Hiển thị địa chỉ email trong TextView
            Toast.makeText(Show_User_Info_Activity.this, Email, Toast.LENGTH_SHORT).show();

        }
        updateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });
    }

    private void onClickUpdateProfile() {
        FirebaseUser UserPoint = FirebaseAuth.getInstance().getCurrentUser();
        String MailUser=userEmail.getText().toString();
        String PassUser=userPassword.getText().toString();
        Toast.makeText(Show_User_Info_Activity.this, MailUser+PassUser, Toast.LENGTH_SHORT).show();
        UserPoint.updateEmail(MailUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    FirebaseAuth.getInstance().signOut();
                    auth.signInWithEmailAndPassword(MailUser,PassUser);
                    Toast.makeText(Show_User_Info_Activity.this, "Update thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}