package simplenetworking;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.*;

public class SimpleChatGUI {
	private JTextField outgoing;
	private JTextArea incoming;
	private PrintWriter writer;
	private BufferedReader	reader;

	public class IncomingReader implements Runnable{
		public void run(){
			System.out.println("ready to receive");
			String message;
			try {
				while ((message = reader.readLine()) != null) {
					incoming.append(message + "\n");
				}
			} catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}


	private String	name;
	public SimpleChatGUI(Socket s, String n) {
		name = n;
		setUpNetworking(s);	//bufferedReader, writer 생성
		go(name);

		Thread t = new Thread(new IncomingReader());
		t.start();
	}

	public void go(String name) {
		JFrame frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel receivePanel = new JPanel();
		receivePanel.setLayout(new BoxLayout(receivePanel, BoxLayout.Y_AXIS));
		incoming = new JTextArea(10, 20);
		incoming.setEditable(false);
		JScrollPane	scpane = new JScrollPane(incoming);
		receivePanel.add(new JLabel("incoming message"));
		receivePanel.add(scpane);
		
		JPanel sendPanel = new JPanel();
		sendPanel.setLayout(new BoxLayout(sendPanel, BoxLayout.Y_AXIS));
		outgoing = new JTextField(20);	
		
		JButton sendButton = new JButton("Send");
		sendPanel.add(new JLabel("outgoing message"));
		sendPanel.add(outgoing);
		sendPanel.add(sendButton);

		SendButtonListener sendListener = new SendButtonListener();
		outgoing.addActionListener(sendListener);
		sendButton.addActionListener(sendListener);

		frame.getContentPane().add(BorderLayout.NORTH, receivePanel);
		frame.getContentPane().add(BorderLayout.SOUTH, sendPanel);

		frame.setSize(400,300);
		frame.setVisible(true);
		outgoing.requestFocus();
	}


	private boolean setUpNetworking(Socket s) {
		try {
			Socket sock = s;
			reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
			writer = new PrintWriter(s.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return  false;
		}
		return true;
	}
	
	public class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				writer.println(name + " : " + outgoing.getText());
				writer.flush();
			} catch (Exception ex) {

			}
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
	
}
