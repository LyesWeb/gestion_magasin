package mVentes;

import java.util.ArrayList;
import java.util.Date;
import mClient.Client;


public class Vente {

	private long codev;
	private Date datev;
	private double totalv;
	private Client clientv;
	private String stat;
	ArrayList<LC> lignesCommande = new ArrayList<>();
	public Vente() {
		super();
	}
	public Vente(long codev, Date datev, Client clientv) {
		super();
		this.codev = codev;
		this.datev = datev;
		this.clientv = clientv;
	}
	public Vente(Date datev, double totalv, Client clientv) {
		super();
		this.datev = datev;
		this.totalv = totalv;
		this.clientv = clientv;
	}
	public Vente(long codev, Date datev, double totalv, Client clientv) {
		super();
		this.codev = codev;
		this.datev = datev;
		this.totalv = totalv;
		this.clientv = clientv;
	}
	public long getCodev() {
		return codev;
	}
	public void setCodev(long codev) {
		this.codev = codev;
	}
	public Date getDatev() {
		return datev;
	}
	public void setDatev(Date datev) {
		this.datev = datev;
	}
	public double getTotalv() {
		return totalv;
	}
	public void setTotalv(double totalv) {
		this.totalv = totalv;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	@Override
	public String toString() {
		return "Vente [codev=" + codev + ", datev=" + datev + ", totalv=" + totalv + "]";
	}
	public Client getClientv() {
		return clientv;
	}
	public void setClientv(Client clientv) {
		this.clientv = clientv;
	}
	
	public ArrayList<LC> getLignesCommande() {
		return lignesCommande;
	}
	public void addLC(LC lc) {
		this.getLignesCommande().add(lc);
	}
	
}
