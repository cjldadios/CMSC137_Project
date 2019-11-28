/**
* The PlayerListenerThread class extends the
* the class Thread.
* 
* It listens to Client requests.
*
*
* @author  cjldadios
* @version 1.0
* @since   2019 
*/

import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class PlayerListenerThread extends Thread {
    private ServerSocket serverSocket;
    private Socket clientSocket; // player may be referred as client
    private Integer playerId;
    private Server server;

    public PlayerListenerThread(
        ServerSocket serverSocket,
        Socket clientSocket,
        int playerId,
        Server server) {

        this.server = server;
        this.playerId = playerId;
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
        System.out.println("Client from "
            + this.clientSocket.getRemoteSocketAddress() 
            + " has connected on "
            + this.serverSocket.getLocalPort() + ".");
        System.out.println("Instanciating a PlayerListenerThread");
    }

	@Override
	public void run() {
		while(true) {
            String received = receiveBytes();
            if (received.equals("x")) {
                System.out.println("Player no. " + playerId + " finished");
                // Send stats
                break;
            }
            System.out.println("\nThread listener received: " + received);

            // Save the received to the server
            this.server.playerSubmissionsList.add(received);
                // received string is in format <playerId>:<8bytesEntry>
		}
	}

    public byte[] readBytes() {
        try {
            System.out.println("Receiving bytes from player no. "
                + this.playerId + " " + this.clientSocket + ".");

            DataInputStream dataInputStream = new DataInputStream(
                this.clientSocket.getInputStream());
                    // A stream is a smaller river.
            int dataLength = dataInputStream.readInt();
                // the next four bytes of this input stream,
                // interpreted as an int.
            byte[] dataByteArray = new byte[dataLength];
            if (dataLength > 0) {
                dataInputStream.readFully(dataByteArray);
            }
            
            return dataByteArray;

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    public void sendBytes(byte[] dataByteArray,
                                    int dataStart, int dataLength) {
        try {
            System.out.println("Sending bytes to client "
                + this.clientSocket + ".");

            DataOutputStream dataOutputStream = new DataOutputStream(
                this.clientSocket.getOutputStream());
                    // A stream is a smaller river.
            dataOutputStream.write(dataByteArray, dataStart, dataLength);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns player id and what the player sent, separated by colon
    private String receiveBytes() {
        String playerIdColonActionString = "";
        try {
            System.out.println("Receiveing bytes from Player "
                + clientSocket + "...");

            DataInputStream dataInputStream = new DataInputStream(
                this.clientSocket.getInputStream());
                    // A stream is a smaller river.

            // System.out.println("PlayerListenerThread: Hello?");

            // the next four bytes of this input stream,
            // interpreted as an int.
            // Sever sent eight bytes, sample = "KH4SAC2D"

            boolean doneReading = false;
            System.out.println("Player Thread Reading is waiting for " 
                + "player no. " + playerId +" submission");
            // hread.sleep(1000);


            int fourBytesPart1of2 = 0;
            int fourBytesPart2of2 = 0;

            while (!doneReading) {
                try {
                    fourBytesPart1of2 = dataInputStream.readInt();
                    fourBytesPart2of2 = dataInputStream.readInt();
                    doneReading = true;
                } catch (Exception e) {
                    // e.printStackTrace();
                    System.out.println("Player no. " + playerId
                            + " is waiting for others.");
                        
                    try {
                        Thread.sleep(1000);
                        // System.out.print("*");
                        System.out.println("Player no. " + playerId
                            + " exited.");
                        // System.out.println("Ending game...");
                        // System.exit(0);
                        // doneReading = true;

                        // return "x";
                    } catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }

            // System.out.println("Here?");

            byte[] receivedBytesPart1 =  ByteBuffer.allocate(4)
                                        .putInt(fourBytesPart1of2).array();
            byte[] receivedBytesPart2 =  ByteBuffer.allocate(4)
                                        .putInt(fourBytesPart2of2).array();
            
            String part1String = new String(receivedBytesPart1);
            String part2String = new String(receivedBytesPart2);

            String completeReceivedDataString = part1String + part2String;
            // System.out.println("completeReceivedDataString: " 
                // + completeReceivedDataString);

            playerIdColonActionString =  this.playerId.toString()
                + ":" + completeReceivedDataString;

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("End of line exception!");
        }

        return playerIdColonActionString;
    } // End receiveBytes()
}

/*
* readBytes() and sendBytes() was adopted from
*   https://stackoverflow.com/questions/2878867/
how-to-send-an-array-of-bytes-over-a-tcp-connection-java-programming
*   This was used to send data in bytes, not in UTF.
*/