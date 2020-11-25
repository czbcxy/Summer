package com.summer.context.support;


import com.summer.context.SunApplicationContext;
import com.summer.webmvc.SunDispatcherSevlet;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class SunWebApplicationContext extends SunGenericApplicationContext {

    private static final String PORT = "summer.server.port";
    private static final String DEFAULT_PORT = "8080";

    public SunWebApplicationContext() {
    }

    public SunWebApplicationContext(Class<?>[] primarySource) {
        bootApplication = primarySource[0];
    }

    public static SunApplicationContext run(Class<?> primarySource, String... args) throws Exception {
        return run(new Class<?>[]{primarySource}, args);
    }

    private static SunApplicationContext run(Class<?>[] primarySource, String... args) throws Exception {
        return new SunWebApplicationContext(primarySource).run(args);
    }

    private SunApplicationContext run(String... args) throws Exception {
        SunDispatcherSevlet.doRefresh();
        CountDownLatch latch = new CountDownLatch(1);
        Thread thread = new Thread(() -> {
            try {
                createServer(latch);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                latch.countDown();
            }
        });
        thread.start();
        latch.await();
        return SunDispatcherSevlet.applicationContext;
    }

    public static void createServer(CountDownLatch latch) throws Exception {
        Server server = new Server();
        server.setStopAtShutdown(true);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(Integer.parseInt(Objects.isNull(config.getProperty(PORT)) ? DEFAULT_PORT : config.getProperty(PORT) ));
        connector.setReuseAddress(true);
        server.setConnectors(new Connector[]{connector});
        ServletContextHandler context = new ServletContextHandler(server, "/");
        server.setHandler(context);
        context.addServlet(SunDispatcherSevlet.class, "/*");
        server.stop();
        server.start();
        latch.countDown();
        server.join();
    }
}
