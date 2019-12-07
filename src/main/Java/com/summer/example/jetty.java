package com.summer.example;


import org.eclipse.jetty.server.Server;

public class jetty {

    public static void main(String[] args) throws Exception{
        Server server = new Server(8090);
        server.start();
        server.dumpStdErr();
        server.join();
    }
}
