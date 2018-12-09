package mClient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import mDonnees.AbstractDao;

public class ClientDaoImpl implements ClientDao {
	
	@Override
	public void insert(Client cli) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("INSERT INTO client(nom, prenom, tele, email, adresse) values(?, ?, ?, ?, ?)");
			ps.setString(1, cli.getNom());
			ps.setString(2, cli.getPrenom());
			ps.setString(3, cli.getTele());
			ps.setString(4, cli.getEmail());
			ps.setString(5, cli.getAdresse());
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(Client cli) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			PreparedStatement ps = conn.prepareStatement("UPDATE client SET nom=?, prenom=?, tele=?, email=?, adresse=? WHERE id=?");
			ps.setString(1, cli.getNom());
			ps.setString(2, cli.getPrenom());
			ps.setString(3, cli.getTele());
			ps.setString(4, cli.getEmail());
			ps.setString(5, cli.getAdresse());
			ps.setLong(6, cli.getId());
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
			PreparedStatement ps = conn.prepareStatement("DELETE FROM client WHERE id=?");
			ps.setLong(1, id);
			ps.executeUpdate();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Collection<Client> getAll() {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			Collection<Client> lesClients = new ArrayList<>();
			String req = "SELECT * FROM client";
			PreparedStatement ps = conn.prepareStatement(req);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
			{
				Client cli = new Client();
				cli.setId(Long.parseLong(rs.getString(1)));
				cli.setNom(rs.getString(2));
				cli.setPrenom(rs.getString(3));
				cli.setTele(rs.getString(4));
				cli.setEmail(rs.getString(5));
				cli.setAdresse(rs.getString(6));
				lesClients.add(cli);
			}
			conn.close();
			return lesClients;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Client getOne(long id) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM Client WHERE code=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();
			Client cli = new Client();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				cli.setId(Long.parseLong(rs.getString(1)));
				cli.setNom(rs.getString(2));
				cli.setPrenom(rs.getString(3));
				cli.setTele(rs.getString(4));
				cli.setEmail(rs.getString(5));
				cli.setAdresse(rs.getString(6));
			}
			conn.close();
			if(b == true)
				return cli;
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public Client getOne(String nom) {
		try {
			Connection conn = AbstractDao.getCon().getConnexion();
			String req = "SELECT * FROM Client WHERE nom=?";
			PreparedStatement ps = conn.prepareStatement(req);
			ps.setString(1, nom);
			ResultSet rs = ps.executeQuery();
			Client cli = new Client();
			boolean b = false;
			if(rs.next())
			{
				b = true;
				cli.setId(Long.parseLong(rs.getString(1)));
				cli.setNom(rs.getString(2));
				cli.setPrenom(rs.getString(3));
				cli.setTele(rs.getString(4));
				cli.setEmail(rs.getString(5));
				cli.setAdresse(rs.getString(6));
			}
			conn.close();
			if(b == true)
				return cli;
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
}
