package chen.eric.nim.ai;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chen.eric.nim.ai.inject.AiModule;
import chen.eric.nim.ai.inject.DaggerAiComponent;

public class Ai {
	private static final Logger log = LoggerFactory.getLogger(Ai.class);

	@Inject
	AiPlayer aiPlayer;

	@Inject
	Ai() {}

	void run(String playerName) throws TException, IOException {
		aiPlayer.run(playerName);
	}

	public static void main(String... args) throws TException, IOException {
		String playerName = "AI";
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
		final AiModule aiModule = new AiModule(serverUrl, serverPort);
		DaggerAiComponent.builder()
			.aiModule(aiModule)
			.build()
			.getAi()
			.run(playerName);
	}
}
