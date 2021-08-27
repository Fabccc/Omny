module webframework {
	exports net.omny.route;
	exports net.omny.views;
	exports net.omny.test;
	exports net.omny.server;
	requires lombok;
	requires toml4j;
	requires org.apache.commons.lang3;
	requires com.google.gson;
	
	// Unit testing
	//requires org.junit.jupiter.engine;
	
}