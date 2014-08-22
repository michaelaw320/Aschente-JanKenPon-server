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
import java.util.ArrayList;

/**
 *
 * @author Michael
 */
public class Room {
    private Player P1;
    private Player P2;
    private int currentRound;
    public String RoomName = "";
    public boolean isFull;
    
    public Room(String roomName, Player hostPlayer) {
        RoomName = roomName;
        P1 = hostPlayer;
        isFull = false;
        currentRound = 1;
    }
    
    public void Join(Player joinPlayer) {
        P2 = joinPlayer;
    }
    
    public int getPlayTo() {
        return ServerVar.PlayTo;
    }
    
}
