package com.evologics.polaris.util;

import java.util.Calendar;

import com.evologics.polaris.NewLoanActivity;
import com.evologics.polaris.util.PolarisUtil.DateType;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;



public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	public static DateType type = DateType.STARTDATE;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		if( type == DateType.STARTDATE )
			NewLoanActivity.startDate.setText( dayOfMonth + "-" + (monthOfYear+1) + "-" + year );
		else
			NewLoanActivity.endDate.setText( dayOfMonth + "-" + (monthOfYear+1) + "-" + year );
		
	}

	

}
