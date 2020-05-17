package jimmyhokkins.chat.theChat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class Server {
	
	private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();
  

	public static class ConnectionHandler extends Thread {		
		private Socket socket;    
		public ConnectionHandler(Socket socket) {
			this.socket = socket;
		}    
		private String serverHandshake(Connection connection) throws IOException, ClassNotFoundException {
			while(true) {				
				connection.send(new Message(MessageType.NAME_REQUEST));
				Message answer = connection.receive();				
				if(answer.getType() == MessageType.USER_NAME) {
					String userName = answer.getData();
					if(userName != null && !userName.isEmpty()) {
						if(!connectionMap.containsKey(userName)) {
							connectionMap.put(userName, connection);
							connection.send(new Message(MessageType.NAME_ACCEPTED));
							return userName;
						}
						else {
							ConsoleHelper.println("A user with this name is already registered. Please enter a different name.");
						}
					}
					else {
						ConsoleHelper.println("The user name you entered is empty. Please enter a different name.");						
					}
				}
			}
		}
		private void sendMessage(Message message) {
			for(Map.Entry<String, Connection> connection : connectionMap.entrySet()) {
				try {
					connection.getValue().send(message);
				} 
				catch (IOException e) {
					ConsoleHelper.println("An error occurred: " + e.getMessage());
				}
			}
		}
		private void serverMainLoop(Connection connection, String userName) throws ClassNotFoundException, IOException {
			while(true) {
				Message message = connection.receive();
				if (message.getType() == MessageType.TEXT) {
					String newMessage = userName + ": " + message.getData();
					sendMessage(new Message(MessageType.TEXT, newMessage));
				} 
				else {
					ConsoleHelper.println("An error occurred. Invalid message type...");
				}
			}
		}
		@Override
		public void run() {
			String userName = null;
			try(Connection connection = new Connection(socket)) {
				ConsoleHelper.println("A connection has been established with a remote address: " + connection.getConnectionAddress());
				userName = serverHandshake(connection);
				sendMessage(new Message(MessageType.USER_ADDED, userName));
				serverMainLoop(connection, userName);
			}
			catch(IOException | ClassNotFoundException e) {
				ConsoleHelper.println("An error occurred when exchanging messages with a remote address: " + socket.getRemoteSocketAddress());
			}
			if(userName != null) {
				connectionMap.remove(userName);
				sendMessage(new Message(MessageType.USER_REMOVED, userName));
			}
			ConsoleHelper.println("Connection to the remote address " + socket.getRemoteSocketAddress() + " was closed.");
		}	
	}


	public static void main(String[] args) {    	
		ConsoleHelper.println("Server startup...");
		ConsoleHelper.print("Please enter a port for the server: ");
		int port = ConsoleHelper.readInt();
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			ConsoleHelper.println("The server is running...");
			while(true) {
				Socket socket = serverSocket.accept();
				ConnectionHandler handler = new ConnectionHandler(socket);
				handler.start();        		
			}        	
		} 
		catch (IOException e) {			
			ConsoleHelper.println("An error occurred: " + e.getMessage());
		}
	}
}
