# Omny Web Framework

Java lightweight web serverside framework

### TODO

- [x] HTTP Request handling
- [x] HTTP Response
- [x] Files type
  - [x] Text files
  - [x] Binary files
  - [x] JSON from Object
- [ ] Method
  - [x] GET
  - [ ] POST
  - [ ] PUT
  - [ ] DELETE
- [ ] File routing
  - [x] Static files routing
  - [x] Default file routing (from a Router class)
  - [ ] Param file routing
- [ ] Encoding
  - [ ] GZIP
  - [ ] Deflate
- [ ] Optimizing
  - [x] Cache request
  - [x] Fast cache lookup
  - [ ] More ??..
- [ ] Features from the framework
  - [ ] Namespaces
  - [ ] More ...


## Microbenching test can be found under `jmh-test/` folder
[Microbenching README.MD](jmh-test/README.md)
## Example

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

