package jimmyhokkins.chat.theChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleHelper {

	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  
	public static void writeMessage(String message) {
		System.out.println(message);
	}

	public static void print(String text) {
		System.out.print(text);
	}

	public static void println(String text) {
		System.out.println(text);
	}

	public static String readString() {
		String input = null;
		while(input == null) {
			try {
				input = reader.readLine();
			} 
			catch (IOException e) {
				System.out.print("An error occurred while trying to enter text. Please try again: ");
			}
		}
		return input;
	}

	public static int readInt() {
		Integer input = null;
		while(input == null) {
			try {
				input = Integer.parseInt(readString());
			}
			catch(NumberFormatException e) {
				System.out.print("An error occurred while trying to enter the port. Please try again: ");
			}
		}
		return input;
	}
}
