package mCategories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import mDonnees.AbstractDao;

public class CategorieDaoImpl implements CategorieDao {

	@Override
	public void insert(Categorie c) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO categorie(intitule) values(?)");
			ps.setString(1, c.getIntitule());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Categorie p) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE categorie SET intitule=? WHERE codecateg=?");
			ps.setString(1, p.getIntitule());
			ps.setDouble(2, p.getCodecat());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(long c) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM categorie WHERE codecateg=?");
			ps.setLong(1, c);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur de suppression !");
		}	
	}

	@Override
	public Collection<Categorie> getAll() {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<Categorie> lesCategories = new ArrayList<>();
			String req = "SELECT * FROM categorie";
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Categorie c = new Categorie();
				c.setCodecat(Long.parseLong(rs.getString(1)));
				c.setIntitule(rs.getString(2));
				lesCategories.add(c);
			}
			conn.close();
			return lesCategories;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Categorie getOne(long id) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM categorie WHERE codecateg=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			Categorie c = new Categorie();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				c.setCodecat(Long.parseLong(rs.getString(1)));
				c.setIntitule(rs.getString(2));
			}
			conn.close();
			if(b == true)
				return c;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
}
