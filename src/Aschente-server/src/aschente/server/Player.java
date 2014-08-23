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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael
 */
public class Player {

    private String name;
    private int score;
    private boolean isNew;

    private int idx = -999; /*for update purpose only*/


    public Player() {
        name = "";
        score = 0;
        isNew = false;
    }

    public Player(String playerName) {
        name = playerName;
        if (queryPlayer()) {
            isNew = false;
        } else {
            isNew = true;
            score = 0;
        }
        System.out.println();
        System.out.println("Name: " + name);
        System.out.println("Score: " + score);
        System.out.println("isNew?: " + isNew);
        System.out.println();
    }
    
    public String getPlayerName() {
        return name;
    }
    
    public int getPlayerScore() {
        return score;
    }

    private synchronized boolean queryPlayer() {
        File highscore = new File("highscore.txt");
        boolean found = false;
        try {
            Scanner sc = new Scanner(highscore);
            String[] processing;

            while (sc.hasNextLine() && !found) {
                processing = sc.nextLine().split(";");
                if (processing[0].equals(name)) {
                    //   score = Integer.parseInt(processing[1]);
                    score = 0;
                    isNew = false;
                    found = true;
                }
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Highscore.txt not found");
            highscore.createNewFile();
        } finally {
            return found;
        }
    }

    public synchronized void updatePlayer() {
        if (isNew) {
            try {
                File highscore = new File("highscore.txt");
                FileWriter fw = new FileWriter(highscore,true);
                BufferedWriter writer = new BufferedWriter(fw);
                String add = name + ";" + Integer.toString(score);
                writer.write(add);
                writer.newLine();
                writer.close();
            } catch (IOException ex) {
                    System.err.println(ex);
                }
        } else {
            if (isDifferent()) {
                try {
                    ArrayList<String> Data = new ArrayList<>();
                    String replacement = name + ";" + Integer.toString(score);

                    File highscore = new File("highscore.txt");
                    Scanner sc = new Scanner(highscore);

                    while (sc.hasNextLine()) {
                        Data.add(sc.nextLine());
                    }

                    Data.remove(idx);
                    Data.add(replacement);

                    FileWriter fw = new FileWriter(highscore);
                    BufferedWriter writer = new BufferedWriter(fw);

                    for (String Data1 : Data) {
                        writer.write(Data1);
                        writer.newLine();
                    }

                    writer.close();

                } catch (IOException ex) {
                    System.err.println(ex);
                }
            }
        }
    }

    private synchronized boolean isDifferent() {
        File highscore = new File("highscore.txt");
        boolean diff = true;
        try {
            Scanner sc = new Scanner(highscore);
            String[] processing;
            boolean stop = false;
            int i = 0;
            while (sc.hasNextLine() && !stop) {
                processing = sc.nextLine().split(";");
                if (processing[0].equals(name)) {
                    diff = score != Integer.parseInt(processing[1]);
                    stop = true;
                    idx = i;
                } else {
                    i++;
                }
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Highscore.txt not found");
            highscore.createNewFile();
        } finally {
            return diff;
        }
    }
}
