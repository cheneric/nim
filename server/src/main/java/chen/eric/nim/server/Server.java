package chen.eric.nim.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import chen.eric.nim.server.inject.DaggerServerComponent;
import chen.eric.nim.server.inject.ServerModule;
import chen.eric.nim.server.inject.ServerPort;

@Singleton
public class Server {
	private static final Logger log = LoggerFactory.getLogger(Server.class);

	@Inject
	@ServerPort
	Integer serverPort;

	@Inject
	TServer thriftServer;

	@Inject
	Server() {}

	void run() throws TTransportException {
			System.out.println("Listening on port " + serverPort);
			thriftServer.serve();
	}

	public static void main(String... args) throws TTransportException {
		Integer serverPort = null;
		if (args.length >= 1) {
			final String serverPortArg = args[0];
			try {
				serverPort = Integer.parseInt(serverPortArg);
			}
			catch (NumberFormatException exception) {
				log.debug("Invalid server port: " + serverPortArg);
			}
		}
		DaggerServerComponent.builder()
			.serverModule(new ServerModule(serverPort))
			.build()
			.getServer()
			.run();
	}
}
