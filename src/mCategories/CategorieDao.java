package mCategories;

import java.util.Collection;

public interface CategorieDao {

	public void insert(Categorie c);
	public void update(Categorie c);
	public void delete(long codecateg);
	public Collection<Categorie> getAll();
	public Categorie getOne(long id);
	
}