
import java.io.*;
import java.net.*;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Server extends Application {
	@Override // Override the start method in the Application class
	public void start(Stage primaryStage) {
		// Text area for displaying contents
		TextArea ta = new TextArea();

		// Create a scene and place it in the stage
		Scene scene = new Scene(new ScrollPane(ta), 450, 200);
		primaryStage.setTitle("Server"); // Set the stage title
		primaryStage.setScene(scene); // Place the scene in the stage
		primaryStage.show(); // Display the stage

		new Thread(() -> {
			try {
				// Create a server socket
				ServerSocket serverSocket = new ServerSocket(8000);
				Platform.runLater(() -> ta.appendText("Server started at " + new Date() + '\n'));

				// Listen for a connection request
				Socket socket = serverSocket.accept();

				// Create data input and output streams
				DataInputStream inputFromClient = new DataInputStream(socket.getInputStream());
				DataOutputStream outputToClient = new DataOutputStream(socket.getOutputStream());

				while (true) {
					// Receive number from the client
					double number = inputFromClient.readDouble();

					boolean isPrime = checkPrime(number);

					// Send area bool to the client
					outputToClient.writeBoolean(isPrime);

					Platform.runLater(() -> {
						ta.appendText("Number received from client: " + number + '\n');
						if (isPrime == false) {
							ta.appendText(number + " is not a prime number" + '\n');
						} 
						if (isPrime == true) {
							ta.appendText(number + " is a prime number" + '\n');
						}
					});
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}).start();
	}

	/**
	 * The main method is only needed for the IDE with limited JavaFX support.
	 * Not needed for running from the command line.
	 */

	// check prime method
	static boolean checkPrime(double number) {
		double remainder = 0;
		boolean isPrime = true;

		if (number <= 1) {
			isPrime = false;
		}
		
			for (double i = 2; i <= number-1; i++) {
				remainder = number % i;
				
				if (remainder == 0) {
					isPrime = false;
					break;
				}
			}
//		}
		return isPrime;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
