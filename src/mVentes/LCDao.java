package mVentes;

import java.util.Collection;

public interface LCDao {

	public void insert(LC lc);
	public void update(LC lc);
	public void delete(long codelc);
	public Collection<LC> getAll();
	public Collection<LC> getAll(Vente v);
	public LC getOne(long id);
	public LC getLc(long codeprd, long codevente);
	
}