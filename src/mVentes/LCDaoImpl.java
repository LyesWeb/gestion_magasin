package mVentes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import com.mysql.jdbc.Statement;

import mCategories.Categorie;
import mDonnees.AbstractDao;
import mDonnees.Config;
import mProduit.Produit;


public class LCDaoImpl implements LCDao{

	@Override
	public void insert(LC lc) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO lc(qt, soustotal, codeprd, codevente) values(?, ?, ?, ?)");
			ps.setInt(1, lc.getQt());
			ps.setDouble(2, lc.getSoustotal());
			ps.setLong(3, lc.getProduitlc().getCode());
			ps.setLong(4, lc.getCodevente());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(LC lc) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE lc SET qt=?, soustotal=?, codeprd=?, codevente=? WHERE codelc=?");
			ps.setInt(1, lc.getQt());
			ps.setDouble(2, lc.getSoustotal());
			ps.setLong(3, lc.getProduitlc().getCode());
			ps.setLong(4, lc.getCodevente());
			ps.setLong(5, lc.getCodelc());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(long codelc) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM lc WHERE codelc=?");
			ps.setLong(1, codelc);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			System.out.println("Erreur de suppression !");
		}
	}

	@Override
	public Collection<LC> getAll() {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<LC> lesLCs = new ArrayList<>();
			String req = "SELECT * FROM lc INNER JOIN produit ON produit.code = lc.codeprd";
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				LC lc = new LC();
				lc.setCodelc(Long.parseLong(rs.getString(1)));
				lc.setQt(Integer.parseInt((rs.getString(2))));
				lc.setSoustotal(Double.parseDouble(rs.getString(3)));
				lc.setProduitlc(new Produit(rs.getLong("code"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), new Categorie()));
				lc.setCodevente(Long.parseLong(rs.getString(5)));
				lesLCs.add(lc);
			}
			conn.close();
			return lesLCs;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Collection<LC> getAll(Vente v) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<LC> lesLCs = new ArrayList<>();
			String req = "SELECT * FROM lc INNER JOIN produit ON produit.code = lc.codeprd WHERE lc.codevente=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, v.getCodev());
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				LC lc = new LC();
				lc.setCodelc(Long.parseLong(rs.getString(1)));
				lc.setQt(Integer.parseInt((rs.getString(2))));
				lc.setSoustotal(Double.parseDouble(rs.getString(3)));
				lc.setProduitlc(new Produit(rs.getLong("code"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), new Categorie()));
				lc.setCodevente(Long.parseLong(rs.getString(5)));
				lesLCs.add(lc);
			}
			conn.close();
			return lesLCs;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public LC getOne(long id) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM lc INNER JOIN produit ON produit.code = lc.codeprd WHERE lc.codelc=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			LC lc = new LC();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				lc.setCodelc(Long.parseLong(rs.getString(1)));
				lc.setQt(Integer.parseInt((rs.getString(2))));
				lc.setSoustotal(Double.parseDouble(rs.getString(3)));
				lc.setProduitlc(new Produit(rs.getLong("code"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), new Categorie()));
				lc.setCodevente(Long.parseLong(rs.getString(5)));
			}
			conn.close();
			if(b == true)
				return lc;
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public LC getLc(long codeprd, long codevente) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM lc INNER JOIN produit ON produit.code = lc.codeprd WHERE lc.codeprd=? AND lc.codevente=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, codeprd);
			ps.setLong(2, codevente);
			ResultSet rs = ps.executeQuery();
			LC lc = new LC();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				lc.setCodelc(Long.parseLong(rs.getString(1)));
				lc.setQt(Integer.parseInt((rs.getString(2))));
				lc.setSoustotal(Double.parseDouble(rs.getString(3)));
				lc.setProduitlc(new Produit(rs.getLong("code"), rs.getString("designation"), rs.getDouble("prixAchat"), rs.getDouble("prixVente"), new Categorie()));
				lc.setCodevente(Long.parseLong(rs.getString(5)));
			}
			conn.close();
			if(b == true)
				return lc;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	public void updateOrInsert(Vente v,ArrayList<Integer> ids) {
		try {
			ArrayList<LC> lcs = v.getLignesCommande();
			Connection conn = AbstractDao.getCon().getConnexion();
			String deleteQ = "DELETE FROM lc WHERE codevente="+v.getCodev()+" and codelc NOT IN (";
			if(lcs.size()==0) deleteQ += "'')";
			int i=0;
			for(LC lc:lcs) {
				PreparedStatement ps = conn.prepareStatement("INSERT INTO lc(codelc,qt,soustotal,codeprd,codevente) \r\n" + 
						"VALUES (?,?,?,?,?) \r\n" + 
						"ON DUPLICATE KEY UPDATE qt = ?;",Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, lc.getCodelc());
				ps.setLong(2, lc.getQt());
				ps.setDouble(3, lc.getProduitlc().getPrixVente());
				ps.setLong(4, lc.getProduitlc().getCode());
				ps.setLong(5, lc.getCodevente());
				ps.setLong(6, lc.getQt());
				ps.executeUpdate();
				ResultSet rss = ps.getGeneratedKeys();
                if(rss.next() && lcs.size()!=0){
                    if(i==lcs.size()-1) {
                    	deleteQ += ""+rss.getInt(1)+")";
                    }else {
                    	deleteQ += ""+rss.getInt(1)+",";
                    }
                }else {
                	if(i==lcs.size()-1) {                		
                		deleteQ += lc.getCodelc()+")";
                	}else {
                		deleteQ += lc.getCodelc()+",";
                	}
                }
//				System.out.println("INSERT INTO lc(codelc,qt,soustotal,codeprd,codevente) \r\n" + 
//						"VALUES ("+lc.getCodelc()+","+lc.getQt()+","+lc.getProduitlc().getPrixVente()+","+lc.getProduitlc().getCode()+","+lc.getCodevente()+") \r\n" + 
//						"ON DUPLICATE KEY UPDATE qt = "+lc.getQt()+";");
				ps.close();
				i++;
			}
			PreparedStatement ps2 = conn.prepareStatement(deleteQ);
			ps2.executeUpdate();
			//update totalv
			PreparedStatement ps3 = conn.prepareStatement("UPDATE `vente` SET `totalv`=? WHERE `codev`=?");
			ps3.setDouble(1, v.getTotalv());
			ps3.setDouble(2, v.getCodev());
//			VenteDaoImpl vdo = new VenteDaoImpl();
//			vdo.updateTotalVente(v.getCodev());
			ps3.executeUpdate();
//			System.out.println(deleteQ);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getLastID() {
		Connection conn = AbstractDao.getCon().getConnexion();
		String req = "SELECT `AUTO_INCREMENT` FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '"+Config.dbName+"' AND TABLE_NAME = 'lc'";
		try {
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			return 0;
		}
	}
}
