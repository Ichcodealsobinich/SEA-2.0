package de.telekom.sea2.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import de.telekom.sea2.model.*;

/** 
 * A helper class to manage persons in a database.
 * Needs a database connection to work. 
 * 
 * @author sea4
 *
 */
public class PersonsRepository extends Repository {
	
	private ParticipationRepository paR;
	
	public PersonsRepository(Connection c, String tableName) {
		super(c, tableName);
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
		
		if (p==null) {return -1;}
		String sql = String.format("INSERT INTO %s ( ID, ANREDE, VORNAME, NACHNAME, BIRTHDATE) VALUES ( ?, ?, ?, ?, ? )", getTableName());
		try (PreparedStatement ps = this.getConnection().prepareStatement(sql);){
			p.setId(getNewUniqueId());
			ps.setLong(1, p.getId());
			ps.setByte(2, p.getSalutation().toByte());
			ps.setString(3, p.getFirstname());
			ps.setString(4, p.getLastname());
			ps.setDate(5, Date.valueOf(p.getBirthdate()));
			ps.execute();
		} catch (Exception e) {return -1;}
		return p.getId();
	}
	
	public boolean update(Person p) {
		
		if (p==null) {return false;}
		String sql = String.format("UPDATE %s "
				+ "ANREDE=?, "
				+ "VORNAME=?, "
				+ "NACHNAME=?, "
				+ "BIRTHDATE=?"
				+ "WHERE ID=?;",getTableName());		
		try (PreparedStatement ps = this.getConnection().prepareStatement(sql);){
			ps.setByte(1, p.getSalutation().toByte());
			ps.setString(2, p.getFirstname());
			ps.setString(3, p.getLastname());
			ps.setLong(4, p.getId());
			ps.setDate(5, Date.valueOf(p.getBirthdate()));
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
		
		if (super.delete(id)) {
			paR.unsubscribePerson(id);
		} else {
			return false;
		}		
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
		if (super.delete(p.getId())) {
			paR.unsubscribePerson(p.getId());
		} else {
			return false;
		}		
		return true;
	}
	
	public Result<Person> get(long id) throws NoSuchElementException{
		
		String query= String.format("SELECT * FROM %s WHERE id=?",getTableName());
		try (PreparedStatement ps = this.getConnection().prepareStatement(query)){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					person.setBirthdate(rs.getDate(5));
					return new Result<Person>(person);
				} else {
					return Result.none;
				}
			}catch (Exception e) {return Result.none;}			
		}catch (Exception e) {return Result.none;}
	}
	
	public ArrayList<Person> getAll() {
		ArrayList<Person> list = new ArrayList<Person>();
		String query= "SELECT * FROM personen";
		try (PreparedStatement ps = this.getConnection().prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){				
				/*iterate through result and write into ArrayList*/
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					person.setBirthdate(rs.getDate(5));
					list.add(person);
				}				
			}catch (Exception e) {}			
		}catch (Exception e) {}
		return list;
	}
	
	public ArrayList<Person> find(String firstname, String lastname) {
		ArrayList<Person> list = new ArrayList<Person>();
		String query= String.format("SELECT * FROM %s WHERE vorname LIKE ? AND nachname LIKE ?",getTableName());
		try (PreparedStatement ps = this.getConnection().prepareStatement(query);){
			ps.setString(1, firstname);
			ps.setString(2, lastname);
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					person.setBirthdate(rs.getDate(5));
					list.add(person);
				}
			}catch (Exception e) {}
		}catch (Exception e) {}		
		return list;
	}
	
	public ArrayList<Person> find(String lastname) {
		ArrayList<Person> list = new ArrayList<Person>();
		String query= String.format("SELECT * FROM %s WHERE nachname LIKE ?",getTableName());
		try (PreparedStatement ps = this.getConnection().prepareStatement(query);){
			ps.setString(1, lastname);
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					Person person = new Person();
					person.setId(rs.getLong(1));
					person.setSalutation(rs.getByte(2));
					person.setFirstName(rs.getString(3));
					person.setLastName(rs.getString(4));
					person.setBirthdate(rs.getDate(5));
					list.add(person);
				}
			}catch (Exception e) {}
		}catch (Exception e) {}
		return list;
	}		

}
