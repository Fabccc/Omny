package net.omny.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.moandjiezana.toml.Toml;

public class TomlTest {

	
	
	@Test
	public void testTomlValue() {
		Toml toml = new Toml().read(new File("./test.toml"));
		
		assertEquals(8080, toml.getLong("port"));
	}
	
}
