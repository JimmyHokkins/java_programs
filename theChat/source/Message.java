package jimmyhokkins.chat.theChat;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	private final MessageType type;
	private final String data;

	public Message(MessageType type) {
		this.type = type;
		this.data = null;
	}

	public Message(MessageType type, String data) {
		this.type = type;
		this.data = data;
	}

	public MessageType getType() {
		return type;
	}

	public String getData() {
		return data;
	}
}
