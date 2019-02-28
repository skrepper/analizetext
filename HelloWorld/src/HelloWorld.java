
public class HelloWorld extends Thread{

	public void run() {
		System.out.println("Hello from thread");
		String path = "127.0.0.1";
		System.out.println(path.substring(0, path.lastIndexOf( '.' )));
	}
	
	public static void main(String[] args) {
		(new HelloWorld()).start();;

	}

}
