import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
 
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.SessionHandler;
 
public class HellloWorld extends AbstractHandler
{
	private String id = new String("Start value");
	private AtomicInteger count = new AtomicInteger(10);
	private boolean create = false;
	private HttpSession session;
	private Server loc_server;
	
	public HellloWorld(Server server) {
		loc_server = server; 
	}
	
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response) 
        throws IOException, ServletException
    {

        DefaultSessionIdManager sessionIdManager = new DefaultSessionIdManager(loc_server);
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setSessionIdManager(sessionIdManager);
        baseRequest.setSessionHandler(sessionHandler);
        session = baseRequest.getSession(true);
    	
    	//new SoutMap().printMap(baseRequest);
    	response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        if(baseRequest.getParameterMap().containsKey("id")!=false) 
        {
        	id=baseRequest.getParameter("id").toString();
        } 
        else 
        {
        	id = Integer.toString(count.incrementAndGet());
			System.out.println("ќпа, что-то не так");
        };
        baseRequest.setHandled(true);
        PageGenerator pageGenerator = new PageGenerator();
        PrintWriter pw = response.getWriter();
        pw.println(pageGenerator.getPage(id));
        pw.println("Create = " + create);
        if (session == null) {
        	pw.println("no session");
        } else {
        	pw.println("Session = " + session.getId());
        	pw.println("Created = " + session.getAttribute("created"));
        }      
    }
 
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8080);
        
        ContextHandler context = new ContextHandler();
        context.setContextPath( "/hello" );
        context.setHandler( new HellloWorld(server) );
        context.setWelcomeFiles(new String[] { "index.html", "index.htm", "index.jsp" });
        

        WebAppContext webAppContext = new WebAppContext();
        webAppContext.setResourceBase("src/jcgresources");
        webAppContext.setContextPath("/rest");

        HandlerCollection handlers = new HandlerCollection();
        handlers.addHandler(context);
        handlers.addHandler(webAppContext);
        server.setHandler(handlers);
        
        server.start();
        server.join();
    }
}