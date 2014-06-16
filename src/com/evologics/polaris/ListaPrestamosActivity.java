package com.evologics.polaris;

import java.util.ArrayList;
import java.util.Date;

import com.evologics.polaris.model.Prestamo;
import com.evologics.polaris.util.PolarisUtil.ESTATUS_PRESTAMO;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListaPrestamosActivity extends Activity {
	
	ArrayList<Prestamo> lista = new ArrayList<Prestamo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_prestamos);
		
		//Actions
		populatePrestamos();
		populateListView();
	}

	/*
	 * This method should extract the JSON from the webservice
	 * and depending on what user is logged on, recover all the
	 * items, and send it to the arrayList
	 */

	private void populatePrestamos() {
		try{
		this.lista.add(new Prestamo("Objeto_1","Persona_1","Esta es una nota de ejemplo",new Date(),new Date(),"Categoria_prueba",ESTATUS_PRESTAMO.EN_TIEMPO));
		this.lista.add(new Prestamo("Objeto_2","Persona_2","Esta es una nota de ejemplo",new Date(),new Date(),"Categoria_prueba",ESTATUS_PRESTAMO.EN_TIEMPO));
		this.lista.add(new Prestamo("Objeto_3","Persona_3","Esta es una nota de ejemplo",new Date(),new Date(),"Categoria_prueba",ESTATUS_PRESTAMO.EN_TIEMPO));
		}catch(Exception e){
			Log.e("ERROR",e.getMessage());
		}
		
	}
	
	/*
	 * This method should take the created arrayList and put all the contents on the ListView
	 * for this activity
	 */
	private void populateListView() {
		ListView itemList = (ListView) findViewById(R.id.listView_prestamos);
		
		//Creating the adapter and passing the ArrayList as a parameter
		ArrayAdapter<Prestamo> arrayAdapter = new ArrayAdapter<Prestamo>(this,android.R.layout.two_line_list_item,this.lista);
		//Set the adapter
		itemList.setAdapter(arrayAdapter);
		
		//Register onClickListener to handle click events on each item
		itemList.setOnItemClickListener(new OnItemClickListener() {
			
			//Argument position gives the index of the current element selected
			@Override
			public void onItemClick(AdapterView<?> arg0, View c, int position,
					long arg3) {
				Prestamo selectedItem = lista.get(position);
				//Here we should call the other activity with the details of the current item
				
				//I'm calling a toast for testing purposes
				Toast.makeText(getApplicationContext(),"Item selected is" + selectedItem.getObjeto(),Toast.LENGTH_LONG);
			}
		});
	}
}
