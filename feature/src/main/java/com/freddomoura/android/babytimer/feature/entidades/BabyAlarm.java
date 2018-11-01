package com.freddomoura.android.babytimer.feature.entidades;

import java.io.Serializable;

public class BabyAlarm implements Serializable {

	// constantes
	public static final int ALARM01 = 1;
	public static final int ALARM02 = 2;
	public static final int ALARM03 = 3;
	public static final int ALARM04 = 4;
	public static final int ALARM05 = 5;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2240354124843027788L;

	private int id;
	private String nome;
	private String som;
	private String imagem;
	private int hora;
	private int minuto;
	private int segundo;
	private String backgroudColor;
	private boolean on;

	public boolean isOn() {
		return on;
	}

	public void setOn(boolean pOn) {
		this.on = pOn;
	}

	public BabyAlarm(int pId) {
		id = pId;
	}

	public int getId() {
		return id;
	}

	public String getBackgroudColor() {
		return backgroudColor;
	}

	public void setBackgroudColor(String pBackgroudColor) {
		backgroudColor = pBackgroudColor;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String pNome) {
		nome = pNome;
	}

	public String getSom() {
		return som;
	}

	public void setSom(String pSom) {
		som = pSom;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String pImagem) {
		imagem = pImagem;
	}

	public int getHora() {
		return hora;
	}

	public void setTime(int pHora, int pMinuto, int pSegundo) {
		hora = pHora;
		minuto = pMinuto;
		segundo = pSegundo;
	}

	public int getMinuto() {
		return minuto;
	}

	public int getSegundo() {
		return segundo;
	}

	public long getAlarmInMiliseconds() {
		return (hora * 60 * 60 * 1000) + (minuto * 60 * 1000)
				+ (segundo * 1000);
	}

}
