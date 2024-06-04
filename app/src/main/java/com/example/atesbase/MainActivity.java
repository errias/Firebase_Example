package com.example.atesbase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText inputID, inputName;
    private Button btnSave, btnRead, btnDelete, btnPush;
    private DatabaseReference ref;
    private DatabaseReference refPush;
    private TextView textViewID, textViewName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputID =findViewById(R.id.inputID);
        inputName =findViewById(R.id.inputName);

        btnSave=findViewById(R.id.btnSave);
        btnDelete=findViewById(R.id.btnDelete);
        btnRead=findViewById(R.id.btnRead);
        btnPush=findViewById(R.id.btnPush);

        textViewID =findViewById(R.id.textViewID);
        textViewName =findViewById(R.id.textViewName);

        refPush = FirebaseDatabase.getInstance().getReference().child("Push_Kullanıcılar");
        ref = FirebaseDatabase.getInstance().getReference().child("Kullanıcılar");

        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ID = Integer.parseInt(inputID.getText().toString());
                String Name = inputName.getText().toString();
                refPush.push().setValue(ID);
                refPush.push().setValue(Name);
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                            Object id = map.get("ID");
                            String name = (String) map.get("Name");
                            textViewID.setText("ID: "+id);
                            textViewName.setText("Name: "+name);
                            //String data = dataSnapshot.getValue().toString();
                            //textView.setText(data);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(inputID.getText().toString());
                String name = inputName.getText().toString();

                HashMap hashMap=new HashMap();
                hashMap.put("ID",id);
                hashMap.put("Name",name);
                ref./*child("Kullanıcı1").*/setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "Data Gönderim İşlemi Başarılı", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Data Gönderim İşlemi Başarısız", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref.getParent().removeValue();
            }
        });
    }
}