package de.telekom.sea2.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

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
			ps.setByte(2, p.getSalutation().toByte());
			ps.setString(3, p.getFirstname());
			ps.setString(4, p.getLastname());
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
			ps.setByte(2, p.getSalutation().toByte());
			ps.setString(3, p.getFirstname());
			ps.setString(4, p.getLastname());
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
		Boolean result = false;
		String sql = "DELETE FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, p.getId());
			result = ps.execute();
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	public Person get(long id) throws NoSuchElementException{
		
		String query= "SELECT * FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					return person;
				} else {
					throw new NoSuchElementException();
				}
			}catch (Exception e) {throw new NoSuchElementException();}
			
		}catch (Exception e) {throw new NoSuchElementException();}
	}
	
	public ArrayList<Person> getAll() throws Exception{
		ArrayList<Person> list = new ArrayList<Person>();
		String query= "SELECT * FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){
								
				//we do not want to unnecessarily boost the counter
				long counter = BaseObject.getCounter();
				
				/*iterate through result and write into ArrayList*/
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					list.add(person);
				}
				//we do not want to unnecessarily boost the counter
				BaseObject.setCounter(counter);
				return list;
			}catch (Exception e) {throw new Exception();}			
		}catch (Exception e) {throw new Exception();}
	}
	
	public ArrayList<Person> find(String firstname, String lastname) {
		ArrayList<Person> list = new ArrayList<Person>();
		String query= "SELECT * FROM personen WHERE vorname LIKE ? AND nachname LIKE ?";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setString(1, firstname);
			ps.setString(2, lastname);
			try (ResultSet rs = ps.executeQuery()){
				long counter = BaseObject.getCounter();
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					list.add(person);
				}
				BaseObject.setCounter(counter);
			}catch (Exception e) {}
		}catch (Exception e) {}
		return list;
	}
	
	public ArrayList<Person> find(String lastname) {
		ArrayList<Person> list = new ArrayList<Person>();
		String query= "SELECT * FROM personen WHERE nachname LIKE ?";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setString(1, lastname);
			try (ResultSet rs = ps.executeQuery()){
				long counter = BaseObject.getCounter();
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					list.add(person);
				}
				BaseObject.setCounter(counter);
			}catch (Exception e) {}
		}catch (Exception e) {}
		return list;
	}
	
	public boolean deleteAll() {
		String sql = "DELETE FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public int getCount() {
		String query= "SELECT COUNT(*) FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){
				rs.next();
				return rs.getInt(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return -1;
		}	
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
