package jimmyhokkins.chat.theChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientView {
	
	private final ClientController controller;

    private JFrame frame = new JFrame("The Chat");
    private JTextField textField = new JTextField(50);
    private JTextArea messages = new JTextArea(10, 50);
    private JTextArea users = new JTextArea(10, 10);

    public ClientView(ClientController controller) {
        this.controller = controller;
        initView();
    }
    
    private void initView() {
        textField.setEditable(false);
        messages.setEditable(false);
        users.setEditable(false);

        frame.getContentPane().add(textField, BorderLayout.NORTH);
        frame.getContentPane().add(new JScrollPane(messages), BorderLayout.WEST);
        frame.getContentPane().add(new JScrollPane(users), BorderLayout.EAST);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        textField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                controller.sendTextMessage(textField.getText());
                textField.setText("");
            }
        });
    }
    
    public String getServerAddress() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter the server address:",
                "Client configuration",
                JOptionPane.QUESTION_MESSAGE);
    }
    
    public int getServerPort() {
        while (true) {
            String port = JOptionPane.showInputDialog(
                    frame,
                    "Enter the server port:",
                    "Client configuration",
                    JOptionPane.QUESTION_MESSAGE);
            try {
                return Integer.parseInt(port.trim());
            } 
            catch (Exception e) {
                JOptionPane.showMessageDialog(
                        frame,
                        "An invalid server port was entered. Try again.",
                        "Client configuration",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public String getUserName() {
        return JOptionPane.showInputDialog(
                frame,
                "Enter your name:",
                "Client configuration",
                JOptionPane.QUESTION_MESSAGE);
    }
    
    public void notifyConnectionStatusChanged(boolean clientConnected) {
        textField.setEditable(clientConnected);
        if (clientConnected) {
            JOptionPane.showMessageDialog(
                    frame,
                    "The connection to the server is established",
                    "The Chat",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "The client is not connected to the server",
                    "The Chat",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void refreshMessages() {
        messages.append(controller.getModel().getNewMessage() + "\n");
    }

    public void refreshUsers() {
        ClientModel model = controller.getModel();
        StringBuilder sb = new StringBuilder();
        for (String userName : model.getAllUserNames()) {
            sb.append(userName).append("\n");
        }
        users.setText(sb.toString());
    }
}
