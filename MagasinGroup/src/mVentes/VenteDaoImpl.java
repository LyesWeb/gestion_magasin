package mVentes;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import packfx.Client;
import mDonnees.AbstractDao;

public class VenteDaoImpl implements VenteDao {

	@Override
	public void insert(Vente v) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO vente(datev, totalv, idclient) values(?, ?, ?)");
			ps.setDate(1, (Date) v.getDatev());
			ps.setDouble(2, v.getTotalv());
			ps.setLong(3, v.getClientv().getId());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Vente v) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE vente SET datev=?, idclient=? WHERE codev=?");
			ps.setDate(1, (Date) v.getDatev());
			ps.setLong(2, v.getClientv().getId());
			ps.setLong(3, v.getCodev());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(long codev) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM lc WHERE codevente=?");
			ps.setLong(1, codev);
			ps.executeUpdate();
			ps = conn.prepareStatement("DELETE FROM vente WHERE codev=?");
			ps.setLong(1, codev);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Erreur de suppression !");
		}
	}

	@Override
	public Collection<Vente> getAll() {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<Vente> lesVentes = new ArrayList<>();
			String req = "SELECT * FROM vente INNER JOIN client On client.id = vente.idclient";
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Vente v = new Vente();
				v.setCodev(Long.parseLong(rs.getString(1)));
				v.setDatev(rs.getDate(2));
				v.setTotalv(rs.getDouble(3));
				v.setClientv(new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tele"), rs.getString("email"), rs.getString("adresse")));
				lesVentes.add(v);
			}
			conn.close();
			return lesVentes;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Vente getOne(long codev) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM vente INNER JOIN client On client.id = vente.idclient WHERE codev=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, codev);
			ResultSet rs = ps.executeQuery();
			Vente v = new Vente();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				v.setCodev(Long.parseLong(rs.getString(1)));
				v.setDatev(rs.getDate(2));
				v.setTotalv(rs.getDouble(3));
				v.setClientv(new Client(rs.getLong("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("tele"), rs.getString("email"), rs.getString("adresse")));
			}
			conn.close();
			if(b == true)
				return v;
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void updateTotalVente(long codevente) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE vente SET totalv=(SELECT SUM(soustotal) FROM lc WHERE codevente=?) WHERE codev=?");
			ps.setLong(1, codevente);
			ps.setLong(2, codevente);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static int getLastId() {
		Connection conn = AbstractDao.getCon().getConnexion();
		String req = "SELECT max(codev) FROM `vente`";
		try {
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1)+1;
		} catch (SQLException e) {
			return 0;
		}
	}

}