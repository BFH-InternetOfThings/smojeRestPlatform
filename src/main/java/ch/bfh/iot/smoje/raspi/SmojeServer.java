package ch.bfh.iot.smoje.raspi;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ServerProperties;

public class SmojeServer {
	
	public final static Logger 		logger 				= Logger.getLogger(SmojeServer.class);
	public final static Configuration config = new Configuration();

    public static void main(String[] args) throws Exception {
    	
        // URL: http://localhost:8080/smoje/sensors/
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletHolder jerseyServlet = context.addServlet(org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(ServerProperties.PROVIDER_CLASSNAMES, SmojeService.class.getCanonicalName());

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }

    }
}