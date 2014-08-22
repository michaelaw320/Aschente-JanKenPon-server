/*
 * Copyright (C) 2014 Michael
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package aschente.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class AschenteServer {

    public static int PORT;

    public AschenteServer(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        System.out.println("SERVER STARTED");
        while (true) {
            new ServerThread(ss.accept());
        }
    }

    public static void main(String[] args)  {
        //read and set server port here
        try {
            File portfile = new File("Config\\port.txt");
            Scanner sc = new Scanner(portfile);
            PORT = sc.nextInt();
        } catch (FileNotFoundException ex) {
            System.err.println("File Config\\port.txt is not found! using default port 45373");
            PORT = 45373;
        } finally {
            try {
                System.out.println("STARTING SERVER");
                new AschenteServer(PORT);
            } catch (IOException ex) {
                System.err.println("CANNOT START SERVER");
                System.exit(1);
            }
        }

    }

}