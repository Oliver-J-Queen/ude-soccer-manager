package org.main;

import java.io.IOException;

import static org.clientconnection.RequestHelper.*;

public class ConnectionTestMain {
    public static void main(String [] args) throws IOException, InterruptedException {
        String name = "Yelle";
                String password = "1234567";

        String result = login(name, password);
        System.out.println(result);

    }
}
