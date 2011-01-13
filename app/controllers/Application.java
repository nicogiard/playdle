package controllers;

import play.*;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static final int EVENT_PER_PAGE = 2;

	public static void index() {
		// Only 5 last events with status set to true
		List<Event> events = Event.find("status = true").fetch(5);
		render(events);
	}

	public static void create(@Valid Event event) {
		// create the new event
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			index();
		} else {
			event.save();
		}
		date(event.id);
	}

	public static void events(int p) {
		int current = p == 0 ? 1 : p;

		// All events with status set to true
		List<Event> events = Event.find("status = true").fetch(current, EVENT_PER_PAGE);

		long count = Event.count("status = true");
		
		long max = count / EVENT_PER_PAGE + (count % EVENT_PER_PAGE > 0 ? 1 : 0);
		
		List<Integer> pages = new ArrayList<Integer>();
		for (int i = 1; i <= max; i++) {
			pages.add(i);
		}
		
		render(current, max, pages, events);
	}

	public static void show(Long id) {
		// show the event identified by id
		Event event = Event.findById(id);
		notFoundIfNull(event);
		if (!event.status) {
			notFound();
		}
		render(event);
	}

	public static void date(Long id) {
		// show the event identified by id in order to add dates
		Event event = Event.findById(id);
		notFoundIfNull(event);
		render(event);
	}

	public static void addDate(@Required Long id, @Valid @Required Date date) {
		// Default format for date is yyyy-MM-dd

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			Event event = Event.findById(id);
			notFoundIfNull(event);

			EventDate eventDate = new EventDate(date);
			validation.valid(eventDate);
			if (validation.hasErrors()) {
				params.flash();
				validation.keep();
			}
			if (!event.listEventDate.contains(eventDate)) {
				event.listEventDate.add(eventDate);
				event.save();
				flash.clear();
				validation.clear();
			} else {
				validation.addError("date", "the date already exist");
			}
		}
		date(id);
	}

	public static void validate(Long id) {
		Event event = Event.findById(id);
		if (event.status) {
			notFound();
		}
		render(event);
	}

	public static void openEvent(Long id) {
		Event event = Event.findById(id);
		if (event.status) {
			notFound();
		}
		event.status = true;
		event.save();
		show(id);
	}

	public static void vote(@Required Long id, @Required String name, List<Long> eventDateId) {
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			Event event = Event.findById(id);

			// check if name already exists
			if (event.listPeople.contains(new People(name))) {
				validation.addError("nameAlreadyExist", "'%2$s' has already vote", name);
				validation.keep();
			} else {
				People people = new People(name);
				event.listPeople.add(people);
				event.save();

				if (eventDateId != null) {
					for (EventDate eventDate : event.listEventDate) {
						if (eventDateId.contains(eventDate.id)) {
							eventDate.listPeople.add(new People(name));
						}
					}
				}
				event.save();
			}
		}
		show(id);
	}
}
