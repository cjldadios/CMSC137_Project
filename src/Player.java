

import java.util.ArrayList;
import java.lang.Thread;
import java.net.Socket;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;


public class Player extends Thread {
	private ArrayList<String> cardList = new ArrayList<String>(4);

	Socket serverSocket;
	// Socket socket;

	private int playerId;

	// private ArrayList<String> cardAtHandStringList = new ArrayList<String>();
	private String cardsAtHandString = "";

	
	public Player(String serverName) throws IOException {
		System.out.println("Instanciating Player...");

		/* Open a ClientSocket and connect to ServerSocket */
        System.out.println("Connecting to " + serverName 
        	+ " on port " + Server.DEFAULT_PORT);
        
		//creating a new socket for client and binding it to a port
		this.serverSocket = new Socket(serverName, Server.DEFAULT_PORT);

        System.out.println("Just connected to " 
        	+ serverSocket.getRemoteSocketAddress());
        
		System.out.println("Player instanciated ");
	}

	public Player(
		int playerId, ArrayList<String> cardList, String serverName) {
		System.out.println("Unused instanciator of Player");
		this.playerId = playerId;
		this.cardList = cardList;
	}

	@Override
	public void run() {
		System.out.println("Player Thread running as invoked from main...");

		initializeCards();

		System.out.println("Player exited the game.");
	}

	private void initializeCards() {

		System.out.println("Player is initializingCards");
		System.out.println("Player is waiting" 
			+ " for Server card initialization...");
		/* Wait for server to send cards in byte array format */

        try {
            System.out.println("Receiveing bytes from server "
                + serverSocket + "...");

            DataInputStream dataInputStream = new DataInputStream(
                this.serverSocket.getInputStream());
                    // A stream is a smaller river.

            // the next four bytes of this input stream,
            // interpreted as an int.
            // Sever sent eight bytes, sample = "KH4SAC2D"
	        int fourBytesPart1of2 = dataInputStream.readInt(); // get "KH4S"
	        int fourBytesPart2of2 = dataInputStream.readInt(); // get "AC2D"

            byte[] receivedBytesPart1 =  ByteBuffer.allocate(4)
            							.putInt(fourBytesPart1of2).array();
            byte[] receivedBytesPart2 =  ByteBuffer.allocate(4)
            							.putInt(fourBytesPart2of2).array();
          	
            String part1String = new String(receivedBytesPart1);
            String part2String = new String(receivedBytesPart2);

            String completeReceivedDataString = part1String + part2String;
            // System.out.println("completeReceivedDataString: " 
            	// + completeReceivedDataString);

            System.out.println("Saving received cards: "
            	+ completeReceivedDataString + "...");
            this.cardsAtHandString = completeReceivedDataString;

            // //dataByteArray;
            //      ^          ...to be interpreted as the initialized cards

        } catch (IOException e) {
            // e.printStackTrace();
            // System.out.println("End of line exception!");
            System.out.println("The server stopped!");
            System.out.println("Server failed to send initializing cards!");
        }

        System.out.println("Player initialized with ...something...cards."
                + "");
	}
}