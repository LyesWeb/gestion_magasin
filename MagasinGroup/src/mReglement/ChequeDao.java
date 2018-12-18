package mReglement;

public interface ChequeDao {

	public void insert(Cheque cheque);
	public Cheque getOne(long id_vente);
	
}
