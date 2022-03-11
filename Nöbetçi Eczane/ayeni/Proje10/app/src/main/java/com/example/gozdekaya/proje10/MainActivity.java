package com.example.gozdekaya.proje10;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String tokentxt;
    WebView webview;
    Spinner spinner; // ilceler için spinner oluşturuyoruz
    org.jsoup.nodes.Document document;
    List<EczaneDetay> eczaneList; // Eczanedetay tipinde bir list oluşturuyoruz bunu handlerda ve Eczane Adapterde verileri get etmek için kullanıyoruz
    EczaneAdapter eczaneAdapter;
    ListView listView; // eczanelerin listelemesi için bir listwiev tanımladık
    Button listeleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        webview = new WebView(getApplicationContext()); // bir wenview oluşturuyoruz
        webview.getSettings().setJavaScriptEnabled(true); // Enable aktif etmemiz gerekiyor, JavaScript ayarlarını aktif ediyoruz.
        // bir web ayarı dönüyor bize
        webview.addJavascriptInterface(new JsBridge(), "Android"); // java interface i veriyoruz, hangi classta JSB yapıyorsak ekliyoruz
        // Classı javascript interface i olarak wenview' a verdik


        this.getToken();
        final String ilceler[] = {"Adalar", "Arnavutköy", "Ataşehir", "Avcılar", "Bağcılar", "Bahçelievler", "Bakırköy","Başakşehir", "Bayrampaşa", "Beşiktaş", "Beykoz","Beylikdüzü", "Beyoğlu", "Büyükçekmece", "Çatalca", "Çekmeköy", "Esenler", "Esenyurt", "Eyüp", "Fatih", "Gaziosmanpaşa", "Güngören", "Kadıköy", "Kağıthane", "Kartal", "Küçükçekmece", "Maltepe", "Pendik", "Sancaktepe", "Sarıyer", "Şile", "Silivri", "Şişli", "Sultanbeyli", "Sultangazi", "Tuzla", "Ümraniye", "Üsküdar", "Zeytinburnu"};
        final int ilceid[] = {1, 33, 34, 2, 3, 4, 5, 35, 6, 7, 8, 36, 9, 10, 11, 37, 13, 38, 14, 15, 16, 17, 18, 19, 20, 21,22,23,39,24,27,25,28,26,40,29,30,31,32};
        //Spinner atanımlamak için dizilere tanımlama yaptık
        spinner = (Spinner) findViewById(R.id.ilceSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ilceler);
        // Spinner işlemleri
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        listeleButton = (Button) findViewById(R.id.listeleButon);
        listeleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = spinner.getSelectedItem().toString();
                int index = Integer.parseInt(String.valueOf(java.util.Arrays.asList(ilceler).indexOf(item)));
                int id = ilceid[index];
                getEczane(String.valueOf(id)); // ilce idlerini spinnera tanımladık ve bunu butona gönderdik
            }
        });
        /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //int index = Integer.parseInt(String.valueOf(java.util.Arrays.asList(ilceler).indexOf(spinner.getSelectedItem().toString())));
                //int gelenId = ilceid[index];
                //getEczane(String.valueOf(gelenId));

            }
*/


    }

    public void getEczane(String id) // bir butona tıkladıktan sonra eczaneleri listelemesini sağlayacak kod kısmı
            // id parametre olarak alıyoruz

    {
        webview.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         view.loadUrl("javascript:window.Android.htmlEczaneDetay("
                                                 + "'<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
        // sayfanın dönen kaynağında yer alan eczane listesini idler ile pars etme
                                         //html den al Eczane Detaya aktar-- htmlEczaneDetay

                                     }
                                 }
        );
        webview.loadUrl("http://apps.istanbulsaglik.gov.tr/Eczane/nobetci?id=" + id + "&token=" + tokentxt);
        // id parametre olarak alınıyor
    }

    // webview'ın token almasını sağlıyoruz
    public void getToken() {
        webview.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageFinished(WebView view, String url) { // webview tanımladık
                                         super.onPageFinished(view, url); // işlem bittikten sonra napılacak
                                         view.loadUrl("javascript:window.Android.htmlContentForToken("
                                                 + "'<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                                         //sayfanın dönen kaynağı ile sadece token'ı pars etme işlemi
                                         // interface gidip tokenı alıyor
                                         // html tagları arasından token alacağız, bir html ifadesi yazdık
                                     }

                                 }

        );
        webview.loadUrl("http://apps.istanbulsaglik.gov.tr/Eczane"); // hani siteden token alacağız onu yazıyoruz, aslında eczaneleri çekiyoruz


    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg); // arkaplan işlemlerini yapacak handler
            if (msg.what == 1) // gel tokenı al :). tokenı alıp almadığını garantiliyoruz.
            {
                tokentxt = (String) msg.obj;
            } else if (msg.what == 2) { // id ve token birleştirmesi
                        // gelen sayfa kodunu pars et
                Eczane ec = parseHtml((String) msg.obj);

                eczaneList = ec.getEczaneDetay();
                eczaneAdapter = new EczaneAdapter(eczaneList, MainActivity.this, MainActivity.this); //eczane listesini tanımladık
                listView.setAdapter(eczaneAdapter); // idsi gelen eczane listelenecek böylece
            }
        }
    };

    private Eczane parseHtml(String htmlkaynak) { // htmlden gelen kaynağı aldık, JAvascript ile almıştık
        document = Jsoup.parse(htmlkaynak); //  pars edilen kaynağı bir document elemanına atıyoruz
        Elements table = document.select("table.ilce-nobet-detay"); // dönen kaynak içerisinden table ' ıalıyoruz
        // ilgili etiketi alıyoruz
        Elements ilceDetay = table.select("caption>b"); //ilcedetay ı yazdırmak için caption içini alıyoruz
                                                        // html kaynağında ilçe adı ve tarih bölümü caption içerisinde yer alıyor
        Eczane eczane = new Eczane(); // eczane classına ait bir nesne oluşturduk
        eczane.setTarih(ilceDetay.get(0).text()); //0. indeks tarihi basarken, 1. indeks de ilce ismini basıyor
                                                    //ilgili ilçenin ismini set ettik
        eczane.setIlceIsmi(ilceDetay.get(1).text());


        Elements eczaneDetayElement = document.select("table.nobecti-eczane"); // ilgili table'a ait kaynağı alıyoruz ve html akaynağındaki
                                                                                        // bodyler gidiyor
        //Kaç tane eczane bulunuyorsa o sayıda tablo dönüyor
        // Eczaneye ait olan detayları set etmiş olduk
        // her tablo bir eczane detayını barındırıyor
        List<EczaneDetay> eczaneDetayList = new ArrayList<>(); // Eczane detay her döndüğünde null değilse bu listeye atansın
        // kaç eczane varsa detaylarıburaya gelecek
        for (Element el : eczaneDetayElement) {

            EczaneDetay eczaneDetay = getEczaneDetay(el); // kaç tane eczane varsa eczaneler eklendi
            //her gelen elemanı döndüreceğiz, elemente göndereceğiz ordan dönecek ve set edilecek
            eczaneDetay.toString();
            if (eczaneDetay != null) { // baita eczane detay null olsun ve ondan sonra dolsun ve set edilsin

                eczaneDetayList.add(eczaneDetay); // eczanler sırayla gelip bilgileri eklenecek
            }
        }
        eczane.setEczaneDetay(eczaneDetayList);
        return eczane;
    //Mantığı;
         //önce eczane geliyor üstkısımda bulunan  kısımdan pars ediliyor ve get eczane detaya gönderiliyor ger/set işlemleri yapılıyor
        // eğer boş değilse üst kısımda bulunan list' e ekleniyor
    }

    public EczaneDetay getEczaneDetay(org.jsoup.nodes.Element el) { //eczene detay bilgilerine bu şekilde  ulaşıyoruz
        // bir element alıp pars işlemleri yapıp gerid öndürecek

        String fax = "", tel = "", adres = "", adresTarif = "";
        EczaneDetay eczaneDetay = new EczaneDetay();

        Elements eczaneIsmiTag = el.select("thead"); // eczane ismi hmtl de theadların içinde
        String eczaneIsmi = eczaneIsmiTag.select("div").attr("title"); // Eczane ismini htmlden pars edip çekmek için htmlde bulunan div içerisindeki title'ı burda çekiyoruz

        eczaneDetay.setEczaneIsmi(eczaneIsmi);

        Elements trTags = el.select("tbody>tr"); // bilgiler tbody içerisinde olduğu için onu da element olarak atayıp
        // içinden bilgileri çekiyoruz
        Elements adresTags = trTags.select("tr#adres"); // html de bulunan trlerin içinden adres verisini alıyor
        adres = adresTags.select("label").get(1).text();
        eczaneDetay.setAdres(adres);


        Elements telTags = trTags.select("tr#Tel"); // html de bulunan trlerin içinden tel verisini alıyor
        tel = telTags.select("label").get(1).text();
        eczaneDetay.setTelefon(tel);

// tablo için id olarak belirilen veriyi biz diyez işaretiyle tanımlıyoruz
        Element faxTags = trTags.get(2); // html de bulunan trlerin içinden fax verisini alıyor
        fax = faxTags.select("label").get(1).text(); // 1. indexsinde veri var numara lar 1. indexte
        if (!fax.equals("")) {
            eczaneDetay.setFax(fax); // eğer fax alanı boş değilse numarayı eczane detaya gönder ve setle

        }


        Element adresTarifTags = trTags.get(3);// html de bulunan trlerin içinden adres verisini alıyor
        adresTarif = adresTarifTags.select("label").get(1).text();
        if (!adresTarif.equals("")) {
            eczaneDetay.setTarif(adresTarif); // eğer fax alanı boş değilse numarayı eczane detaya gönder ve setle

        }


        return eczaneDetay;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void kayit(View view) {
        Intent girisekrani=new Intent(getApplicationContext(),KayitOl.class);
        startActivity(girisekrani);
        finish();
    }

    public void gir(View view) {
        Intent girisekrani=new Intent(getApplicationContext(),Main2Activity.class);
        startActivity(girisekrani);
        finish();
    }

    public class JsBridge extends MainActivity { //maindeki elemanlara foksiyonlara ulaşmak için extend alıyoruz
                                                // tokenlara ulaşmak için Java köprüleri kuruyoruz

        @JavascriptInterface //interfacele metot oluşturuyoruz
        public void htmlContentForToken(String str) { // bir string değer alacak

            String token[] = str.split("token"); //bir string dizisi döndürür
                                                        // htmlde bulunan slaş, iki nokta gibi üyeleri yok ediyorus ki doğrudan tokena ulaşalım
            //token split ettiğimizde javascrşpt köprüleri ile sitede bulunan tüm verileri çekebildik
            if (token.length > 1) // birden büyükse elimizde veri var
            {
                String token2[] = token[1].split(Pattern.quote("}")); // Token ile birlikte gelen süslü parantezleri kaldırıyoruz
                //Split fonksiyonu ile bir string dizisi döner
                tokentxt = token2[0].replaceAll(" ", "").replaceAll(":", "").replaceAll("\"", "");
                // replace All ile boşluk, :, ve slaş işaretlerini siliyoruz, bu karakterleri boş bir ifadeye dönüştürüyoruz
                // böylece doğrudan token adresimize ulaşabiliyoruz
                Message message = new Message(); // bir message nesnesiyle token ve id göndereceğiz
                message.what = 1; // token oluştur
                message.obj = tokentxt; // token gönder
                handler.sendMessage(message);// token oluştur ve html i pars et
            }
        }

        @JavascriptInterface
        public void htmlEczaneDetay(String str) {
            Log.i("cevapp", str);
            Message message = new Message(); // handlera gönderme yapacağız
            message.what = 2; // idleri çekme işlemi
            message.obj = str;
            handler.sendMessage(message);
        }
    }
}




