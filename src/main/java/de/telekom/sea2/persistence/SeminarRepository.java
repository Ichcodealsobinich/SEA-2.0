package de.telekom.sea2.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.NoSuchElementException;

import de.telekom.sea2.model.Person;
import de.telekom.sea2.model.Seminar;

public class SeminarRepository extends Repository{

	private PersonsRepository pr;
	private ParticipationRepository paR;

	public SeminarRepository(Connection c, String tableName) {
		super(c, tableName);
	}
	
	public PersonsRepository getPr() {
		return pr;
	}

	public void setPr(PersonsRepository pr) {
		this.pr = pr;
	}
	
	public void setPaR(ParticipationRepository paR) {
		this.paR = paR;
	}
	
	public long create(Seminar s) {		
		String sql = "INSERT INTO seminars ( ID, NAME, LECTURER) VALUES ( ?, ?, ?)";
		try (PreparedStatement ps = getConnection().prepareStatement(sql);){
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
		try (PreparedStatement ps = getConnection().prepareStatement(query);){
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
		try (PreparedStatement ps = getConnection().prepareStatement(query);){
			ps.setLong(1, id);			
			try (ResultSet rs = ps.executeQuery()){				
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
		
		//check if seminar exists in database and cancel if not
		if (!this.exists(seminar_id)) {
			return false;
		}
		//check if persons exists in database
		//and create it if not
		if (!pr.exists(p.getId())) {
			//Person will get a new id when saving in the db
			Long newId = pr.create(p);
			if (newId < 0) {
				return false;
			} else {
				p.setId(newId);
			}
		}			
		//insert person_id and seminar_id into joint table
		return paR.subscribe(p.getId(), seminar_id);
	}
	
}
