
public class PageGenerator {
	
	private String s = new String("<html><head>"
			+ "<script>"
			+ "function refresh(){"
			+ "var idid = document.getElementById('idid');"
			+ "document.myform.submit();"
			+ "}</script></head>"
			+ "<body onload='setInterval(function(){refresh1()})'>"
			+ "<h1>Hello World</h1>"
			+ "<p>Id=%1$s</p>"
			+ "<form name='myform' method='post'>"
			+ "%2$s"
			+ "<p><input type='submit' value='Отправить'></p>"
			+ "</form>"
			+ "</body>"
			+ "</html>");

	public String getPage(String id) {
		String inpid;
		inpid = String.format("<p><input type='text' value='%s' name='id' id='idid'></p>", id);
		return String.format(s, id,inpid);
	}
}
