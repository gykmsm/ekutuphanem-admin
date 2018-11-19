package com.merveakgormus.ekutuphanem_admin;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {
    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("kutuphanem");

    FirebaseStorage storage = FirebaseStorage.getInstance("gs://ekutuphanem-8154b.appspot.com");
    StorageReference storageRef = storage.getReference();


    private static final int PICK_IMAGE_REQUEST = 123;
    private Uri filePath;
    private String path;
    private FirebaseStorage fStorage;


    @BindView(R.id.imgOnkapak)
    ImageButton onkapak;
    @BindView(R.id.edtkitapadi)
    EditText kitapadi;
    @BindView(R.id.edtyazaradi)
    EditText yazaradi;
    @BindView(R.id.edtkapakturu)
    EditText kapakturu;
    @BindView(R.id.edturunaciklamasi)
    EditText urunaciklamasi;
    @BindView(R.id.edtfiyat)
    EditText fiyat;
    @BindView(R.id.edtsayfasayisi)
    EditText sayfasayisi;
    @BindView(R.id.btnGiris)
    Button btnGiris;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fStorage = FirebaseStorage.getInstance();


    }

    @OnClick(R.id.btnGiris)
    public void onViewClicked() {

String storageurl="images/"+filePath.getLastPathSegment();
        StorageReference Ref=storageRef.child(storageurl);
        UploadTask uploadTask=Ref.putFile(filePath);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
        String key = myRef.push().getKey();
        Book book = new Book(kitapadi.getText().toString(), yazaradi.getText().toString(), Integer.parseInt(sayfasayisi.getText().toString()), Double.parseDouble(fiyat.getText().toString()), kapakturu.getText().toString(), urunaciklamasi.getText().toString(),storageurl);
        myRef.child(key).setValue(book);
        Toast.makeText(this, path, Toast.LENGTH_SHORT).show();

        kitapadi.setText("");
        yazaradi.setText("");
        sayfasayisi.setText("");
        fiyat.setText("");
        urunaciklamasi.setText("");
        kapakturu.setText("");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            path=filePath.getPath().toString();

            try {
                Picasso.with(MainActivity.this).load(filePath).fit().centerCrop().into(onkapak);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnClick(R.id.imgOnkapak)
    public void setOnkapak() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Resim Seçiniz"), PICK_IMAGE_REQUEST);

    }
}
