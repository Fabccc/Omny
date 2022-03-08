package net.omny.views;

import java.util.Objects;

import lombok.Getter;
import net.omny.route.Response;
import net.omny.utils.HTTPUtils.Headers;

@Getter
public class TextView implements View{

	private String text;
	
	public TextView(String text) {
		Objects.requireNonNull(text);
		this.text = text;
	}
	
	@Override
	public void write(Response res) {
        res.setHeader(Headers.CONTENT_LENGTH, String.valueOf(text.getBytes().length));
		res.addBody(text);
	}


	@Override
	public String toString() {
		return "{" +
			" text='" + getText() + "'" +
			"}";
	}


}
