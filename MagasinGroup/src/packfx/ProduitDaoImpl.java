package packfx;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import mCategories.Categorie;
import mDonnees.AbstractDao;

public class ProduitDaoImpl extends AbstractDao implements ProduitDao {

	@Override
	public void insert(Produit p) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO produit(designation, prixAchat, prixVente, codecat) values(?, ?, ?, ?)");
			ps.setString(1, p.getDesignation());
			ps.setDouble(2, p.getPrixAchat());
			ps.setDouble(3, p.getPrixVente());
			ps.setLong(4, p.getCat().getCodecat());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Produit p) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE produit SET designation=?, prixAchat=?, prixVente=?, codecat=? WHERE code=?");
			ps.setString(1, p.getDesignation());
			ps.setDouble(2, p.getPrixAchat());
			ps.setDouble(3, p.getPrixVente());
			ps.setDouble(4, p.getCat().getCodecat());
			ps.setDouble(5, p.getCode());
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
			PreparedStatement ps = conn.prepareStatement("DELETE FROM produit WHERE code=?");
			ps.setLong(1, c);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<Produit> getAll() {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<Produit> lesProduits = new ArrayList<>();
			String req = "SELECT * FROM produit INNER JOIN Categorie ON produit.codecat=Categorie.codecateg";
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Produit e = new Produit();
				e.setCode(Long.parseLong(rs.getString(1)));
				e.setDesignation(rs.getString(2));
				e.setPrixAchat(Double.parseDouble(rs.getString(3)));
				e.setPrixVente(Double.parseDouble(rs.getString(4)));
				e.setCat(new Categorie(Long.parseLong(rs.getString(5)), rs.getString(7)));
				lesProduits.add(e);
			}
			conn.close();
			return lesProduits;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Produit getOne(long id) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM produit INNER JOIN Categorie ON produit.codecat=Categorie.codecateg WHERE produit.code=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			Produit p = new Produit();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				p.setCode(Long.parseLong(rs.getString(1)));
				p.setDesignation(rs.getString(2));
				p.setPrixAchat(Double.parseDouble(rs.getString(3)));
				p.setPrixVente(Double.parseDouble(rs.getString(4)));
				p.setCat(new Categorie(Long.parseLong(rs.getString(5)), rs.getString(7)));
			}
			conn.close();
			if(b == true)
				return p;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	public static int getCount() {
		Connection conn = AbstractDao.getCon().getConnexion();
		String req = "SELECT max(code) FROM produit";
		try {
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1)+1;
		} catch (SQLException e) {
			return 0;
		}
	}

	public Produit getOne(String designation) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM produit INNER JOIN Categorie ON produit.codecat=Categorie.codecateg WHERE produit.designation=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setString(1, designation);
			ResultSet rs = ps.executeQuery();
			Produit p = new Produit();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				p.setCode(Long.parseLong(rs.getString(1)));
				p.setDesignation(rs.getString(2));
				p.setPrixAchat(Double.parseDouble(rs.getString(3)));
				p.setPrixVente(Double.parseDouble(rs.getString(4)));
				p.setCat(new Categorie(Long.parseLong(rs.getString(5)), rs.getString(7)));
			}
			conn.close();
			if(b == true)
				return p;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}
