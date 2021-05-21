package de.telekom.sea2.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ParticipationRepository {
	private Connection connection;
	private PersonsRepository pr;
	private SeminarRepository sr;
	
	public ParticipationRepository(Connection c, PersonsRepository pr, SeminarRepository sr) {
		this.connection = c; 
		this.pr = pr;
		this.sr = sr;
	}
	
	public boolean subscribe(Long person_id, Long seminar_id) {
		
		//Check if Person and Seminar exist
		if (!pr.exists(person_id)) {return false;}
		if (!sr.exists(seminar_id)) {return false;}
		
		//only insert subscription if not already subscribed
		if (!exists(person_id,seminar_id)) {
			String sql = "INSERT INTO participates ( person_id, seminar_id) VALUES ( ?, ?)";
			try (PreparedStatement ps = this.connection.prepareStatement(sql);){
				ps.setLong(1, person_id);
				ps.setLong(2, seminar_id);
				ps.execute();
			} catch (Exception e) {return false;}
		}		
		return true;
	}

	public boolean unsubscribe(Long person_id, Long seminar_id) {
		String sql = "DELETE FROM participates WHERE person_id=? AND seminar_id=?;";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, person_id);
			ps.setLong(2, seminar_id);
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean unsubscribePerson(Long person_id) {
		String sql = "DELETE FROM participates WHERE person_id=?;";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, person_id);
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean unsubscribeSeminar(Long seminar_id) {
		String sql = "DELETE FROM participates WHERE seminar_id=?;";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.setLong(1, seminar_id);
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean unsubscribeAll() {
		String sql = "DELETE FROM participates";
		try (PreparedStatement ps = this.connection.prepareStatement(sql);){
			ps.execute();
		} catch (Exception e) {return false;}
		return true;
	}
	
	public boolean exists(Long person_id, Long seminar_id) {
		String query = "SELECT COUNT(1) FROM participants WHERE person_id= ? AND seminar_id=?;";
		try (PreparedStatement ps = this.connection.prepareStatement(query);){
			ps.setLong(1, person_id);
			ps.setLong(2, seminar_id);
			try (ResultSet rs = ps.executeQuery()){
				rs.next();
				System.out.println(rs.getLong(1));
				if (rs.getLong(1) == 1) {					
					return true;
				}
				else {return false;}
			}catch (Exception e) {return false;}
		} catch (Exception e) {return false;}
	}
}
