package com.freddomoura.android.babytimer.feature.services;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.util.Log;

import com.freddomoura.android.babytimer.feature.BabyTimerPrincipal;
import com.freddomoura.android.babytimer.feature.entidades.BabyAlarm;

public class ChronoAlarmBinder extends Binder implements IChronoAlarmBinder {

	private Context context;

	private BabyAlarm babyAlarm01;
	private BabyAlarm babyAlarm02;
	private BabyAlarm babyAlarm03;
	private BabyAlarm babyAlarm04;
	private BabyAlarm babyAlarm05;

	private Timer timer01 = new Timer();
	private Timer timer02 = new Timer();
	private Timer timer03 = new Timer();
	private Timer timer04 = new Timer();
	private Timer timer05 = new Timer();

	private MediaPlayer mp01;
	private MediaPlayer mp02;
	private MediaPlayer mp03;
	private MediaPlayer mp04;
	private MediaPlayer mp05;

	public ChronoAlarmBinder() {
		babyAlarm01 = new BabyAlarm(BabyAlarm.ALARM01);
		babyAlarm02 = new BabyAlarm(BabyAlarm.ALARM02);
		babyAlarm03 = new BabyAlarm(BabyAlarm.ALARM03);
		babyAlarm04 = new BabyAlarm(BabyAlarm.ALARM04);
		babyAlarm05 = new BabyAlarm(BabyAlarm.ALARM05);
	}

	protected void destroy() {
		stopAlarm(babyAlarm01);
		stopAlarm(babyAlarm02);
		stopAlarm(babyAlarm03);
		stopAlarm(babyAlarm04);
		stopAlarm(babyAlarm05);

		babyAlarm01 = null;
		babyAlarm02 = null;
		babyAlarm03 = null;
		babyAlarm04 = null;
		babyAlarm05 = null;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	private void scheduleAlarm(Timer pTimer, final BabyAlarm pAlarm) {
		pTimer.schedule(new TimerTask() {
			public void run() {
				Log.i(ChronoAlarmBinder.class.toString(),
						"Rodando o timer do alarm: " + pAlarm.getId());
				playRingtone(pAlarm);
				// showActivity();
			}
		}, pAlarm.getAlarmInMiliseconds());
	}

	protected void showActivity() {
		context.startActivity(new Intent(context, BabyTimerPrincipal.class));
	}

	private void startAlarm(BabyAlarm pAlarm) {
		switch (pAlarm.getId()) {
		case BabyAlarm.ALARM01: {
			scheduleAlarm(timer01, pAlarm);
			break;
		}
		case BabyAlarm.ALARM02: {
			scheduleAlarm(timer02, pAlarm);
			break;
		}
		case BabyAlarm.ALARM03: {
			scheduleAlarm(timer03, pAlarm);
			break;
		}
		case BabyAlarm.ALARM04: {
			scheduleAlarm(timer04, pAlarm);
			break;
		}
		case BabyAlarm.ALARM05: {
			scheduleAlarm(timer05, pAlarm);
			break;
		}
		}
	}

	public void stopAlarm(BabyAlarm pAlarm) {
		MediaPlayer mp = null;
		Log.i(ChronoAlarmBinder.class.toString(), "Parando alarm: "
				+ pAlarm.getId());
		switch (pAlarm.getId()) {
		case BabyAlarm.ALARM01: {
			mp = this.mp01;
			this.timer01.cancel();
			this.timer01.purge();
			this.timer01 = new Timer();
			break;
		}
		case BabyAlarm.ALARM02: {
			mp = mp02;
			timer02.cancel();
			timer02.purge();
			timer02 = new Timer();
			break;
		}
		case BabyAlarm.ALARM03: {
			mp = mp03;
			timer03.cancel();
			timer03.purge();
			timer03 = new Timer();
			break;
		}
		case BabyAlarm.ALARM04: {
			mp = mp04;
			timer04.cancel();
			timer04.purge();
			timer04 = new Timer();
			break;
		}
		case BabyAlarm.ALARM05: {
			mp = mp05;
			timer05.cancel();
			timer05.purge();
			timer05 = new Timer();
			break;
		}
		}
		stopPlayRingtone(mp);
	}

	private void playRingtone(BabyAlarm pAllarm) {
		MediaPlayer mp = null;
		switch (pAllarm.getId()) {
		case BabyAlarm.ALARM01: {
			this.mp01 = new MediaPlayer();
			mp = this.mp01;
			break;
		}
		case BabyAlarm.ALARM02: {
			mp02 = new MediaPlayer();
			mp = mp02;
			break;
		}
		case BabyAlarm.ALARM03: {
			mp03 = new MediaPlayer();
			mp = mp03;
			break;
		}
		case BabyAlarm.ALARM04: {
			mp04 = new MediaPlayer();
			mp = mp04;
			break;
		}
		case BabyAlarm.ALARM05: {
			mp05 = new MediaPlayer();
			mp = mp05;
			break;
		}
		}
		Uri ringtoneUri = Uri.parse(pAllarm.getSom());
		mp.setAudioStreamType(AudioManager.STREAM_ALARM);
		mp.setLooping(true);

		try {
			mp.setDataSource(context, ringtoneUri);
			mp.prepare();
			mp.start();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void stopPlayRingtone(MediaPlayer pMp) {
		try {
			if (pMp != null) {
				if (pMp.isPlaying()) {
					pMp.stop();
					pMp.reset();
					pMp.release();
				}
			}
		} catch (Exception e) {
			Log.e("ChronoAlarmBinder", "Erro ao parar Player", e);
		}

	}

	@Override
	public BabyAlarm getAlarm01() {
		return babyAlarm01;
	}

	@Override
	public BabyAlarm getAlarm02() {
		return babyAlarm02;
	}

	@Override
	public BabyAlarm getAlarm03() {
		return babyAlarm03;
	}

	@Override
	public BabyAlarm getAlarm04() {
		return babyAlarm04;
	}

	@Override
	public BabyAlarm getAlarm05() {
		return babyAlarm05;
	}

	@Override
	public void restartAlarm(BabyAlarm pAlarm) {
		stopAlarm(pAlarm);
		startAlarm(pAlarm);

	}

}