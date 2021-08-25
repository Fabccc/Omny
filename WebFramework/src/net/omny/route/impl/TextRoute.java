package net.omny.route.impl;

import net.omny.route.Request;
import net.omny.route.Response;
import net.omny.route.Route;
import net.omny.views.TextView;
import net.omny.views.View;

public class TextRoute implements Route{

	private TextView view;

	public TextRoute(String text) {
		this.view = new TextView(text);	
	}

	@Override
	public View handle(Request req, Response res) {
		return this.view;
	}

	
	
}