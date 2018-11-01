package com.freddomoura.android.babytimer.feature;

import java.text.NumberFormat;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.Preference.OnPreferenceClickListener;
import android.widget.TimePicker;

import com.freddomoura.android.babytimer.feature.dialogs.CronoPickerDialog;

public class Preferences extends PreferenceActivity
		implements
			OnPreferenceClickListener {

	private static final int TIME_DIALOG_ID01 = 1;
	private static final int TIME_DIALOG_ID02 = 2;
	private static final int TIME_DIALOG_ID03 = 3;
	private static final int TIME_DIALOG_ID04 = 4;
	private static final int TIME_DIALOG_ID05 = 5;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

		Preference clock01 = findPreference("allarm01_clock");
		clock01.setOnPreferenceClickListener(this);

		Preference clock02 = findPreference("allarm02_clock");
		clock02.setOnPreferenceClickListener(this);

		Preference clock03 = findPreference("allarm03_clock");
		clock03.setOnPreferenceClickListener(this);

		Preference clock04 = findPreference("allarm04_clock");
		clock04.setOnPreferenceClickListener(this);

		Preference clock05 = findPreference("allarm05_clock");
		clock05.setOnPreferenceClickListener(this);

	}

	@Override
	public boolean onPreferenceClick(Preference pPreference) {
		if ("allarm01_clock".equalsIgnoreCase(pPreference.getKey())) {
			this.showDialog(TIME_DIALOG_ID01);
		}
		if ("allarm02_clock".equalsIgnoreCase(pPreference.getKey())) {
			this.showDialog(TIME_DIALOG_ID02);
		}
		if ("allarm03_clock".equalsIgnoreCase(pPreference.getKey())) {
			this.showDialog(TIME_DIALOG_ID03);
		}
		if ("allarm04_clock".equalsIgnoreCase(pPreference.getKey())) {
			this.showDialog(TIME_DIALOG_ID04);
		}
		if ("allarm05_clock".equalsIgnoreCase(pPreference.getKey())) {
			this.showDialog(TIME_DIALOG_ID05);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMinimumIntegerDigits(2);
		switch (id) {
			case TIME_DIALOG_ID01 : {
				CronoPickerDialog dialog1 = new CronoPickerDialog(this,
						new CronoPickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int pHourOfDay, int pMinute) {
								Preference clock01 = findPreference("allarm01_clock");
								SharedPreferences.Editor editor = clock01
										.getEditor();
								editor.putInt("allarm01_hour", pHourOfDay);
								editor.putInt("allarm01_minute", pMinute);
								editor.apply();

							}
						}, prefs.getInt("allarm01_hour", 0), prefs.getInt(
								"allarm01_minute", 0), true);

				return dialog1;
			}
			case TIME_DIALOG_ID02 : {
				return new CronoPickerDialog(this,
						new CronoPickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int pHourOfDay, int pMinute) {
								Preference clock02 = findPreference("allarm02_clock");
								SharedPreferences.Editor editor = clock02
										.getEditor();
								editor.putInt("allarm02_hour", pHourOfDay);
								editor.putInt("allarm02_minute", pMinute);
								editor.apply();
							}
						}, prefs.getInt("allarm02_hour", 0), prefs.getInt(
								"allarm02_minute", 0), true);
			}
			case TIME_DIALOG_ID03 : {
				return new CronoPickerDialog(this,
						new CronoPickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int pHourOfDay, int pMinute) {
								Preference clock03 = findPreference("allarm03_clock");
								SharedPreferences.Editor editor = clock03
										.getEditor();
								editor.putInt("allarm03_hour", pHourOfDay);
								editor.putInt("allarm03_minute", pMinute);
								editor.apply();
							}
						}, prefs.getInt("allarm03_hour", 0), prefs.getInt(
								"allarm03_minute", 0), true);
			}
			case TIME_DIALOG_ID04 : {
				return new CronoPickerDialog(this,
						new CronoPickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int pHourOfDay, int pMinute) {
								Preference clock04 = findPreference("allarm04_clock");
								SharedPreferences.Editor editor = clock04
										.getEditor();
								editor.putInt("allarm04_hour", pHourOfDay);
								editor.putInt("allarm04_minute", pMinute);
								editor.apply();
							}
						}, prefs.getInt("allarm04_hour", 0), prefs.getInt(
								"allarm04_minute", 0), true);
			}
			case TIME_DIALOG_ID05 : {
				return new CronoPickerDialog(this,
						new CronoPickerDialog.OnTimeSetListener() {

							public void onTimeSet(TimePicker view,
									int pHourOfDay, int pMinute) {
								Preference clock05 = findPreference("allarm05_clock");
								SharedPreferences.Editor editor = clock05
										.getEditor();
								editor.putInt("allarm05_hour", pHourOfDay);
								editor.putInt("allarm05_minute", pMinute);
								editor.apply();
							}
						}, prefs.getInt("allarm05_hour", 0), prefs.getInt(
								"allarm05_minute", 0), true);
			}
		}
		return null;
	}
}
