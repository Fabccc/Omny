module webframework {
	exports net.omny.route;
	exports net.omny.views;
	exports net.omny.test;
	exports net.omny;
	requires lombok;
	requires toml4j;
	
	// Unit testing
	requires org.junit.jupiter;
	requires org.junit.jupiter.api;
	//requires org.junit.jupiter.engine;
	requires org.junit.jupiter.params;
	
}