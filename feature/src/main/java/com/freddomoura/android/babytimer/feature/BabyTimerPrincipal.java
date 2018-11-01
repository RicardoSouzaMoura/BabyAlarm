package com.freddomoura.android.babytimer.feature;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.freddomoura.android.babytimer.feature.entidades.BabyAlarm;
import com.freddomoura.android.babytimer.feature.services.ChronoAlarmService;
import com.freddomoura.android.babytimer.feature.services.IChronoAlarmBinder;

public class BabyTimerPrincipal extends AppCompatActivity implements OnClickListener {

	// alarms
	private BabyAlarm alarm01;
	private BabyAlarm alarm02;
	private BabyAlarm alarm03;
	private BabyAlarm alarm04;
	private BabyAlarm alarm05;

	// counters
	private MyCountDownTimer myCountDown01;
	private MyCountDownTimer myCountDown02;
	private MyCountDownTimer myCountDown03;
	private MyCountDownTimer myCountDown04;
	private MyCountDownTimer myCountDown05;

	private IChronoAlarmBinder alarmBinder;

	private static boolean firstTimeStart = true;

	private ServiceConnection conn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
		initLinhaAlarm();
		firstTimeStart = true;

	}

	private void initLinhaAlarm() {
		RelativeLayout linhaAlarm01 = this
				.findViewById(R.id.alarm01);
		linhaAlarm01.setOnClickListener(this);
		RelativeLayout linhaAlarm02 =  this
				.findViewById(R.id.alarm02);
		linhaAlarm02.setOnClickListener(this);
		RelativeLayout linhaAlarm03 = this
				.findViewById(R.id.alarm03);
		linhaAlarm03.setOnClickListener(this);
		RelativeLayout linhaAlarm04 =  this
				.findViewById(R.id.alarm04);
		linhaAlarm04.setOnClickListener(this);
		RelativeLayout linhaAlarm05 =  this
				.findViewById(R.id.alarm05);
		linhaAlarm05.setOnClickListener(this);
	}

	private void checkAlarmOnOff(BabyAlarm pAlarm, boolean pWasOn,
			long pOldTimeAlarm) {

		boolean timeAlarmChange = pAlarm.getAlarmInMiliseconds() != pOldTimeAlarm;

		if (!pWasOn && pAlarm.isOn()) {
			if (pAlarm.getAlarmInMiliseconds() > 0) {
				long timeInitial = initInitialTime(pAlarm.getId());
				initCounter(pAlarm, timeInitial);
				alarmBinder.restartAlarm(pAlarm);
			}
		} else if (pWasOn && !pAlarm.isOn()) {
			stopCounter(pAlarm.getId());
			alarmBinder.stopAlarm(pAlarm);
		} else if (pWasOn && pAlarm.isOn() && timeAlarmChange) {
			if (pAlarm.getAlarmInMiliseconds() > 0) {
				long timeInitial = initInitialTime(pAlarm.getId());
				initCounter(pAlarm, timeInitial);
				alarmBinder.restartAlarm(pAlarm);
			} else {
				stopCounter(pAlarm.getId());
				alarmBinder.stopAlarm(pAlarm);
			}
		} else if (pWasOn && pAlarm.isOn() && !timeAlarmChange) {
			if (pAlarm.getAlarmInMiliseconds() > 0) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(getBaseContext());
				long timeInitial = prefs.getLong("allarm0" + pAlarm.getId()
						+ "_timeInitial", 0);
				initCounter(pAlarm, timeInitial);
			} 
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		if (firstTimeStart) {
			if (ChronoAlarmService.on) {
				conn = new ServiceConnection() {
					@Override
					public void onServiceConnected(ComponentName name,
							IBinder service) {
						alarmBinder = (IChronoAlarmBinder) service;
						alarm01 = alarmBinder.getAlarm01();
						alarm02 = alarmBinder.getAlarm02();
						alarm03 = alarmBinder.getAlarm03();
						alarm04 = alarmBinder.getAlarm04();
						alarm05 = alarmBinder.getAlarm05();
						boolean alarm01On = alarm01.isOn();
						boolean alarm02On = alarm02.isOn();
						boolean alarm03On = alarm03.isOn();
						boolean alarm04On = alarm04.isOn();
						boolean alarm05On = alarm05.isOn();
						long timeAlarm01 = alarm01.getAlarmInMiliseconds();
						long timeAlarm02 = alarm02.getAlarmInMiliseconds();
						long timeAlarm03 = alarm03.getAlarmInMiliseconds();
						long timeAlarm04 = alarm04.getAlarmInMiliseconds();
						long timeAlarm05 = alarm05.getAlarmInMiliseconds();
						refreshAlarms();
						refreshLabels();
						checkAlarmOnOff(alarm01, alarm01On, timeAlarm01);
						checkAlarmOnOff(alarm02, alarm02On, timeAlarm02);
						checkAlarmOnOff(alarm03, alarm03On, timeAlarm03);
						checkAlarmOnOff(alarm04, alarm04On, timeAlarm04);
						checkAlarmOnOff(alarm05, alarm05On, timeAlarm05);
						Log.i("Service", "connected 1");
					}

					@Override
					public void onServiceDisconnected(ComponentName name) {
						Log.i("Service", "disconnected");
						alarmBinder = null;
					}
				};
				this.bindService(new Intent(BabyTimerPrincipal.this,
						ChronoAlarmService.class), conn, 0);
			} else {
				if (anyAlarmIsOn()) {
					Intent myIntent = new Intent(BabyTimerPrincipal.this,
							ChronoAlarmService.class);
					this.startService(myIntent);
					conn = new ServiceConnection() {
						@Override
						public void onServiceConnected(ComponentName name,
								IBinder service) {
							alarmBinder = (IChronoAlarmBinder) service;
							alarm01 = alarmBinder.getAlarm01();
							alarm02 = alarmBinder.getAlarm02();
							alarm03 = alarmBinder.getAlarm03();
							alarm04 = alarmBinder.getAlarm04();
							alarm05 = alarmBinder.getAlarm05();
							boolean alarm01On = alarm01.isOn();
							boolean alarm02On = alarm02.isOn();
							boolean alarm03On = alarm03.isOn();
							boolean alarm04On = alarm04.isOn();
							boolean alarm05On = alarm05.isOn();
							long timeAlarm01 = alarm01.getAlarmInMiliseconds();
							long timeAlarm02 = alarm02.getAlarmInMiliseconds();
							long timeAlarm03 = alarm03.getAlarmInMiliseconds();
							long timeAlarm04 = alarm04.getAlarmInMiliseconds();
							long timeAlarm05 = alarm05.getAlarmInMiliseconds();
							refreshAlarms();
							refreshLabels();
							checkAlarmOnOff(alarm01, alarm01On, timeAlarm01);
							checkAlarmOnOff(alarm02, alarm02On, timeAlarm02);
							checkAlarmOnOff(alarm03, alarm03On, timeAlarm03);
							checkAlarmOnOff(alarm04, alarm04On, timeAlarm04);
							checkAlarmOnOff(alarm05, alarm05On, timeAlarm05);
							Log.i("Service", "connected 2");
						}

						@Override
						public void onServiceDisconnected(ComponentName name) {
							Log.i("Service", "disconnected");
							alarmBinder = null;
						}
					};
					this.bindService(new Intent(BabyTimerPrincipal.this,
							ChronoAlarmService.class), conn, 0);
				} else {
					alarm01 = new BabyAlarm(BabyAlarm.ALARM01);
					alarm02 = new BabyAlarm(BabyAlarm.ALARM02);
					alarm03 = new BabyAlarm(BabyAlarm.ALARM03);
					alarm04 = new BabyAlarm(BabyAlarm.ALARM04);
					alarm05 = new BabyAlarm(BabyAlarm.ALARM05);
					refreshAlarms();
					refreshLabels();
				}
			}
			firstTimeStart = false;
		} else {
			if (ChronoAlarmService.on) {
				alarm01 = alarmBinder.getAlarm01();
				alarm02 = alarmBinder.getAlarm02();
				alarm03 = alarmBinder.getAlarm03();
				alarm04 = alarmBinder.getAlarm04();
				alarm05 = alarmBinder.getAlarm05();
				boolean alarm01On = alarm01.isOn();
				boolean alarm02On = alarm02.isOn();
				boolean alarm03On = alarm03.isOn();
				boolean alarm04On = alarm04.isOn();
				boolean alarm05On = alarm05.isOn();
				long timeAlarm01 = alarm01.getAlarmInMiliseconds();
				long timeAlarm02 = alarm02.getAlarmInMiliseconds();
				long timeAlarm03 = alarm03.getAlarmInMiliseconds();
				long timeAlarm04 = alarm04.getAlarmInMiliseconds();
				long timeAlarm05 = alarm05.getAlarmInMiliseconds();
				refreshAlarms();
				refreshLabels();
				checkAlarmOnOff(alarm01, alarm01On, timeAlarm01);
				checkAlarmOnOff(alarm02, alarm02On, timeAlarm02);
				checkAlarmOnOff(alarm03, alarm03On, timeAlarm03);
				checkAlarmOnOff(alarm04, alarm04On, timeAlarm04);
				checkAlarmOnOff(alarm05, alarm05On, timeAlarm05);
				if (!anyAlarmIsOn()) {
					stopBabyService();
				}

			} else {
				if (anyAlarmIsOn()) {
					if (alarmBinder == null) {
						Intent myIntent = new Intent(BabyTimerPrincipal.this,
								ChronoAlarmService.class);
						this.startService(myIntent);
						conn = new ServiceConnection() {
							@Override
							public void onServiceConnected(ComponentName name,
									IBinder service) {
								alarmBinder = (IChronoAlarmBinder) service;
								alarm01 = alarmBinder.getAlarm01();
								alarm02 = alarmBinder.getAlarm02();
								alarm03 = alarmBinder.getAlarm03();
								alarm04 = alarmBinder.getAlarm04();
								alarm05 = alarmBinder.getAlarm05();
								boolean alarm01On = alarm01.isOn();
								boolean alarm02On = alarm02.isOn();
								boolean alarm03On = alarm03.isOn();
								boolean alarm04On = alarm04.isOn();
								boolean alarm05On = alarm05.isOn();
								long timeAlarm01 = alarm01
										.getAlarmInMiliseconds();
								long timeAlarm02 = alarm02
										.getAlarmInMiliseconds();
								long timeAlarm03 = alarm03
										.getAlarmInMiliseconds();
								long timeAlarm04 = alarm04
										.getAlarmInMiliseconds();
								long timeAlarm05 = alarm05
										.getAlarmInMiliseconds();
								refreshAlarms();
								refreshLabels();
								checkAlarmOnOff(alarm01, alarm01On, timeAlarm01);
								checkAlarmOnOff(alarm02, alarm02On, timeAlarm02);
								checkAlarmOnOff(alarm03, alarm03On, timeAlarm03);
								checkAlarmOnOff(alarm04, alarm04On, timeAlarm04);
								checkAlarmOnOff(alarm05, alarm05On, timeAlarm05);
								Log.i("Service", "connected 2");
							}

							@Override
							public void onServiceDisconnected(ComponentName name) {
								Log.i("Service", "disconnected");
								alarmBinder = null;
							}
						};
						this.bindService(new Intent(BabyTimerPrincipal.this,
								ChronoAlarmService.class), conn, 0);
					} else {
						alarm01 = alarmBinder.getAlarm01();
						alarm02 = alarmBinder.getAlarm02();
						alarm03 = alarmBinder.getAlarm03();
						alarm04 = alarmBinder.getAlarm04();
						alarm05 = alarmBinder.getAlarm05();
						boolean alarm01On = alarm01.isOn();
						boolean alarm02On = alarm02.isOn();
						boolean alarm03On = alarm03.isOn();
						boolean alarm04On = alarm04.isOn();
						boolean alarm05On = alarm05.isOn();
						long timeAlarm01 = alarm01.getAlarmInMiliseconds();
						long timeAlarm02 = alarm02.getAlarmInMiliseconds();
						long timeAlarm03 = alarm03.getAlarmInMiliseconds();
						long timeAlarm04 = alarm04.getAlarmInMiliseconds();
						long timeAlarm05 = alarm05.getAlarmInMiliseconds();
						refreshAlarms();
						refreshLabels();
						checkAlarmOnOff(alarm01, alarm01On, timeAlarm01);
						checkAlarmOnOff(alarm02, alarm02On, timeAlarm02);
						checkAlarmOnOff(alarm03, alarm03On, timeAlarm03);
						checkAlarmOnOff(alarm04, alarm04On, timeAlarm04);
						checkAlarmOnOff(alarm05, alarm05On, timeAlarm05);
					}
				}
			}
		}
	}

	private boolean anyAlarmIsOn() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		return (prefs.getBoolean("allarm01_active", false) && (prefs.getInt(
				"allarm01_hour", 0)
				+ prefs.getInt("allarm01_minute", 0) > 0))
				|| (prefs.getBoolean("allarm02_active", false) && (prefs
						.getInt("allarm02_hour", 0)
						+ prefs.getInt("allarm02_minute", 0) > 0))
				|| (prefs.getBoolean("allarm03_active", false) && (prefs
						.getInt("allarm03_hour", 0)
						+ prefs.getInt("allarm03_minute", 0) > 0))
				|| (prefs.getBoolean("allarm04_active", false) && (prefs
						.getInt("allarm04_hour", 0)
						+ prefs.getInt("allarm04_minute", 0) > 0))
				|| (prefs.getBoolean("allarm05_active", false) && (prefs
						.getInt("allarm05_hour", 0)
						+ prefs.getInt("allarm05_minute", 0) > 0));
	}

	private void stopBabyService() {
		Intent myIntent = new Intent(BabyTimerPrincipal.this,
				ChronoAlarmService.class);
		this.stopService(myIntent);
		if (alarmBinder != null) {
			this.unbindService(conn);
			alarmBinder = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (myCountDown01 != null) {
			myCountDown01.destroy();
		}
		if (myCountDown02 != null) {
			myCountDown02.destroy();
		}
		if (myCountDown03 != null) {
			myCountDown03.destroy();
		}
		if (myCountDown04 != null) {
			myCountDown04.destroy();
		}
		if (myCountDown05 != null) {
			myCountDown05.destroy();
		}
	}

	private void refreshLabels() {
		TextView labelAllarm01 = this
				.findViewById(R.id.AllarmName01);
		labelAllarm01.setText(alarm01.getNome());
		TextView labelAllarm02 = this
				.findViewById(R.id.AllarmName02);
		labelAllarm02.setText(alarm02.getNome());
		TextView labelAllarm03 = this
				.findViewById(R.id.AllarmName03);
		labelAllarm03.setText(alarm03.getNome());
		TextView labelAllarm04 = this
				.findViewById(R.id.AllarmName04);
		labelAllarm04.setText(alarm04.getNome());
		TextView labelAllarm05 = this
				.findViewById(R.id.AllarmName05);
		labelAllarm05.setText(alarm05.getNome());
	}

	private void initCounter(BabyAlarm pAllarm, long pTimeInitial) {
		long tempoTranscorrido = System.currentTimeMillis() - pTimeInitial;
		long remaindTimer = (pAllarm.getAlarmInMiliseconds() - tempoTranscorrido);
		switch (pAllarm.getId()) {
		case BabyAlarm.ALARM01: {

			stopCounter(pAllarm.getId());

			TextView countDownTimer01 = this
					.findViewById(R.id.CountDownTimer01);
			if (remaindTimer > 0) {
				myCountDown01 = new MyCountDownTimer(remaindTimer,
						countDownTimer01);
				myCountDown01.start();
			} else {
				runAnimations(countDownTimer01);
			}
			break;
		}
		case BabyAlarm.ALARM02: {
			
			stopCounter(pAllarm.getId());
			
			TextView countDownTimer02 = this
					.findViewById(R.id.CountDownTimer02);
			
			if (remaindTimer > 0) {
				myCountDown02 = new MyCountDownTimer(remaindTimer,
						countDownTimer02);
				myCountDown02.start();
			} else {
				runAnimations(countDownTimer02);
			}
			break;
		}
		case BabyAlarm.ALARM03: {
			
			stopCounter(pAllarm.getId());
			
			TextView countDownTimer03 = this
					.findViewById(R.id.CountDownTimer03);
			
			if (remaindTimer > 0) {
				myCountDown03 = new MyCountDownTimer(remaindTimer,
						countDownTimer03);
				myCountDown03.start();
			} else {
				runAnimations(countDownTimer03);
			}
			break;
		}
		case BabyAlarm.ALARM04: {
			
			stopCounter(pAllarm.getId());
			
			TextView countDownTimer04 = this
					.findViewById(R.id.CountDownTimer04);
			
			if (remaindTimer > 0) {
				myCountDown04 = new MyCountDownTimer(remaindTimer,
						countDownTimer04);
				myCountDown04.start();
			} else {
				runAnimations(countDownTimer04);
			}
			break;
		}
		case BabyAlarm.ALARM05: {
			
			stopCounter(pAllarm.getId());
			
			TextView countDownTimer05 = this
					.findViewById(R.id.CountDownTimer05);
			
			if (remaindTimer > 0) {
				myCountDown05 = new MyCountDownTimer(remaindTimer,
						countDownTimer05);
				myCountDown05.start();
			} else {
				runAnimations(countDownTimer05);
			}
			break;
		}
		}

	}

	private void stopCounter(int pAllarmNumber) {
		switch (pAllarmNumber) {
		case BabyAlarm.ALARM01: {
			if (myCountDown01 != null) {
				myCountDown01.destroy();
				myCountDown01 = null;
			}
			TextView countDownTimer01 = this
					.findViewById(R.id.CountDownTimer01);
			countDownTimer01.setText("00:00:00");
			break;
		}
		case BabyAlarm.ALARM02: {
			if (myCountDown02 != null) {
				myCountDown02.destroy();
			}
			TextView countDownTimer02 = this
					.findViewById(R.id.CountDownTimer02);
			countDownTimer02.setText("00:00:00");
			break;
		}
		case BabyAlarm.ALARM03: {
			if (myCountDown03 != null) {
				myCountDown03.destroy();
			}
			TextView countDownTimer03 = this
					.findViewById(R.id.CountDownTimer03);
			countDownTimer03.setText("00:00:00");
			break;
		}
		case BabyAlarm.ALARM04: {
			if (myCountDown04 != null) {
				myCountDown04.destroy();
			}
			TextView countDownTimer04 = this
					.findViewById(R.id.CountDownTimer04);
			countDownTimer04.setText("00:00:00");
			break;
		}
		case BabyAlarm.ALARM05: {
			if (myCountDown05 != null) {
				myCountDown05.destroy();
			}
			TextView countDownTimer05 = this
					.findViewById(R.id.CountDownTimer05);
			countDownTimer05.setText("00:00:00");
			break;
		}
		}

	}

	private void refreshAlarms() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		// ALARM 01
		alarm01.setNome(prefs.getString("allarm01_name", this
				.getString(R.string.allarm01_name)));
		alarm01.setImagem(prefs
				.getString("allarm01_imagem", "@drawable/dormir"));
		alarm01.setSom(prefs.getString("allarm01_som", "DEFAULT_RINGTONE_URI"));
		alarm01.setTime(prefs.getInt("allarm01_hour", 0), prefs.getInt(
				"allarm01_minute", 0), 0);
		alarm01.setOn(prefs.getBoolean("allarm01_active", false));

		// ALARM 02

		alarm02.setNome(prefs.getString("allarm02_name", this
				.getString(R.string.allarm02_name)));
		alarm02
				.setImagem(prefs
						.getString("allarm02_imagem", "@drawable/mamar"));
		alarm02.setSom(prefs.getString("allarm02_som", "DEFAULT_RINGTONE_URI"));
		alarm02.setTime(prefs.getInt("allarm02_hour", 0), prefs.getInt(
				"allarm02_minute", 0), 0);
		alarm02.setOn(prefs.getBoolean("allarm02_active", false));

		// ALARM 03
		alarm03.setNome(prefs.getString("allarm03_name", this
				.getString(R.string.allarm03_name)));
		alarm03.setImagem(prefs
				.getString("allarm03_imagem", "@drawable/fralda"));
		alarm03.setSom(prefs.getString("allarm03_som", "DEFAULT_RINGTONE_URI"));
		alarm03.setTime(prefs.getInt("allarm03_hour", 0), prefs.getInt(
				"allarm03_minute", 0), 0);
		alarm03.setOn(prefs.getBoolean("allarm03_active", false));

		// ALARM04
		alarm04.setNome(prefs.getString("allarm04_name", this
				.getString(R.string.allarm04_name)));
		alarm04.setImagem(prefs.getString("allarm04_imagem",
				"@drawable/medicine"));
		alarm04.setSom(prefs.getString("allarm04_som", "DEFAULT_RINGTONE_URI"));
		alarm04.setTime(prefs.getInt("allarm04_hour", 0), prefs.getInt(
				"allarm04_minute", 0), 0);
		alarm04.setOn(prefs.getBoolean("allarm04_active", false));

		// ALARM 05
		alarm05.setNome(prefs.getString("allarm05_name", this
				.getString(R.string.allarm05_name)));
		alarm05.setImagem(prefs.getString("allarm05_imagem",
				"@drawable/optional"));
		alarm05.setSom(prefs.getString("allarm05_som", "DEFAULT_RINGTONE_URI"));
		alarm05.setTime(prefs.getInt("allarm05_hour", 0), prefs.getInt(
				"allarm05_minute", 0), 0);
		alarm05.setOn(prefs.getBoolean("allarm05_active", false));

	}

	private long initInitialTime(int pAlarmNumber) {
		long initialTime = System.currentTimeMillis();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putLong("allarm0" + pAlarmNumber + "_timeInitial", initialTime);
		editor.commit();
		return initialTime;
	}

	private class MyCountDownTimer extends CountDownTimer {

		private TextView countDownField;

		public MyCountDownTimer(long pTime, TextView pCountDownField) {
			super(pTime, 1000);
			countDownField = pCountDownField;
		}

		public void destroy() {
			this.cancel();
			countDownField = null;
		}

		@Override
		public void onFinish() {
			countDownField.setText("00:00:00");
			runAnimations(countDownField);
		}

		@Override
		public void onTick(long pMillisUntilFinished) {
			long segundos = pMillisUntilFinished / 1000;
			long minutos = 0;
			long horas = 0;
			if (segundos >= 60) {
				minutos = segundos / 60;
				segundos = segundos % 60;
				if (minutos >= 60) {
					horas = minutos / 60;
					minutos = minutos % 60;
				}
			}
			String strSegundos = segundos >= 10 ? "" + segundos : "0"
					+ segundos;
			String strMinutos = minutos >= 10 ? "" + minutos : "0" + minutos;
			String strHoras = horas >= 10 ? "" + horas : "0" + horas;
			countDownField.setText(strHoras + ":" + strMinutos + ":"
					+ strSegundos);
		}
	}

	@Override
	public void onClick(View pV) {
		if (alarmBinder != null) {
			int i = pV.getId();
			if (i == R.id.alarm01) {
				TextView countDownTimer01 = this
						.findViewById(R.id.CountDownTimer01);
				if (countDownTimer01.getAnimation() != null) {
					alarmBinder.restartAlarm(alarm01);
					long timeInitial = initInitialTime(BabyAlarm.ALARM01);
					initCounter(alarm01, timeInitial);
					stopAnimations(countDownTimer01);
				} else {
					this.startActivity(new Intent(this, Preferences.class));
				}
			} else if (i == R.id.alarm02) {
				TextView countDownTimer02 = this
						.findViewById(R.id.CountDownTimer02);
				if (countDownTimer02.getAnimation() != null) {
					alarmBinder.restartAlarm(alarm02);
					long timeInitial = initInitialTime(BabyAlarm.ALARM02);
					initCounter(alarm02, timeInitial);
					stopAnimations(countDownTimer02);
				} else {
					this.startActivity(new Intent(this, Preferences.class));
				}
			} else if (i == R.id.alarm03) {
				TextView countDownTimer03 = this
						.findViewById(R.id.CountDownTimer03);
				if (countDownTimer03.getAnimation() != null) {
					alarmBinder.restartAlarm(alarm03);
					long timeInitial = initInitialTime(BabyAlarm.ALARM03);
					initCounter(alarm03, timeInitial);
					stopAnimations(countDownTimer03);
				} else {
					this.startActivity(new Intent(this, Preferences.class));
				}
			} else if (i == R.id.alarm04) {
				TextView countDownTimer04 = this
						.findViewById(R.id.CountDownTimer04);
				if (countDownTimer04.getAnimation() != null) {
					alarmBinder.restartAlarm(alarm04);
					long timeInitial = initInitialTime(BabyAlarm.ALARM04);
					initCounter(alarm04, timeInitial);
					stopAnimations(countDownTimer04);
				} else {
					this.startActivity(new Intent(this, Preferences.class));
				}
			} else if (i == R.id.alarm05) {
				TextView countDownTimer05 = this
						.findViewById(R.id.CountDownTimer05);
				if (countDownTimer05.getAnimation() != null) {
					alarmBinder.restartAlarm(alarm05);
					long timeInitial = initInitialTime(BabyAlarm.ALARM05);
					initCounter(alarm05, timeInitial);
					stopAnimations(countDownTimer05);
				} else {
					this.startActivity(new Intent(this, Preferences.class));
				}
			}
		} else {
			this.startActivity(new Intent(this, Preferences.class));
		}
	}

	// ////////////////////////////////////////////////////
	// //////////// ANIMATIONS ////////////////////////////

	private void stopAnimations(TextView pTextView) {
		pTextView.clearAnimation();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.cancel();
	}

	private void runAnimations(TextView pTextView) {
		Animation a = AnimationUtils.loadAnimation(this, R.anim.alpha);
		a.reset();
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		pTextView.clearAnimation();
		pTextView.startAnimation(a);
		v.cancel();
		long[] pattern = { 0, 200, 500 };
		v.vibrate(pattern, 0);

		/*
		 * a = AnimationUtils.loadAnimation(this, R.anim.translate); a.reset();
		 * pTextView.clearAnimation(); pTextView.startAnimation(a);
		 * 
		 * a = AnimationUtils.loadAnimation(this, R.anim.rotate); a.reset();
		 * pTextView.clearAnimation(); pTextView.startAnimation(a);
		 */
	}

	// ////////////////////////////////////////////////////

	// //////////////////////////////////////////////////
	// ////////////////MENU /////////////////////////////
	// //////////////////////////////////////////////////
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.babytimer_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		int i1 = item.getItemId();
		if (i1 == R.id.settings) {// Launch Preference activity
			Intent i = new Intent(BabyTimerPrincipal.this, Preferences.class);
			this.startActivity(i);
			// A toast is a view containing a quick little message for the
			// user.
			Toast.makeText(BabyTimerPrincipal.this,
					"Here you can maintain your allarms settings.",
					Toast.LENGTH_LONG).show();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	// ////////////////////////////////////////
	// /////// FIM MENU ///////////////////////
	// ////////////////////////////////////////

}