package mReglement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import mDonnees.AbstractDao;
import mVentes.VenteDao;
import mVentes.VenteDaoImpl;

@SuppressWarnings("unused")
public class ReglementDaoImpl implements ReglementDao {

	public static VenteDao vdao = new VenteDaoImpl();
	private java.sql.Date sDC = null, sDPP = null, sDPE = null;
	
	@Override
	public void insert(Reglement r) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO Reglement"
					+ "(titulaire, datecheque, datepp, datepe, montant, type, codev, etat, isTrait) "
					+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			if(r.getType() == "cheque" && r.getIsTrait()==0){
				sDC = new java.sql.Date(r.getDatecheque().getTime());
			}
			if(r.getType() == "traite" && r.getIsTrait()==0){
				sDPP = new java.sql.Date(r.getDatepp().getTime());
				sDPE = new java.sql.Date(r.getDatepe().getTime());
			}
			ps.setString(1, r.getTitulaire());
			ps.setDate(2, sDC);
			ps.setDate(3, sDPP);
			ps.setDate(4, sDPE);
			ps.setDouble(5, r.getMontant());
			ps.setString(6, r.getType());
			ps.setLong(7, r.getVente().getCodev());
			ps.setString(8, r.getEtat());
			ps.setInt(9, r.getIsTrait());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("erreur insert reg !!");
		}
	}

	@Override
	public void update(Reglement r) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE reglement SET "
					+ "titulaire=?, datecheque=?, datepp=?, datepe=?, montant=?, type=?, codev=?, etat=? "
					+ "WHERE codecateg=?");
			java.sql.Date sDC = new java.sql.Date(r.getDatecheque().getTime());
			java.sql.Date sDPP = new java.sql.Date(r.getDatepp().getTime());
			java.sql.Date sDPE = new java.sql.Date(r.getDatepe().getTime());
			ps.setString(1, r.getTitulaire());
			ps.setDate(2, sDC);
			ps.setDate(3, sDPP);
			ps.setDate(4, sDPE);
			ps.setDouble(5, r.getMontant());
			ps.setString(6, r.getType());
			ps.setLong(7, r.getVente().getCodev());
			ps.setString(8, r.getEtat());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(long id) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("DELETE FROM reglement WHERE id=?");
			ps.setLong(1, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			//e.printStackTrace();
			System.out.println("Erreur de suppression !");
		}	
	}

	@Override
	public Collection<Reglement> getAll() {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<Reglement> lesReglements = new ArrayList<>();
			String req = "SELECT * FROM reglement";
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Reglement r = new Reglement();
				r.setId(Long.parseLong(rs.getString(1)));
				r.setTitulaire(rs.getString(2));
				r.setDatecheque(rs.getDate((3)));
				r.setDatepp(rs.getDate(4));
				r.setDatepe(rs.getDate(5));
				r.setMontant(rs.getDouble(6));
				r.setType(rs.getString(7));
				r.setVente(vdao.getOne(rs.getLong(8)));
				lesReglements.add(r);
			}
			conn.close();
			return lesReglements;
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Collection<Reglement> getAllofType(long codeV, int isTrait) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<Reglement> lesReglements = new ArrayList<>();
			String req = "SELECT * FROM reglement WHERE codev="+codeV+" AND isTrait = "+isTrait;
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Reglement r = new Reglement();
				r.setId(Long.parseLong(rs.getString(1)));
				r.setTitulaire(rs.getString(2));
				r.setDatecheque(rs.getDate((3)));
				r.setDatepp(rs.getDate(4));
				r.setDatepe(rs.getDate(5));
				r.setMontant(rs.getDouble(6));
				r.setType(rs.getString(7));
				r.setVente(vdao.getOne(rs.getLong(8)));
				lesReglements.add(r);
			}
			conn.close();
			return lesReglements;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Reglement getOne(long id) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM reglement WHERE id=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			Reglement r = new Reglement();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				r.setId(Long.parseLong(rs.getString(1)));
				r.setTitulaire(rs.getString(2));
				r.setDatecheque(rs.getDate((3)));
				r.setDatepp(rs.getDate(4));
				r.setDatepe(rs.getDate(5));
				r.setMontant(rs.getDouble(6));
				r.setType(rs.getString(7));
				r.setVente(vdao.getOne(rs.getLong(8)));
			}
			conn.close();
			if(b == true)
				return r;
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Reglement getOneRegOfType(long codev, String typeee) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM reglement WHERE codev=? AND isTrait=0 AND type='"+typeee+"'";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, codev);
			ResultSet rs = ps.executeQuery();
			Reglement r = new Reglement();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				r.setId(Long.parseLong(rs.getString("id")));
				r.setMontant(rs.getDouble("montant"));
				r.setType(rs.getString("type"));
				r.setVente(vdao.getOne(rs.getLong("codev")));
				if(rs.getString("type") == "cheque"){
					r.setTitulaire(rs.getString("titulaire"));
					r.setDatecheque(rs.getDate(("datecheque")));
				}
				if(rs.getString("type") == "traite"){
					r.setDatepp(rs.getDate("datepp"));
					r.setDatepe(rs.getDate("datepe"));
				}
			}
			conn.close();
			if(b == true)
				return r;
			return null;
		} catch (Exception e) {
			//return null;
			System.out.println("erreur getone reg testttt");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public double getMontantPayerTrait(double codev) {
		double m = 0;
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT montant FROM reglement WHERE codev = "+codev+" AND isTrait = 1";
//			System.out.println("SELECT montant FROM reglement WHERE codev = "+codev+" AND isTrait = 1");
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				m += rs.getDouble(1);
			}
//			System.out.println(m);
			conn.close();
			return m;
		} catch (Exception e) {
			return 0;
		}
	}

	
}
