package jimmyhokkins.chat.theChat;

import java.io.IOException;
import java.net.Socket;

public class ClientController {
	
	protected Connection connection;

	private ClientModel model = new ClientModel();
    private ClientView view = new ClientView(this);

    public class SocketThread {
        
        protected void processIncomingMessage(String message) {
            model.setNewMessage(message);
            view.refreshMessages();
        }
        
        protected void informAboutAddingNewUser(String userName) {
            model.addUser(userName);
            view.refreshUsers();
        }
        
        protected void informAboutDeletingNewUser(String userName) {
            model.deleteUser(userName);
            view.refreshUsers();
        }
        
        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            view.notifyConnectionStatusChanged(clientConnected);
        }        
        
        protected void clientHandshake() throws IOException, ClassNotFoundException {
            while(true) {
                Message message = connection.receive();
                if(message.getType() == null) throw new IOException("Unexpected MessageType");
                switch(message.getType()) {
                    case NAME_REQUEST:
                        String userName = getUserName();
                        connection.send(new Message(MessageType.USER_NAME, userName));
                        break;
                    case NAME_ACCEPTED:
                        notifyConnectionStatusChanged(true);
                        return;
                    default:
                        throw new IOException("Unexpected MessageType");
                }
            }
        }
        
        protected void clientMainLoop() throws IOException, ClassNotFoundException {
            while(true) {
                Message message = connection.receive();
                if(message.getType() == null) throw new IOException("Unexpected MessageType");
                switch(message.getType()) {
                    case TEXT:
                        processIncomingMessage(message.getData());
                        break;
                    case USER_ADDED:
                        informAboutAddingNewUser(message.getData());
                        break;
                    case USER_REMOVED:
                        informAboutDeletingNewUser(message.getData());
                        break;
                    default:
                        throw new IOException("Unexpected MessageType");
                }
            }
        }
        
        public void run() {
            String serverAddress = getServerAddress();
            int port = getServerPort();
            try {
                Socket socket = new Socket(serverAddress, port);
                connection = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException | ClassNotFoundException e) {
                notifyConnectionStatusChanged(false);
            }
        }
    }
    
    protected void sendTextMessage(String text) {
        try {
            connection.send(new Message(MessageType.TEXT, text));
        } catch (IOException e) {
            ConsoleHelper.writeMessage("Error when sending a message: " + e.getMessage());
        }
    }   
    
    protected SocketThread getSocketThread() {
        return new SocketThread();
    }
    
    public void run() {
        getSocketThread().run();
    }
    
    protected String getServerAddress() {
        return view.getServerAddress();
    }
    
    protected int getServerPort() {
        return view.getServerPort();
    }
    
    protected String getUserName() {
        return view.getUserName();
    }
    public ClientModel getModel() {
        return model;
    }

    public static void main(String[] args) {
        new ClientController().run();
    }	 
}
