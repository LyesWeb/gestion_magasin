package mProduit;

import mCategories.*;

public class Produit {

	private long code;
	private String designation;
	private double prixAchat;
	private double prixVente;
	private Categorie cat;
	
	public Produit() { }

	public Produit(String designation, double prixAchat, double prixVente, Categorie cat) {
		this.designation = designation;
		this.prixAchat = prixAchat;
		this.prixVente = prixVente;
		this.cat = cat;
	}
	
	public Produit(long code, String designation, double prixAchat, double prixVente, Categorie cat) {
		this.code = code;
		this.designation = designation;
		this.prixAchat = prixAchat;
		this.prixVente = prixVente;
		this.cat = cat;
	}

	public long getCode() {
		return code;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public double getPrixAchat() {
		return prixAchat;
	}

	public void setPrixAchat(double prixAchat) {
		this.prixAchat = prixAchat;
	}

	public double getPrixVente() {
		return prixVente;
	}

	public void setPrixVente(double prixVente) {
		this.prixVente = prixVente;
	}

	public Categorie getCat() {
		return cat;
	}

	public void setCat(Categorie cat) {
		this.cat = cat;
	}

	@Override
	public String toString() {
		return this.getDesignation();
	}	
	
}