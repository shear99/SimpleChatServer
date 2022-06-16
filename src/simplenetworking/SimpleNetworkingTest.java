package simplenetworking;


public class SimpleNetworkingTest {
	public static void main(String[] args) {

		new Thread(() -> new SimpleChatServer().go()).start();


		try {
			Thread.sleep(1000);
		} catch (Exception ex) {
	 	}

		new Thread(() -> new SimpleChatClient().go("TalkerA")).start();
		new Thread( () -> new SimpleChatClient().go("TalkerB")).start();
		new Thread( () -> new SimpleChatClient().go("TalkerC")).start();
	}
	
}
