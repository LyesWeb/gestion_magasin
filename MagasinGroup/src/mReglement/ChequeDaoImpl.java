package mReglement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mDonnees.AbstractDao;
import mVentes.Vente;
import mVentes.VenteDaoImpl;


public class ChequeDaoImpl implements ChequeDao{

	@Override
	public void insert(Cheque che) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO cheque(id_vente, date_cheque, montant, titulaire) values(?, ?, ?, ?)");
			ps.setLong(1, che.getVente().getCodev());
			ps.setDate(2, (Date) che.getDateCheque());
			ps.setDouble(3, che.getMontant());
			ps.setString(4, che.getTitulaire());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Cheque getOne(long id_vente) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM `cheque` WHERE `id_vente` = ?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id_vente);
			ResultSet rs = ps.executeQuery();
			Cheque che = new Cheque();
			VenteDaoImpl vdao = new VenteDaoImpl();
			
			boolean b = false;
			if(rs.next()){
				b = true;
				Vente v = vdao.getOne(rs.getLong(6));
				che.setIdCheque(rs.getLong(1));
				che.setVente(v);
				che.setTitulaire(rs.getString(2));
				che.setDateCheque(rs.getDate(3));
				che.setMontant(rs.getDouble(5));
			}
			conn.close();
			if(b == true)
				return che;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
