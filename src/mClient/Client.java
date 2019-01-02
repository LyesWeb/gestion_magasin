package mClient;

public class Client {

	private long id;
	private String nom;
	private String prenom;
	private String tele;
	private String email;
	private String adresse;
	
	public Client() {
		super();
	}
	public Client(String nom, String prenom, String tele, String email, String adresse) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.tele = tele;
		this.email = email;
		this.adresse = adresse;
	}
	public Client(long id, String nom, String prenom, String tele, String email, String adresse) {
		super();
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.tele = tele;
		this.email = email;
		this.adresse = adresse;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getTele() {
		return tele;
	}
	public void setTele(String tele) {
		this.tele = tele;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	@Override
	public String toString() {
		return this.getNom()+" "+this.getPrenom();
	}
	public String getNomPrenom() {
		return this.getNom()+" "+this.getPrenom();
	}
	
}
