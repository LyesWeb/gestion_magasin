package mReglement;

import java.util.Date;
import mVentes.Vente;

public class Reglement {

	private long id = -1;
	private String titulaire = "";
	private Date datecheque = null;
	private Date datepp = null;
	private Date datepe = null;
	private double montant = 0;
	private String type = "";
	private Vente vente = null;
	private String etat = "";
	private int isTrait = 0;
	
	public int getIsTrait() {
		return isTrait;
	}
	public void setIsTrait(int isTrait) {
		this.isTrait = isTrait;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitulaire() {
		return titulaire;
	}
	public void setTitulaire(String titulaire) {
		this.titulaire = titulaire;
	}
	public Date getDatecheque() {
		return datecheque;
	}
	public void setDatecheque(Date datecheque) {
		this.datecheque = datecheque;
	}
	public Date getDatepp() {
		return datepp;
	}
	public void setDatepp(Date datepp) {
		this.datepp = datepp;
	}
	public Date getDatepe() {
		return datepe;
	}
	public void setDatepe(Date datepe) {
		this.datepe = datepe;
	}
	public double getMontant() {
		return montant;
	}
	public void setMontant(double montant) {
		this.montant = montant;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEtat() {
		return etat;
	}
	public void setEtat(String etat) {
		this.etat = etat;
	}
	public Vente getVente() {
		return vente;
	}
	public void setVente(Vente vente) {
		this.vente = vente;
	}
	
}
