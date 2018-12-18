package mReglement;

import java.util.Date;

import mVentes.Vente;

public class Cheque {
	
	private long idCheque;
	private Vente vente;
	private String titulaire;
	private Date dateCheque;
	private double montant;
	
	public Cheque() {
		super();
	}

	public Cheque(Vente vente, String titulaire, Date dateCheque, double montant) {
		super();
		this.vente = vente;
		this.titulaire = titulaire;
		this.dateCheque = dateCheque;
		this.montant = montant;
	}

	public long getIdCheque() {
		return idCheque;
	}

	public void setIdCheque(long idCheque) {
		this.idCheque = idCheque;
	}

	public Vente getVente() {
		return vente;
	}

	public void setVente(Vente vente) {
		this.vente = vente;
	}

	public String getTitulaire() {
		return titulaire;
	}

	public void setTitulaire(String titulaire) {
		this.titulaire = titulaire;
	}

	public Date getDateCheque() {
		return dateCheque;
	}

	public void setDateCheque(Date dateCheque) {
		this.dateCheque = dateCheque;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}
	
	
	
}
