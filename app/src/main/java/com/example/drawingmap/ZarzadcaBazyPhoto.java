package com.example.drawingmap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ZarzadcaBazyPhoto extends SQLiteOpenHelper{

	public ZarzadcaBazyPhoto(Context context) {
		super(context, "baza_photo.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d("TAG","PIERWSZE URUCHOMIENIE");
		db.execSQL(
				"create table photo(" +
				"nr integer primary key autoincrement," +
				"photo_id text," +
				"photo text);" +
				"");
}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
	}

	public void dodajPhoto(Obiekt_photo photo){
		Log.d("TAG","Dodaje photo");

		SQLiteDatabase db = getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		wartosci.put("photo_id", photo.getphoto_id());
		wartosci.put("photo", photo.getphoto());
		db.insertOrThrow("photo",null, wartosci);
	}

	public boolean sprawdzCzyIstniejeTabela(){
		SQLiteDatabase db = getWritableDatabase();
		String query="Select photo_id from photo limit 1";
		try{
			Cursor kursor =db.rawQuery(query,null);

	//	this.UsunWszystkieRekordy();
			Log.d("aa","SPRAWDZAM CZY ISTIENIEJE");
		Log.d("aa",Integer.toString(kursor.getCount()));
		if(kursor.getCount()==0){
			Log.d("Tabela","Brak tabeli");
			return false;
		}else{			Log.d("Tabela","Jest tabela");

			return true;}
		}catch (NullPointerException ne){
			Log.d("aa","NUL POINT EXCEPT");

			return false;
		}catch (SQLiteException se){
			Log.d("aa","SQL EXCEPT");

			return false;
		}
		catch(CursorIndexOutOfBoundsException ce){
			Log.d("aa","CURSOR OUT OF");
			return false;
		}
	}

	public String dajPhoto(String photo_id){
		String query="Select photo FROM photo where photo_id like '"+photo_id+"'";
		String photo =null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor kursor =db.rawQuery(query,null);
		while(kursor.moveToNext()){
			photo=kursor.getString(0);
		}
		return photo;
	}



	public void kasujKontakt(int id){
		SQLiteDatabase db = getWritableDatabase();
		String[] argumenty={""+id};
		db.delete("telefony", "nr=?", argumenty);
	}

	public void kasujObiekt(int id){
		SQLiteDatabase db = getWritableDatabase();
		String[] argumenty={""+id};
		db.delete("obiekty", "nr=?", argumenty);
	}
	
	
	public void aktualizujKontakt(Kontakt kontakt){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues wartosci = new ContentValues();
		wartosci.put("imie", kontakt.getImie());
		wartosci.put("nazwisko",kontakt.getNazwisko());
		wartosci.put("telefon", kontakt.getTelefon());
		String args[]={kontakt.getNr()+""};
		db.update("telefony", wartosci,"nr=?",args);
	}

	public List<Obiekt_Adres> dajWszystkieAdres(){
		List<Obiekt_Adres> adresy = new LinkedList<Obiekt_Adres>();
		Log.d("daj wszystkie","**********************");

		Log.d("daj wszystkie","Tu jest problem");
;

		Log.d("daj wszystkie","**********************");
		SQLiteDatabase db = getReadableDatabase();
		Log.d("daj wszystkie","**********************");

		Log.d("daj wszystkie","Tu jest problem 1");
		;

		Log.d("daj wszystkie","**********************");
		Cursor kursor =db.rawQuery("Select * from adresyGdansk",null);
		while(kursor.moveToNext()){
			Obiekt_Adres adres  = new Obiekt_Adres();
			//adres.setNr(kursor.getLong(0));
			adres.setUlica(kursor.getString(1));
			adres.setNumer(kursor.getString(2));
			adres.setLatitude(kursor.getString(3));
			adres.setLongitude(kursor.getString(4));

			Log.d("daj wszystkie","**********************");

			Log.d("daj wszystkie",kursor.getString(1));
			Log.d("daj wszystkie",kursor.getString(2));
			Log.d("daj wszystkie",kursor.getString(3));
			Log.d("daj wszystkie",kursor.getString(4));

			Log.d("daj wszystkie","**********************");
			adresy.add(adres);
		}
		return adresy;
	}



	public void UsunWszystkieRekordy(){
		String query="delete from adresyGdansk";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(query);
	}

	public ArrayList<String> dajNrMieszkania(String Ulica){
		String query="Select numer FROM adresyGdansk where ulica like '%"+Ulica+"%'order by  CAST(numer as Int)";//" '% +Ulica+"'%'";
		ArrayList<String> numery = new ArrayList<>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor kursor =db.rawQuery(query,null);
		while(kursor.moveToNext()){
			numery.add(kursor.getString(0));
		}
		return numery;
	}

	public String dajWspolrzedneMieszkania(String Ulica,String Mieszkanie){
		String query="Select latitude,longitude FROM adresyGdansk where ulica like '%"+Ulica+"%' and numer like '"+Mieszkanie+"'";
		String coordinates =null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor kursor =db.rawQuery(query,null);
		while(kursor.moveToNext()){
		coordinates=kursor.getString(0)+";"+kursor.getString(1);
		}
		return coordinates;
	}




	public List<Kontakt> dajWszystkie2(){
		List<Kontakt> kontakty = new LinkedList<Kontakt>();
		String[] kolumny={"nr","imie","nazwisko","telefon"};
		SQLiteDatabase db = getReadableDatabase();
		Cursor kursor =db.query("telefony",kolumny,null,null,null,null,null);	
		while(kursor.moveToNext()){			
			Kontakt kontakt = new Kontakt();
			kontakt.setNr(kursor.getLong(0));
			kontakt.setImie(kursor.getString(1));
			kontakt.setNazwisko(kursor.getString(2));
			kontakt.setTelefon(kursor.getString(3));
			kontakty.add(kontakt);



		}
		return kontakty;
	}
	public List<Obiekt> dajWszystkie(){
		List<Obiekt> obiekty = new LinkedList<Obiekt>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor kursor =db.rawQuery("Select * from obiekty",null);
		while(kursor.moveToNext()){
			Obiekt obiekt = new Obiekt();
			obiekt.setNr(kursor.getLong(0));
			obiekt.setParametr_id(kursor.getString(1));
			obiekt.setParametr(kursor.getString(2));
			obiekt.setPodparametr_id(kursor.getString(3));
			obiekt.setPodparametr(kursor.getString(4));

			Log.d("daj wszystkie","**********************");

			Log.d("daj wszystkie",kursor.getString(1));
			Log.d("daj wszystkie",kursor.getString(2));
			Log.d("daj wszystkie",kursor.getString(3));
			Log.d("daj wszystkie",kursor.getString(4));

			Log.d("daj wszystkie","**********************");
			obiekty.add(obiekt);
		}
		return obiekty;
	}
	
	public Kontakt dajKontakt(int nr){		
		Kontakt kontakt=new Kontakt();		
			SQLiteDatabase db = getReadableDatabase();
			String[] kolumny={"nr","imie","nazwisko","telefon"};
			String args[]={nr+""};
			Cursor kursor=db.query("telefony",kolumny," nr=?",args,null,null,null,null);
			if(kursor!=null){
				kursor.moveToFirst();
				kontakt.setNr(kursor.getLong(0));
				kontakt.setImie(kursor.getString(1));
				kontakt.setNazwisko(kursor.getString(2));
				kontakt.setTelefon(kursor.getString(3));
			}
		return kontakt;
	}
	
	public List<Kontakt> dajPoNazwisku(String nazwisko){
		List<Kontakt> kontakty = new LinkedList<Kontakt>();
		String[] kolumny={"nr","imie","nazwisko","telefon"};
		SQLiteDatabase db = getReadableDatabase();		
		Cursor kursor =db.rawQuery("select nr,imie,nazwisko,telefon from telefony where nazwisko='"
									+nazwisko+
								   "' order by imie asc", null);
		/*Alternatywne wywołanie metody rawQuery
		 * 
		 * Cursor kursor =db.rawQuery
		 * ("select nr,imie,nazwisko,telefon from telefony where nazwisko=?	order by imie asc", nazwi);		
		 * */
		while(kursor.moveToNext()){			
			Kontakt kontakt = new Kontakt();
			kontakt.setNr(kursor.getLong(0));
			kontakt.setImie(kursor.getString(1));
			kontakt.setNazwisko(kursor.getString(2));
			kontakt.setTelefon(kursor.getString(3));
			kontakty.add(kontakt);
		}
		return kontakty;
	}

	public List<Obiekt> dajPoIdParametru(String param_id,String podparam_id){

		List<Obiekt> obiekty = new LinkedList<Obiekt>();
		String[] kolumny={"nr","parametr","podparametr","parametr_id","podparametr_id"};		SQLiteDatabase db = getReadableDatabase();

		Cursor kursor =db.rawQuery("select nr,parametr,podparametr from obiekty where parametr_id=1 and podparametr_id=1", null);

		while(kursor.moveToNext()){
			Obiekt obiekt = new Obiekt();
			obiekt.setNr(kursor.getLong(0));
			obiekt.setParametr(kursor.getString(1));
			obiekt.setPodparametr(kursor.getString(2));
			obiekty.add(obiekt);
		}
		return obiekty;
	}

}
