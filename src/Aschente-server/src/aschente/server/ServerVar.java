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

import static aschente.server.AschenteServer.PORT;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Michael
 */
public class ServerVar {
    public static volatile ArrayList<Room> RoomList = new ArrayList<>(); 
    public static int PlayTo = 1;
    
    public static void loadPlayTo() {
        try {
            File playtofile = new File("Config\\playto.txt");
            Scanner sc = new Scanner(playtofile);
            PlayTo = sc.nextInt();
        } catch (FileNotFoundException ex) {
            System.err.println("File Config\\playto.txt is not found! using default playto 5");
            PlayTo = 5;
        }
    }
}
