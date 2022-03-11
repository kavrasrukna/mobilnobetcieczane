package com.example.gozdekaya.proje10;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity
{

    EditText et_ad,et_sifre;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        et_ad= (EditText) findViewById(R.id.et_ad);
        et_sifre= (EditText) findViewById(R.id.et_sifre);

    }


    public void butonaDokunuldu(View v)
    {

        //Kullanıcı adı ve parola alınıyor.
        String kullaniciadi = et_ad.getText().toString();
        String sifresi = et_sifre.getText().toString();

        //Buton olayları tanımlanıyor.
        switch (v.getId())
        {
            case R.id.btn_gir:

                if(sifresi.isEmpty()||kullaniciadi.isEmpty())
                {
                    Toast.makeText(Main2Activity.this, "Alanlar boş geçilemez!!!", Toast.LENGTH_SHORT).show();
                }

                Veritabani db=new Veritabani(this);
//Sadece kullanıcı adını alıyor ve bu kullanıcının şifresini kontrol ediyor.Sonuca göre mesaj verdiriyor.

                String kontrol=db.KaydiKontrolEt(kullaniciadi);


                if(sifresi.equals(kontrol))
                {
                    Toast.makeText(Main2Activity.this, "Giriş yapıldı...", Toast.LENGTH_SHORT).show();

                    //Şifre doğruysa "Hoşgeldiniz" Intentine geçiliyor.
                    Intent girisekrani=new Intent(getApplicationContext(),Hosgeldin.class);
                    startActivity(girisekrani);
                }


                else
                {
                    Toast.makeText(Main2Activity.this, "Hatalı kullanıcı adı veya şifre!!!", Toast.LENGTH_SHORT).show();
                }



                break;

            case R.id.btn_kayit:

                //Kayıt işlemi için kayıt ol Intentine geçiş yapılıyor.
                Intent intent=new Intent(getApplicationContext(),KayitOl.class);
                startActivity(intent);

            case R.id.btn_temizle:

                //Temizleme butonu için tanımlama yapılıyor.
                et_ad.getText().clear();
                et_sifre.getText().clear();

                break;

        }
    }
}
