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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class ServerThread extends Thread {

    private final Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private String Query;

    public ServerThread(Socket socket) {
        this.socket = socket;
        start();
    }

    public void run() {
        try {
            System.out.println("New client connected" + socket.getInetAddress());
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                Query = (String) Receive();
                if (Query.equals("USERNAMEINPUT")) {
                    UserLoginHandler();
                }
            }
        } catch (Throwable t) {
            System.out.println("Caught " + t + " - closing thread");
            //client disconnected
        }
    }

    private void Send(Object toSend) throws IOException {
        out.writeObject(toSend);
        out.flush();
        out.reset();
    }

    private Object Receive() throws IOException {
        try {
            return in.readObject();
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    private void UserLoginHandler() {
        try {
            /* UserLogin.java handler */
            Send("ACK");
            String userName = (String) Receive();
            System.out.println(userName);
            //username checking
            /* OathScreen.java handler */
            Send("OK");
        } catch (Throwable t) {
            System.out.println("Caught " + t + " - closing thread");
            //client disconnected
        }
    }
}
