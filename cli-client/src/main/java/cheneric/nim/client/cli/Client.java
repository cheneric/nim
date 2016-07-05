package cheneric.nim.client.cli;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cheneric.nim.client.cli.inject.ClientModule;
import cheneric.nim.client.cli.inject.DaggerClientComponent;

@Singleton
public class Client {
	private static final Logger log = LoggerFactory.getLogger(Client.class);

	@Inject
	Game game;

	@Inject
	Client() {}

	void run(String playerName) throws TException, IOException {
		game.run(playerName);
	}

	public static void main(String... args) throws TException, IOException {
		String playerName = "Anonymous";
		String serverUrl = null;
		Integer serverPort = null;
		if (args.length >= 1) {
			playerName = args[0];
		}
		if (args.length >= 2) {
			serverUrl = args[1];
		}
		if (args.length >= 3) {
			final String serverPortArg = args[2];
			try {
				serverPort = Integer.parseInt(serverPortArg);
			}
			catch (NumberFormatException exception) {
				log.debug("Invalid server port: " + serverPortArg);
			}
		}
		final ClientModule clientModule = new ClientModule(serverUrl, serverPort);
		DaggerClientComponent.builder()
			.clientModule(clientModule)
			.build()
			.getClient()
			.run(playerName);
	}
}
