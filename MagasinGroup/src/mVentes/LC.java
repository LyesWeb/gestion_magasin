package mVentes;

import packfx.Produit;

public class LC {

	private long codelc;
	private int qt;
	private double soustotal;
	private Produit produitlc;
	private long codevente;

	public LC() {
		super();
	}
	public LC(long codelc, int qt, double soustotal, Produit produitlc, long codevente) {
		super();
		this.codelc = codelc;
		this.qt = qt;
		this.soustotal = soustotal;
		this.produitlc = produitlc;
		this.codevente = codevente;
	}
	public long getCodelc() {
		return codelc;
	}
	public void setCodelc(long codelc) {
		this.codelc = codelc;
	}
	public int getQt() {
		return qt;
	}
	public void setQt(int qt) {
		this.qt = qt;
	}
	public Produit getProduitlc() {
		return produitlc;
	}
	public void setProduitlc(Produit produitlc) {
		this.produitlc = produitlc;
	}
	public long getCodevente() {
		return codevente;
	}
	public void setCodevente(long codevente) {
		this.codevente = codevente;
	}
	public double getSoustotal() {
		return soustotal;
	}
	public void setSoustotal(double soustotal) {
		this.soustotal = soustotal;
	}
	@Override
	public String toString() {
		return "LC [codelc=" + codelc + ", qt=" + qt + ", soustotal=" + soustotal + ", produitlc=" + produitlc
				+ ", codevente=" + codevente + "]";
	}
	
}
