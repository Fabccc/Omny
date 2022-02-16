package net.omny.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import net.omny.route.Router;
import net.omny.server.WebServer;

/**
 * Je dois pouvoir écrire un code comme celui-ci :
 * <pre>
 * WebServer ws = WebServer.builder()
 * 	.route("/", "index.html")
 * 	.route("/test", "test.html")
 * 	.static("/assets")
 *  .listen(8080, err -> {});
 * </pre>
 * Ou bien
 * <pre>
 * class Web extends WebServer{
 * 
 * 	main(String[] args){
 * 		WebServer.launch(new Web());
 * 	}
 * 
 * 	Web(){
 * 		super("conf.toml");
 * 	}
 * 	
 * 	@Override
 * 	void route(Router router){
 * 		router.route(new CustomRoute());
 *  }
 * }
 * 
 * class CustomRoute{
 * 	@Get(url = "/")
 * 	public View index(){
 *  	return new FileView("index.html");
 * 	}
 * 	@Get(url = "/test")
 * 	public View test(){
 * 		return new FileView("test.html");
 * 	}
 * }
 * </pre>
 * 
 * et que ça marche
 * @author Fabien CAYRE (Computer)
 *
 *
 * @date 08/08/2021
 */
public class WebTest extends WebServer{

	private static final ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(4);
	
	public static void main(String[] args) {
		launch(new WebTest());
	}
	
	public WebTest() {
		super("conf.toml", SERVICE, 4);
	}

	@Override
	public void route(Router router) {
		//router.route("/", FileRoute.);
	}
	
	
}
