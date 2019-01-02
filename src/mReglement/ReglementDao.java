package mReglement;

import java.util.Collection;

public interface ReglementDao {

	public void insert(Reglement r);
	public void update(Reglement r);
	public void delete(long id);
	public Collection<Reglement> getAll();
	public Collection<Reglement> getAllofType(long codeV,int isTrait);
	public Reglement getOne(long id);
	public double getMontantPayerTrait(double codev);
	public Reglement getOneRegOfType(long codev, String type);
	
}