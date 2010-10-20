package models;

import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class People extends Model {
	
	@Required
	public String name;

	public People(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || name == null || !(obj instanceof People)) {
			return false;
		}
		return StringUtils.equalsIgnoreCase(name, ((People) obj).name);
	}
}
