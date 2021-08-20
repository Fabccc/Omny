package net.omny.views;

import java.util.Objects;

import net.omny.route.Response;

public class TextView implements View{

	private String text;
	
	public TextView(String text) {
		Objects.requireNonNull(text);
		this.text = text;
		if(!this.text.endsWith("\r\n")) {
			this.text = this.text+"\r\n";
		}
	}
	
	@Override
	public void write(Response res) {
		res.addBody(text);
	}

}
