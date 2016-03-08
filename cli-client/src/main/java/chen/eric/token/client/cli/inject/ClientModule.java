package chen.eric.token.client.cli.inject;

import javax.inject.Singleton;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import chen.eric.token.api.TokenGame;
import dagger.Module;
import dagger.Provides;

@Module
public class ClientModule {
	Integer serverPort = 9090;
	String serverUrl = "localhost";

	public ClientModule(String serverUrl, Integer serverPort) {
		if (serverUrl != null) {
			this.serverUrl = serverUrl;
		}
		if (serverPort != null) {
			this.serverPort = serverPort;
		}
	}

	@Provides
	@ServerPort
	Integer provideServerPort() {
		return serverPort;
	};

	@Provides
	@ServerUrl
	String provideServerUrl() {
		return serverUrl;
	}

	@Provides
	@Singleton
	TTransport provideThriftTransport(@ServerUrl String serverUrl, @ServerPort Integer serverPort) {
		return new TSocket(serverUrl, serverPort);
	}

	@Provides
	@Singleton
	TokenGame.Iface provideThriftClient(TTransport thriftTransport) {
		return new TokenGame.Client(new TBinaryProtocol(thriftTransport));
	}
}
