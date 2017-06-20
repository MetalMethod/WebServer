package org.academiadecodigo.bootcamp8.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class implements a multithreaded web server.
 *
 *When in execution, it aways listens for new requests at the designated PORT
 * and created a new thread for that request. Old unused threads are reused.
 *
 * Created by Igor Busquets on 14/06/17.
 */
public class Server {

    public static final int PORT = 9090;

    private static final Logger logger = Logger.getLogger(Server.class.getName());

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(Server.PORT);

            System.out.println("\n###############################################################");
            System.out.println("#####        Java Multithreaded Web Server started        #####");
            System.out.println("##### Done @ AcademiaDeCodigo.org - bootcamp8 - june/2017 #####");
            System.out.println("#####      written by Igor Busquets - version 0.0.4       #####");
            System.out.println("###############################################################");
            System.out.println("#####         Waiting for connections at port "
                    + serverSocket.getLocalPort()+"        #####");
            System.out.println("###############################################################\n");

            while (true) {

                //init new socket for every request
                Socket clientSocket = serverSocket.accept();
                logger.log(Level.INFO, "new connection from " + clientSocket.getInetAddress());


                //strong thread pool creation
                ExecutorService cachedPool = Executors.newCachedThreadPool();

                cachedPool.submit(new Provider(clientSocket));

                System.out.println("New request received. Running Threads: " + Thread.activeCount());
            }

        } catch (IOException e) {
            System.out.println("\n#####    Could not start the server.    #####\n\n" +
                    "Please check:\n\nif there isn't another server process running\nor if the port "
                    + Server.PORT
                    +" is free for use.");
            System.out.println("\n#############################################\n");
            System.exit(1);
        }
    }
}
