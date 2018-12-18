package mReglement;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import mDonnees.AbstractDao;
import mVentes.Vente;
import mVentes.VenteDaoImpl;


public class EspeceDaoImpl implements EspeceDao{

	@Override
	public void insert(Espece esp) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO espece(id_vente, date_espece, montant) values(?, ?, ?)");
			ps.setLong(1, esp.getVente().getCodev());
//			ps.setDate(2, (Date) esp.getDateEspece());
			ps.setDate(2, new Date(0));
			ps.setDouble(3, esp.getMontant());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Espece getOne(long id_vente) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM `espece` WHERE `id_vente` = ? and id_trait=0";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id_vente);
			ResultSet rs = ps.executeQuery();
			Espece esp = new Espece();
			VenteDaoImpl vdao = new VenteDaoImpl();
			
			boolean b = false;
			if(rs.next()){
				b = true;
				Vente v = vdao.getOne(rs.getLong(2));
				esp.setIdEspece(rs.getLong(1));
				esp.setVente(v);
				esp.setDateEspece(rs.getDate(3));
				esp.setMontant(rs.getDouble(4));
			}
			conn.close();
			if(b == true)
				return esp;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
