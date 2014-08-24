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

/**
 *
 * @author Michael
 */
public class Room {
    private Player P1;
    private Player P2;
    private volatile int currentRound;
    public String RoomName = "";
    public volatile boolean isFull;
    private volatile boolean P1Aschente;
    private volatile boolean P2Aschente;
    
    private volatile boolean P1EnterChoice;
    private volatile String P1Choice;
    private volatile boolean P2EnterChoice;
    private volatile String P2Choice;
    
    private volatile boolean P1WinFlag;
    private volatile boolean P2WinFlag;
    
    private volatile boolean P1ReqStatus;
    private volatile boolean P2ReqStatus;
    
    private volatile boolean decisionAvailable;
    
    public Room(String roomName, Player hostPlayer) {
        RoomName = roomName;
        P1 = hostPlayer;
        isFull = false;
        currentRound = 1;
        P1Aschente = false;
        P2Aschente = false;
        P1EnterChoice = false;
        P2EnterChoice = false;
        P1WinFlag = false;
        P2WinFlag = false;
        P1ReqStatus = false;
        P2ReqStatus = false;
        decisionAvailable = false;
    }
    
    public void Join(Player joinPlayer) {
        P2 = joinPlayer;
        isFull = true;
    }
    
    public Player getOtherPlayer(Player requestingPlayer) {
        if(requestingPlayer.equals(P1)) {
            return P2;
        } else {
            return P1;
        }
    }
    
    public int getPlayTo() {
        return ServerVar.PlayTo;
    }
    
    public void Aschente(Player whoAschente) {
        if(whoAschente.equals(P1)) {
            P1Aschente = true;
        } else {
            P2Aschente = true;
        }
    }
    
    public boolean isBothAschente() {
        return (P1Aschente && P2Aschente);
    }
    
    public void enterChoice(Player whoEntered, String choice) {
        if (whoEntered.equals(P1)) {
            P1EnterChoice = true;
            P1Choice = choice;
            System.out.println(P1.getPlayerName()+" choose: "+choice);
        } else {
            P2EnterChoice = true;
            P2Choice = choice;
            System.out.println(P2.getPlayerName()+" choose: "+choice);
        }
    }
    
    private void resetFlags() {
        P1EnterChoice = false;
        P2EnterChoice = false;
        P1WinFlag = false;
        P2WinFlag = false;
        P1ReqStatus = false;
        P2ReqStatus = false;
        decisionAvailable = false;
    }
    
    public boolean hasBothChoosen() {
        return (P1EnterChoice && P2EnterChoice);
    }
    
    public void makeDecision() {
        if(P1Choice.equals("NOTHINGSELECTED") && P2Choice.equals("NOTHINGSELECTED")) {
            P1WinFlag = true;
            P2WinFlag = true;
        } else if (P1Choice.equals("NOTHINGSELECTED") && !P2Choice.equals("NOTHINGSELECTED")) {
            P2WinFlag = true;
        } else if (!P1Choice.equals("NOTHINGSELECTED") && P2Choice.equals("NOTHINGSELECTED")) {
            P1WinFlag = true;
        }
        
        else if (P1Choice.equals("ROCK") && P2Choice.equals("ROCK")) {
            P1WinFlag = true;
            P2WinFlag = true;
        } else if (P1Choice.equals("ROCK") && P2Choice.equals("SCISSOR")) {
            P1WinFlag = true;
        } else if (P1Choice.equals("ROCK") && P2Choice.equals("PAPER")) {
            P2WinFlag = true;
        }
        
        else if (P1Choice.equals("SCISSOR") && P2Choice.equals("SCISSOR")) {
            P1WinFlag = true;
            P2WinFlag = true;
        } else if (P1Choice.equals("SCISSOR") && P2Choice.equals("ROCK")) {
            P2WinFlag = true;
        } else if (P1Choice.equals("SCISSOR") && P2Choice.equals("PAPER")) {
            P1WinFlag = true;
        } 
        
        else if (P1Choice.equals("PAPER") && P2Choice.equals("PAPER")) {
            P1WinFlag = true;
            P2WinFlag = true;
        } else if (P1Choice.equals("PAPER") && P2Choice.equals("ROCK")) {
            P1WinFlag = true;
        } else if (P1Choice.equals("PAPER") && P2Choice.equals("SCISSOR")) {
            P2WinFlag = true;
        }
        
        if (P1WinFlag == true && P2WinFlag == true) {
                System.out.println("DRAW");
        } else if (P1WinFlag == true && P2WinFlag == false) {
                System.out.println(P1.getPlayerName()+" WINS");
        } else if (P1WinFlag == false && P2WinFlag == true) {
                System.out.println(P2.getPlayerName()+" WINS");
        }
        
        decisionAvailable = true;
        

    }
    
    public String requestWinningStatus (Player whoRequested) {
        String retval;
        if (whoRequested.equals(P1)) {
            if (P1WinFlag == true && P2WinFlag == true) {
                retval = "DRAW";
            } else if (P1WinFlag == true && P2WinFlag == false) {
                retval = "WIN";
            } else {
                retval = "LOSE";
            }
            P1ReqStatus = true;
        } else {
            if (P1WinFlag == true && P2WinFlag == true) {
                retval = "DRAW";
            } else if (P2WinFlag == true && P1WinFlag == false) {
                retval = "WIN";
            } else {
                retval = "LOSE";
            }
            P2ReqStatus = true;
        }
        if(P1ReqStatus && P2ReqStatus) {
            resetFlags();
        }
        return retval;
    }
    
    public boolean isDecisionAvailableYet() {
        return decisionAvailable;
    }
}
