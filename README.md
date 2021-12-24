# Omny Web Framework

Framework java pour serveur web léger

### Exemple de code

```java

class HTTPServer{
    public static void main(String[] args) {
		launch(new HTTPServer());
	}

	public HTTPServer() {
		super("./conf.toml");
	}

    @HTTP(url = "/")
	public Route indexRoute = new FileRoute("index.html");

    @HTTP(url = "/test")
	public View index(Request req, Response res) {
		res.setHeader("Content-Type", "text/plain");
		return new TextView("This is a test!");
	}

	@Override
	public void route(Router router) {
		router.route(this);
		router.staticRoute("./static"); // Static file routing (eg: png, jpg, xml files)
	}
}    
```
