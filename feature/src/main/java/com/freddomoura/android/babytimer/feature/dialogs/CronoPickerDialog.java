package com.freddomoura.android.babytimer.feature.dialogs;

import java.text.NumberFormat;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TimePicker;

public class CronoPickerDialog extends TimePickerDialog
		implements
			DialogInterface.OnClickListener,
			TimePicker.OnTimeChangedListener {

	private NumberFormat nf = NumberFormat.getNumberInstance();

	public CronoPickerDialog(Context pContext, OnTimeSetListener pCallBack,
			int pHourOfDay, int pMinute, boolean pIs24HourView) {
		super(pContext, pCallBack, pHourOfDay, pMinute, pIs24HourView);
		nf.setMinimumIntegerDigits(2);
		this.setTitle(nf.format(pHourOfDay) + ":" + nf.format(pMinute));
	}

	public CronoPickerDialog(Context pContext, int pTheme,
			OnTimeSetListener pCallBack, int pHourOfDay, int pMinute,
			boolean pIs24HourView) {
		super(pContext, pTheme, pCallBack, pHourOfDay, pMinute, pIs24HourView);
		nf.setMinimumIntegerDigits(2);
		this.setTitle(nf.format(pHourOfDay) + ":" + nf.format(pMinute));
	}

	@Override
	public void onClick(DialogInterface pDialog, int pWhich) {
		super.onClick(pDialog, pWhich);
	}

	@Override
	public void onTimeChanged(TimePicker pView, int pHourOfDay, int pMinute) {
		super.onTimeChanged(pView, pHourOfDay, pMinute);
		this.setTitle(nf.format(pHourOfDay) + ":" + nf.format(pMinute));
	}

}
