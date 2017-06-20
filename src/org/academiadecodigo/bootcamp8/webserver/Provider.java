package org.academiadecodigo.bootcamp8.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

/**
 * This class implements the logic for the server.
 *
 * Requires a Server class with a ServerSocket with defined port and calling:
 * new Thread(new Provider(clientSocket)) for every new Thread created.
 *
 * Created by Igor Busquets on 14/06/17.
 */

public class Provider implements Runnable {

    private Socket clientSocket;

    public Provider(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {

            //Input for request header only
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader in = new BufferedReader(inputStreamReader);

            //Output as bytes for all files served.
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            // Asks to read the request header from the input stream,
            // parse and return the Resource folder into a string
            String requestResource = readRequest(in);

            // Serve the content from the resource folder
            serveResource(requestResource, out);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readRequest(BufferedReader in) throws IOException {

        String line = "";
        String requestResourcePath = "";

        //Reads first line of browser request and returns it
        if ((line = in.readLine()) != null && !line.isEmpty()) {

            requestResourcePath = line.split(" ")[1];
        }

        return requestResourcePath;
    }

    public void serveResource(String requestResource, DataOutputStream out) throws IOException {

        FileInputStream in = null;

        try {
            //IMAGE
            if (requestResource.equals("/eu.png")) {

                // get the image file content
                File file = new File("www/eu.png");

                in = new FileInputStream(file);

                //build and send the header response for this file
                String responseHeader =
                        "HTTP/1.0 200 Document Follows\r\n" +
                                "Content-Type: image/png\r\n" +
                                "Content-Length: " + file.length() + "\r\n\r\n";

                out.write(responseHeader.getBytes());
                // send image in bytes
                Files.copy(file.toPath(), out);
            }// end of IMAGE

            //INDEX or ROOT
            if (requestResource.equals("/") || requestResource.equals("/index.html")) {

                // get the html file content
                File file = new File("www/index.html");

                in = new FileInputStream(file);

                //build and send the header response for this file
                String responseHeader =
                                "HTTP/1.0 200 Document Follows\r\n" +
                                "Content-Type: text/html; charset=UTF-8\r\n" +
                                "Content-Length: " + file.length() + "\r\n\r\n";

                out.write(responseHeader.getBytes());

                //send the html content in bytes
                Files.copy(file.toPath(), out);

            }// end of INDEX

            //FILE NOT FOUND
            if (!requestResource.equals("/") && !requestResource.equals("/index.html")
                    && !requestResource.equals("/eu.png")) {

                // get the html file content
                File file = new File("www/error.html");

                in = new FileInputStream(file);

                //build and send the header response for this file
                String responseHeader =
                                "HTTP/1.0 404 Not Found\r\n" +
                                "Content-Type: text/html; charset=UTF-8\r\n" +
                                "Content-Length: " + file.length() + "\r\n\r\n";

                out.write(responseHeader.getBytes());

                //send the html content in bytes
                /*int numChar;
                while ((numChar = in.read()) != -1) {
                    out.write(numChar);
                }*/
                Files.copy(file.toPath(), out);
            }// end of FILE NOT FOUND

            //System.out.println("received new request with resource path:   " + requestResource);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (in != null) {
               in.close();
            }

            if (out != null) {
                out.close();
            }
        }
    }
}
