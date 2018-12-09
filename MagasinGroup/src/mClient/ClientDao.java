package mClient;

import java.util.Collection;

public interface ClientDao {

	public void insert(Client cli);
	public void update(Client cli);
	public void delete(long c);
	public Collection<Client> getAll();
	public Client getOne(long id);
	
}
