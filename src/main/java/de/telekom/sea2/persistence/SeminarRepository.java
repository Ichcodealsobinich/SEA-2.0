package de.telekom.sea2.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.NoSuchElementException;

import de.telekom.sea2.BaseObject;
import de.telekom.sea2.model.Person;
import de.telekom.sea2.model.Seminar;

public class SeminarRepository {

	private Connection connection;
	private PersonsRepository pr;
	private ParticipationRepository paR;
	


	public SeminarRepository(Connection c, PersonsRepository pr) {
		this.connection = c; 
		this.pr = pr;
	}
	
	public void setPaR(ParticipationRepository paR) {
		this.paR = paR;
	}
	
	public long create(Seminar s) {		
		String sql = "INSERT INTO seminars ( ID, NAME, LECTURER) VALUES ( ?, ?, ?)";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			s.setId(getNewUniqueId());
			ps.setLong(1, s.getId());
			ps.setString(2, s.getName());
			ps.setLong(3, s.getLecturer().getId());
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		for (Person p : s.getParticipants()) {
			addPersonToSeminar(p,s.getId());
		}
		return s.getId();
	}
	
	public Seminar get(Long id) {
		String query= "SELECT * FROM seminars WHERE id=?";
		Long lecturer_id;
		Seminar seminar = new Seminar();
		
		//read seminar
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				if (rs.next()) {					
					seminar.setId(rs.getLong(1));
					seminar.setName(rs.getString(2));
					lecturer_id = rs.getLong(3);					
				} else {
					throw new NoSuchElementException();
				}
			}catch (Exception e) {throw new NoSuchElementException();}			
		}catch (Exception e) {throw new NoSuchElementException();}
		
		//getLecturer
		Person lecturer = pr.get(lecturer_id);
		seminar.setLecturer(lecturer);
		
		//get all Participants
		query = "    SELECT *"
				+ "  FROM personen"
				+ "  WHERE id IN (SELECT person_id"
				+ "               FROM participates"
				+ "               WHERE seminar_id = ?";		
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);			
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
					seminar.add(person);
				}
			}catch (Exception e) {}			
		}catch (Exception e) {}
		return seminar;
	}

	public boolean addPersonToSeminar(Person p, Long seminar_id) {
		//check if persons exists in database
		//and create it if not
		if (!pr.exists(p.getId())) {
			//Person will get a new id when saving in the db
			Long newId = pr.create(p);
			if (newId < 0) {return false;
			}else {p.setId(newId);}
		}
		
		//check if seminar exists in database and cancel if not
		if (!this.exists(seminar_id)) {
			return false;
		}
		
		//insert person_id and seminar_id into joint table
		return paR.subscribe(p.getId(), seminar_id);
	}

	public boolean delete(Long id) {
		String sql = "DELETE FROM seminars WHERE id=?";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, id);
			ps.execute();
		} catch (Exception e) {return false;}
		paR.unsubscribeSeminar(id);
		return true;
	}
	
	public boolean deleteAll() {
		String sql = "DELETE FROM seminars";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.execute();
		} catch (Exception e) {return false;}
		paR.unsubscribeAll();
		return true;
	}

	public boolean exists(Long id) {
		String query = "SELECT COUNT(1) FROM seminars WHERE id= ?;";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()){
				rs.next();
				System.out.println(id);
				System.out.println(rs.getLong(1));
				if (rs.getLong(1) == 1) {					
					return true;
				}
				else {return false;}
			}catch (Exception e) {return false;}
		} catch (Exception e) {return false;}
	}
	
	private long getNewUniqueId() {
		long id = 1;
		String query= "SELECT id FROM seminars";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()) {
					if (rs.getLong(1)>id) {
						id=rs.getLong(1);
					}
				}
			}catch (Exception e) {System.out.println("Problem with rs");};
		}catch (Exception e){}
		return id+1;
	}
}
