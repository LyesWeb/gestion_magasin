package mReglement;

import java.util.Date;

import mVentes.Vente;

public class Espece {
	private long idEspece;
	private Vente vente;
	private Date dateEspece;
	private double montant;
	
	public Espece() {
		super();
	}
	public Espece(Vente vente, Date dateEspece, double montant) {
		super();
		this.vente = vente;
		this.dateEspece = dateEspece;
		this.montant = montant;
	}
	public long getIdEspece() {
		return idEspece;
	}
	public void setIdEspece(long idEspece) {
		this.idEspece = idEspece;
	}
	public Vente getVente() {
		return vente;
	}
	public void setVente(Vente vente) {
		this.vente = vente;
	}
	public Date getDateEspece() {
		return dateEspece;
	}
	public void setDateEspece(Date dateEspece) {
		this.dateEspece = dateEspece;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	
	
	
}
