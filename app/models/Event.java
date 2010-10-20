package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Event extends Model {

	public Date creationDate = new Date();

	@Required
	public String title;

	@Lob
	@Required
	public String description;

	@Required
	public String owner;

	@Email
	@Required
	public String email;
	
	public boolean status = false;

	@OneToMany(cascade = CascadeType.ALL)
	public List<People> listPeople;

	@OneToMany(cascade = CascadeType.ALL)
	public List<EventDate> listEventDate;

	@Override
	public String toString() {
		return title;
	}

	public boolean contains(EventDate eventDate) {
		return this.listEventDate.contains(eventDate);
	}
}
