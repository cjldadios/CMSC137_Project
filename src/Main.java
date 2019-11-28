
import java.util.Scanner;
import java.io.IOException;

public class Main {
	public static final String HOST_IP = new String("10.11.28.24");
	public static final int SECONDS = 1000; // 1 thousand milliseconds

	public static void main(String args[]) {


		// String serverName = args[0]; //get IP address of server from first param
        // int port = Integer.parseInt(args[1]); //get port from second param

		System.out.println("Use ifconfig to see the correct host IP address.");
		final Scanner scanner = new Scanner(System.in);
		System.out.print("Host a game [y/n]?: ");
		String hosting = scanner.next();
		boolean isHost = true;
		if(hosting.equals("y") || hosting.equals("Y")) {
			isHost = true;
		} else if(hosting.equals("n") || hosting.equals("N")) {
			isHost = false;
		} else {
			System.out.println("Invalid input!");
			System.exit(0);
		}

		if(isHost) {
			// Instanciate server
			try {
				Server server = new Server();
				server.start();
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("Default port unavailable!");
			}

		} else {
			// Instanciate player
			//System.out.print("Enter server IP address: ");
			//String scannedIP = scanner.next();
		
			try {
				// Player player = new Player(scannedIP); 
				Player player = new Player(HOST_IP);
				System.out.println("Main started the player thread to run...");
				player.start();
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("Server not running!");
			}						
		}
	}
}

/*

		try{
            int port = Integer.parseInt(args[0]);
            Thread t = new ChatServer(port);
            t.start();
        }catch(IOException e){
            //e.printStackTrace();
            System.out.println("\nUsage: java ChatServer <port-no.>\n"+
                    "Make sure to use valid ports (greater than 1023)");
        }catch(ArrayIndexOutOfBoundsException e){
            //e.printStackTrace();
            System.out.println("\nUsage: java ChatServer <port-no.>\n"+
                    "Insufficient arguments given.");
        }
*/