package mVentes;

import java.util.Collection;

public interface VenteDao {

	public void insert(Vente v);
	public void update(Vente v);
	public void delete(long codev);
	public Collection<Vente> getAll();
	public Vente getOne(long id);
	public void updateTotalVente(long codevente);
	
}
