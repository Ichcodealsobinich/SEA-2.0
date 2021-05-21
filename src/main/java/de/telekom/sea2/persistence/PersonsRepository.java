package de.telekom.sea2.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import de.telekom.sea2.model.*;
import de.telekom.sea2.*;

/** 
 * A helper class to manage persons in a database.
 * Needs a database connection to work. 
 * 
 * @author sea4
 *
 */
public class PersonsRepository {
	
	private Connection connection;
	private ParticipationRepository paR;
	
	/**
	 * Constructor with a db connection
	 */
	public PersonsRepository(Connection c) {
		this.connection = c; 
	}
	
	public void setPaR(ParticipationRepository paR) {
		this.paR = paR;
	}

	/**
	 * This method saves a person in the database.
	 * It will change the id to ensure it's unique.
	 * @param p A reference to an instance of the person class.
	 * @return Returns the (new) id of the saved person or -1 in case of error.
	 */
	public long create(Person p) {
		
		String sql = "INSERT INTO personen ( ID, ANREDE, VORNAME, NACHNAME) VALUES ( ?, ?, ?, ? )";
		p.setId(getNewUniqueId());
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, p.getId());
			ps.setByte(2, p.getSalutation().toByte());
			ps.setString(3, p.getFirstname());
			ps.setString(4, p.getLastname());
			ps.execute();
		} catch (Exception e) {return -1;}
		return p.getId();
	}
	
	public boolean update(Person p) {
		String sql = "UPDATE personen "
				+ "ANREDE=?, "
				+ "VORNAME=?, "
				+ "NACHNAME=? "
				+ "WHERE ID=?;";		
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setByte(1, p.getSalutation().toByte());
			ps.setString(2, p.getFirstname());
			ps.setString(3, p.getLastname());
			ps.setLong(4, p.getId());
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	/**
	 * Removes a person from the database.
	 * Returns false in case of an error.
	 * This method also tries to unsubscribe the person from all seminars,
	 * but does not check if that worked. 
	 * @param id is the id of the person to delete.
	 * @return Returns true if no error occurred.
	 */
	public boolean delete(long id) {
		
		String sql = "DELETE FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, id);
			ps.execute();
		} catch (Exception e) {return false;}
		paR.unsubscribePerson(id);
		return true;
	}
	
	/**
	 * Removes a person from the database.
	 * Returns false in case of an error.
	 * This method also tries to unsubscribe the person from all seminars,
	 * but does not check if that worked. 
	 * @param p is a reference to the person to delete. 
	 * @return Returns true if no error occurred.
	 */
	public boolean delete(Person p) {

		String sql = "DELETE FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, p.getId());
			ps.execute();
		} catch (Exception e) {return false;}
		paR.unsubscribePerson(p.getId());
		return true;
	}
	
	public Person get(long id) throws NoSuchElementException{
		
		String query= "SELECT * FROM personen WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(query)){
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
				/*iterate through result and write into ArrayList*/
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					list.add(person);
				}
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
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					list.add(person);
				}
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
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					list.add(person);
				}
			}catch (Exception e) {}
		}catch (Exception e) {}
		return list;
	}
	
	public boolean deleteAll() {
		String sql = "DELETE FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.execute();
		} catch (Exception e) {return false;}
		paR.unsubscribeAll();
		return true;
	}
	
	/**
	 * Gets the number of persons saved in the database
	 * @return the number of persons saved in the database. Returns -1 in case of error.
	 */
	public int getCount() {
		String query= "SELECT COUNT(*) FROM personen";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){
				rs.next();
				return rs.getInt(1);
			}
		}catch (Exception e) {return -1;}	
	}
	
	public boolean exists(Long id) {
		String query = "SELECT COUNT(1) FROM personen WHERE id= ?;";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				rs.next();
				if (rs.getLong(1) == 1) {return true;}
				else {return false;}
			}catch (Exception e) {return false;}
		} catch (Exception e) {return false;}
	}
	
	private long getHighestId() {
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
	
	private long getNewUniqueId() {
		return getHighestId() +1;
	}
}
