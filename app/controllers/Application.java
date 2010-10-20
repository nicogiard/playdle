package controllers;

import play.*;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

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

	public static void events() {
		// All events with status set to true
		List<Event> events = Event.find("status = true").fetch();
		render(events);
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
}
