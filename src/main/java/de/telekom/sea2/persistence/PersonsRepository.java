package de.telekom.sea2.persistence;

import java.sql.*;

import de.telekom.sea2.model.*;
import de.telekom.sea2.*;

public class PersonsRepository {
	
	private Connection connection;
	
	/*Constructor with a db connection*/
	public PersonsRepository(Connection c) {
		this.connection = c; 
	}
	
	public boolean create(Person p) {
		
		String sql = "INSERT INTO personen ( ID, ANREDE, VORNAME, NACHNAME) VALUES ( ?, ?, ?, ? )";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, p.getId());
			ps.setByte(2, p.getAnrede().toByte());
			ps.setString(3, p.getVorname());
			ps.setString(4, p.getNachname());
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean update(Person p) {
		String sql = "UPDATE personen "
				+ "SET ID=?, "
				+ "ANREDE=?, "
				+ "VORNAME=?, "
				+ "NACHNAME=? "
				+ "WHERE ID=?;";		
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, p.getId());
			ps.setByte(2, p.getAnrede().toByte());
			ps.setString(3, p.getVorname());
			ps.setString(4, p.getNachname());
			ps.setLong(5, p.getId());
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean delete(long id) {
		
		String sql = "DELETE FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, id);
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean delete(Person p) {
		String sql = "DELETE FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, p.getId());
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public Person get(long id) {
		
		String query= "SELECT * FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setAnrede(rs.getByte(2));
					person.setVorname(rs.getString(3));
					person.setNachname(rs.getString(4));
					return person;
				}
			}catch (Exception e) {System.out.println("Problem with rs");};
			
		}catch (Exception e) {System.out.println("Problem with ps");}
		return new Person();
	}
	
	public Person[] getAll(){
		String query= "SELECT * FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){
				
				/*find out how big the array will be*/
				int size =0;
				if (rs != null) 
				{
				  rs.last();    // moves cursor to the last row
				  size = rs.getRow(); // get row id 
				  rs.beforeFirst();
				}
				Person[] persons = new Person[size];
				
				/*iterate through result and write into array*/
				int i = 0;
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setAnrede(rs.getByte(2));
					person.setVorname(rs.getString(3));
					person.setNachname(rs.getString(4));
					persons[i] = person;
					i++;
				}
				return persons;
			}catch (Exception e) {System.out.println("Problem with rs");};
			
		}catch (Exception e) {System.out.println("Problem with ps");}
		return new Person[0];
	}
	
	public long getHighestId() {
		long id = 0;
		String query= "SELECT * FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					if (rs.getLong(1)>id) {
						id=rs.getLong(1);
					}
				}
			}catch (Exception e) {System.out.println("Problem with rs");};
		}catch (Exception e){}
		return id;
	}
}