package net.omny.test;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.moandjiezana.toml.Toml;

public class TomlTest {

	
	
	@Test
	public void testTomlValue() {
		Toml toml = new Toml().read(new File("./test.toml"));
		
		assertEquals(8080, (long) toml.getLong("port"));
	}
	
}
