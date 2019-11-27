

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

	// private byte[];

	
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


            System.out.println("Wait!!");
            int dataLength = dataInputStream.readInt();
                // the next four bytes of this input stream,
                // interpreted as an int.

            byte[] receivedBytes =  ByteBuffer.allocate(4)
            							.putInt(dataLength).array();
            System.out.println("receivedBytes.toString(): "
            						+ receivedBytes.toString());
            System.out.println("receivedBytes: "
            						+ receivedBytes);


            // String string = new String(byteArrray);
            String string = new String(receivedBytes);
            System.out.println("String: " + string);


            System.out.println("Hold!");
            System.out.println("Data length: " + dataLength);
            byte[] dataByteArray = new byte[dataLength];
            System.out.println("It!");
            // if (dataLength > 0) {
            // 	System.out.println("DataLength > 0");
            //     dataInputStream.readFully(dataByteArray);
            // }

            System.out.println("Received dataByteArray: " + dataByteArray);
            
            //dataByteArray;
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