package com.freddomoura.android.babytimer.feature.services;

import android.os.IBinder;

import com.freddomoura.android.babytimer.feature.entidades.BabyAlarm;

public interface IChronoAlarmBinder extends IBinder{

	public BabyAlarm getAlarm01();
	public BabyAlarm getAlarm02();
	public BabyAlarm getAlarm03();
	public BabyAlarm getAlarm04();
	public BabyAlarm getAlarm05();
	public void restartAlarm(BabyAlarm pBabyAlarm);
	public void stopAlarm(BabyAlarm pAlarm);

}
