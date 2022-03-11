package com.example.gozdekaya.proje10;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class Veritabani extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="kullanicilar_db";

    private static final String TABLE_NAME ="kullanici_bilgi";

    private final String SUTUN_ID = "kullanici_id";
    private final String SUTUN_KULLANICI_ADI = "kullanici_adi";
    private final String SUTUN_EMAIL="kullanici_email";
    private final String SUTUN_SIFRE = "kullanici_sifre";


    public Veritabani(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    //***KOLAYLIK OLMASI AÇISINDAN KOMUTLAR ÖNCE YAZILDI.DİREKT METODLAR İÇİNDE DE YAZILABİLİRDİ.


    //Tablo oluşturuldu.(SQL komutu)
    String TABLO = "CREATE TABLE " + TABLE_NAME +
            "(" + SUTUN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SUTUN_KULLANICI_ADI + " TEXT, " +
            SUTUN_EMAIL + " TEXT, "+
            SUTUN_SIFRE + " TEXT " + ")";
    //"INTEGER PRIMARY KEY AUTOINCREMENT," otomatik olarak artması içn oluşturulan uniqe key


    //Tablo oluşturuldu.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(TABLO);
    }


    //Tablo güncelleme için.(SQL komutu)
    String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    //Eski versiyondan yeni versiyona güncelleme için kullanılır.Tabloyu silip yeniden oluşturuyor böylece güncelleniyor.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);

    }



    public long KayıtEkle(Bilgiler k1)
    {

        SQLiteDatabase db=this.getWritableDatabase();

        //Content value cv'nin değerleri Bilgiler.java kısmındanki propertie kullanımı ile ilgili kısımlara aktarılıyor.
        ContentValues cv=new ContentValues();
        cv.put(SUTUN_KULLANICI_ADI,k1.getIsım());
        cv.put(SUTUN_EMAIL,k1.getEmail());
        cv.put(SUTUN_SIFRE,k1.getSifre());


        //insert() geri dönüş tipi long tur.Geriye döndürdüğü değer o satırın id değeridir.
        //eğer hata oluşuyorsa id değeri -1 döner.hata oluşup oluşmadığını bir değişken belirleyek değerinden anlayabiliriz.

        long kontrol=db.insert(TABLE_NAME,null,cv);


        //long kontrol=db.insertOrThrow(TABLE_NAME,"",cv); olabilirdi...


        //Son olarak veritabanını kapatmalıyız.
        db.close();

        return kontrol;

    }


//Kullanıcı adını alarak bu kullanıcı adının şifresini geri döndürüyor Giriş ekranından girilen şifreyle
// eşleşip eşleşmediğine bakıyor.

    public  String KaydiKontrolEt(String gelenad)
    {
        SQLiteDatabase db=this.getReadableDatabase();

        Cursor cursor=db.query(TABLE_NAME, null, SUTUN_KULLANICI_ADI+"=?", new String[]{gelenad}, null, null, null);

        //Kullanıcı ismi yoksa hata veriliyor.
        if(cursor.getCount()<1)
        {
            cursor.close();
            return "Kayıt Bulunamadı";
        }

        cursor.moveToFirst();
        String password= cursor.getString(cursor.getColumnIndex(SUTUN_SIFRE));
        cursor.close();

        return password;
    }

}
