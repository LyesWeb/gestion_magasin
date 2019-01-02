package mReglement;

import java.io.Serializable;
import java.util.Date;


public class Compte implements Serializable{
	private int id_Card;
	private int Number ;
	private int SortCode;
	private Date DtaeExpired;
	
	
	
	public Compte() {
		super();
	}
	public Compte(int number, int sortCode, Date dtaeExpired) {
		super();
		Number = number;
		SortCode = sortCode;
		DtaeExpired = dtaeExpired;
	}
	public int getId_Card() {
		return id_Card;
	}
	public void setId_Card(int id_Card) {
		this.id_Card = id_Card;
	}
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	public int getSortCode() {
		return SortCode;
	}
	public void setSortCode(int sortCode) {
		SortCode = sortCode;
	}
	public Date getDtaeExpired() {
		return DtaeExpired;
	}
	public void setDtaeExpired(Date dtaeExpired) {
		DtaeExpired = dtaeExpired;
	}
	
	
}
