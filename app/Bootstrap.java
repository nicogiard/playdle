
import models.Event;
import play.jobs.Job;
import play.jobs.OnApplicationStart;
import play.test.Fixtures;

@OnApplicationStart
public class Bootstrap extends Job {
	@Override
	public void doJob() {
		// Check if the database is empty
		if (Event.count() == 0) {
			Fixtures.load("initial-data.yml");
		}
	}
}
