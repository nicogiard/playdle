package controllers;

import play.*;
import play.data.validation.Valid;
import play.mvc.*;

import java.util.*;

import models.*;

public class Application extends Controller {

	public static void index() {
		// Only 5 last events
		List<Event> events = Event.find("").fetch(5);
		render(events);
	}

	public static void create(@Valid Event event) {
		// create the new event
		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
		} else {
			event.save();
		}
		index();
	}
	
	public static void events() {
		// All events
		List<Event> events = Event.findAll();
		render(events);
	}

	public static void show(Long id) {
		// show the event identified by id 
		Event event = Event.findById(id);
		notFoundIfNull(event);
		render(event);
	}
}
