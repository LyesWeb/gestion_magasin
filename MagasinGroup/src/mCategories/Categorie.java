package mCategories;

public class Categorie {
	
	private long codecat;
	private String intitule;

	public Categorie() { }
	public Categorie(String intitule) {
		this.intitule = intitule;
	}
	public Categorie(long codecat, String intitule) {
		super();
		this.codecat = codecat;
		this.intitule = intitule;
	}
	public long getCodecat() {
		return codecat;
	}
	public void setCodecat(long codecat) {
		this.codecat = codecat;
	}
	public String getIntitule() {
		return intitule;
	}
	public void setIntitule(String intitule) {
		this.intitule = intitule;
	}
	@Override
	public String toString() {
		return "Categorie [codecat=" + codecat + ", intitule=" + intitule + "]";
	}

}