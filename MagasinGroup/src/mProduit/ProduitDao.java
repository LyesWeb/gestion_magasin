package mProduit;

import java.util.Collection;

public interface ProduitDao {
	
	public void insert(Produit p);
	public void update(Produit p);
	public void delete(long c);
	public Collection<Produit> getAll();
	public Produit getOne(long id);
	
}