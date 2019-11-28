
import java.lang.Thread;
import java.util.ArrayList;
import java.net.Socket;
import java.io.IOException;
import java.net.ServerSocket;


public class PlayerAcceptingThread extends Thread {
	private ArrayList<Thread> playerListenerThreadList;
	private ArrayList<Socket> socketConnectionList;
	private ServerSocket serverSocket;
	private Server server;

	private int connectionCount = 0;

	public PlayerAcceptingThread(
		ArrayList<Thread> playerListenerThreadList,
		ArrayList<Socket> socketConnectionList,
		ServerSocket serverSocket,
		Server server
		) {

		System.out.println("Instanciating Server.PlayerAcceptingThread");

		this.playerListenerThreadList = playerListenerThreadList;
		this.socketConnectionList = socketConnectionList;
		this.serverSocket = serverSocket;
		this.server = server;
	}

	public void run() {
		/*
        * Instanciate threads listening for connecting players
        * Wait for every connections
        * Then instanciate the PlayerListenerThread
        */
		// System.out.println("Server.PlayerAcceptingThread is running...");

        try {
	        for (int i=0; i < Server.MAX_PLAYERS; i+=1) {
	        	int index = i + 1;
	        	// System.out.println("Waiting for player connection #"
	        		// + index + "...");
        		// a player connects
	        	Socket clientSocket = serverSocket.accept();
	        		// Record this connection...

	        	// System.out.println("Player connection #"
	        		// + index + " accepted.");

	        	/* Update the ArrayList of PlayerListenerTread
	        	* which is the same variable shared with the Server
	        	*/
	        	// System.out.println("Adding new Server.PlayerListenerThread" 
	        		// + "for player #" + index);
	        	PlayerListenerThread playerListenerThread
	        		= new PlayerListenerThread(serverSocket, clientSocket,
	        			index, this.server);
	        	playerListenerThreadList.add(playerListenerThread);

	        	/* Update Server's socketConnectionList, <- same as...
	        	* which just records all the players by their socket.
	        	* This is the same as this.socketConnectionList <- ...this one
	        	*/
	        	socketConnectionList.add(clientSocket);
	        		// ...here
	        	
	        	System.out.println("Player " + index + " joined.");

	        	connectionCount += 1;
	        }
	    } catch (IOException e) {
	    	// e.printStackTrace();
	    	System.out.println("Server timed out!");
	    	System.exit(0);
	    } finally{
		    // System.out.print("The server is now full.");
		    // System.out.print(" From now on, the Server's PlayerAcceptingThread");
		    // System.out.println(" will stop accepting player connections!");
	    }
	}

	public int getConnectionCount() {
		return connectionCount;
	}
}

	