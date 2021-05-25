package de.telekom.sea2.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class Repository {

	private Connection connection;
	private String tableName = "";
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public Connection getConnection() {
		return connection;
	}

	public Repository(Connection c, String tableName) {
		this.setConnection(c); 
		this.setTableName(tableName);
	}
	
	public boolean delete(long id) {
		String sql = String.format("DELETE FROM %s WHERE id=?",tableName);
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, id);
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean deleteAll() {
		String sql = String.format("DELETE FROM %s", tableName);
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public int getCount() {
		String query= String.format("SELECT COUNT(*) FROM %s", tableName);
		try (PreparedStatement ps = this.connection.prepareStatement(query)){
			try (ResultSet rs = ps.executeQuery()) {
				rs.next();
				return rs.getInt(1);
			}catch (Exception e) {return -1;}
		} catch (Exception e) {return -1;}

	}
	
	public boolean exists(Long id) {
		String query = String.format("SELECT COUNT(1) FROM %s WHERE id= ?;", tableName);
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				rs.next();
				if (rs.getLong(1) > 0) {return true;}
				else {return false;}
			}catch (Exception e) {return false;}
		} catch (Exception e) {return false;}
	}
	
	public long getNewUniqueId() {
		long id = 0;
		String sql = String.format("SELECT MAX (id) from %s",tableName);
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {id=rs.getLong(1);}
			}catch (Exception e) {return -1;}
		}catch (Exception e) {return -1;}
		return id+1;
	}
}
