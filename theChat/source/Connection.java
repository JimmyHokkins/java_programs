package jimmyhokkins.chat.theChat;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements Closeable {

	private final Socket socket;
	private final ObjectOutputStream out;
	private final ObjectInputStream in;

	public Connection(Socket socket) throws IOException {		
		this.socket = socket;
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
	}

	public void send(Message message) throws IOException {
		out.writeObject(message);
	}

	public Message receive() throws ClassNotFoundException, IOException {
		return (Message) in.readObject();
	}

	public SocketAddress getConnectionAddress() {
		return socket.getRemoteSocketAddress();
	}

	@Override
	public void close() throws IOException {
		out.close();
		in.close();
		socket.close();		
	}
}
