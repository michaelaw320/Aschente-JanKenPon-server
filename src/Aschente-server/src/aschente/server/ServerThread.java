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
import java.util.ArrayList;
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
    private String userName;
    private String roomName;
    private boolean isHost;
    private Player P;
    private Room currentRoom;

    public ServerThread(Socket socket) {
        this.isHost = false;
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
                switch (Query) {
                    case "USERNAMEINPUT":
                        UserLoginHandler();
                        break;
                    case "CREATEROOM":
                        CreateRoomHandler();
                        break;
                    case "REFRESHROOMLIST":
                        RefreshListHandler();
                        break;
                    case "JOINROOM":
                        JoinRoomHandler();
                        break;
                    case "REFRESHGAMEDATA":
                        RefreshGameDataHandler();
                        break;
                    case "ASCHENTE!":
                        AschenteHandler();
                        break;
                }
            }
        } catch (Throwable t) {
            //client disconnected, cleanup
            System.out.println("Client Disconnected"+socket.getInetAddress());
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println(ex);
            }
            if(userName != null) P.updatePlayer();
            if (isHost) {
                for(int i = 0; i < ServerVar.RoomList.size();i++) {
                    if(ServerVar.RoomList.get(i).RoomName.equals(roomName)) {
                        ServerVar.RoomList.remove(i);
                        break;
                    }
                }
            }
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
    
    private void UserLoginHandler() throws IOException {
            /* UserLogin.java handler */
            Send("ACK");
            userName = (String) Receive();
            P = new Player(userName);
            Send(P.getPlayerScore());
            //username checking
            /* OathScreen.java handler */
            Send("OK");
    }
    
    private void CreateRoomHandler() throws IOException {
            Send("ACK");
            roomName = (String) Receive();
            System.out.println(roomName);
            currentRoom = new Room(roomName, P);
            ServerVar.RoomList.add(currentRoom);
            isHost = true;
            while (currentRoom.isFull == false) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    
                }
            }
            Send("GAMESTART");
    }
    
    private void RefreshListHandler() throws IOException {
        ArrayList<String> RoomNameList = new ArrayList<>();
        for (Room RoomList : ServerVar.RoomList) {
            RoomNameList.add(RoomList.RoomName);
        }
        Send(RoomNameList);
    }
    
    private void JoinRoomHandler() throws IOException {
        Send("ACK");
        roomName = (String) Receive();
        System.out.println(roomName);
        int i = 0;
        boolean stop = false;
        currentRoom = null;
        while (!stop && i < ServerVar.RoomList.size()) {
            if(ServerVar.RoomList.get(i).RoomName.equals(roomName)) {
                stop = true;
                currentRoom = ServerVar.RoomList.get(i);
            } else {
                i++;
            }
        }
        if(currentRoom != null) currentRoom.Join(P);
    }
    
    private void RefreshGameDataHandler() throws IOException {
        System.out.println(currentRoom.getOtherPlayer(P).getPlayerName());
        System.out.println(currentRoom.getOtherPlayer(P).getPlayerScore());
        Send(currentRoom.getOtherPlayer(P).getPlayerName());
        Send(currentRoom.getOtherPlayer(P).getPlayerScore());
        Send(ServerVar.PlayTo);
        
    }
    
    private void AschenteHandler() throws IOException {
        currentRoom.Aschente(P);
        System.out.println("WAITING");
        while(!currentRoom.isBothAschente()) {
            //wait
            System.out.println("WAITING");
        }
        Send("ASCHENTE!");
    }
    
}
