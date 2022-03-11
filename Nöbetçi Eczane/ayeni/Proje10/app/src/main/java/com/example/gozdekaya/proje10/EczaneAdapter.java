package com.example.gozdekaya.proje10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
// lisviewde gösterme işlemlerini yapar


public class EczaneAdapter extends BaseAdapter{ // Base adapter dan extend alacak
    List<EczaneDetay> list; // liste tanımladık
    Context context;
    Activity activity;

    public EczaneAdapter(List<EczaneDetay> list, Context context,Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity; //telefon araması için
    }
 // aşağıda dönmesini istediğimiz veri tiplerini, verileri return ile döndürüyoruz
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.layout,parent,false); // layoutu convert view içinde tanımladık
        TextView eczaneIsim, eczaneAdres, eczaneTel, eczaneFax, eczaneAdresTarif; // arayüzde oluşturulanları tanımladık
        Button haritadaGoster, aramaYap;

        eczaneIsim = (TextView) convertView.findViewById(R.id.eczaneIsmi);
        eczaneAdres = (TextView) convertView.findViewById(R.id.eczaneAdres);
        eczaneTel = (TextView) convertView.findViewById(R.id.eczaneTelefon);
        eczaneFax = (TextView) convertView.findViewById(R.id.eczaneFax);
        eczaneAdresTarif = (TextView) convertView.findViewById(R.id.eczaneAdresTarif);
        haritadaGoster = (Button) convertView.findViewById(R.id.eczaneHaritadaGoster);
        aramaYap = (Button) convertView.findViewById(R.id.aramaYapButon);

        eczaneIsim.setText(list.get(position).getEczaneIsmi()); // buna göre ekrana gelen return edilen veriler arayüzdekii idlere gönderilioyr
        eczaneAdres.setText(list.get(position).getAdres());
        eczaneTel.setText(list.get(position).getTelefon());
        eczaneFax.setText(list.get(position).getFax());
        eczaneAdresTarif.setText(list.get(position).getTarif());
        aramaYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(); // arama işlemi için intent tanımladık
                intent.setData(Uri.parse("tel:"+list.get(position).getTelefon()));
                activity.startActivity(intent); // arama yapabilmesi için activity tanımladık

            }
        });
        return convertView;
    }
}
