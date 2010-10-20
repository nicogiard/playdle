package models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.InFuture;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class EventDate extends Model {

	@Temporal(TemporalType.DATE)
	@InFuture
	@Required
	public Date date;

	@OneToMany(cascade = CascadeType.PERSIST)
	public List<People> listPeople;

	public EventDate(Date date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof EventDate)) {
			return false;
		}
		return date.equals(((EventDate) obj).date);
	}
}
