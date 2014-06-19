package com.evologics.polaris.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evologics.polaris.R;
import com.evologics.polaris.model.Loan;



public class LoanAdapter extends ArrayAdapter<Loan>{

	    private final Context context;
	    private final ArrayList<Loan> LoansArrayList;
	    
	    
	    //Constructor
	    public LoanAdapter(Context context, ArrayList<Loan> LoansArrayList) {

	        super(context,R.layout.row,LoansArrayList);

	        this.context = context;
	        this.LoansArrayList = LoansArrayList;
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {

	        // 1. Create inflater 
	        LayoutInflater inflater = (LayoutInflater) context
	            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	        // 2. Get rowView from inflater
	        View rowView = inflater.inflate(R.layout.row, parent, false);

	        // 3. Get the necessary text views
	        TextView loan_name_text = (TextView) rowView.findViewById(R.id.loan_name);
	        TextView loan_dates_text = (TextView) rowView.findViewById(R.id.loan_dates);
	        TextView loan_details_text = (TextView) rowView.findViewById(R.id.loan_details);

	        // 4. Set the text for textView
	        loan_name_text.setText(LoansArrayList.get(position).getObjectName() + 
	        		" -> " + 
	        		LoansArrayList.get(position).getLoanOwner());
	        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
	        
	        String date_end = LoansArrayList.get(position).getDateEnd() == null ? "Sin fecha" : sdf.format(LoansArrayList.get(position).getDateEnd());
	        
	        loan_dates_text.setText(sdf.format(LoansArrayList.get(position).getDateStart()) 
	        		+ " ::: " + date_end);
	        
	        loan_details_text.setText(LoansArrayList.get(position).getNote());
	        

	        // 5. return rowView
	        return rowView;
	    }	
}
