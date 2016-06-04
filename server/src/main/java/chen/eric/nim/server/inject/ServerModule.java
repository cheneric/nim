package chen.eric.nim.server.inject;

import javax.inject.Singleton;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import chen.eric.nim.api.Nim;
import chen.eric.nim.api.Nim.Processor;
import chen.eric.nim.server.api.ApiHandler;
import dagger.Module;
import dagger.Provides;

@Module
public class ServerModule {
	Integer serverPort = 9090;

	public ServerModule(Integer serverPort) {
		if (serverPort != null) {
			this.serverPort = serverPort;
		}
	}

	@Provides
	@Singleton
	Nim.Iface provideApiHandler(ApiHandler apiHandler) {
		return apiHandler;
	}

	@Provides
	@ServerPort
	Integer provideServerPort() {
		return serverPort;
	};

	@Provides
	@Singleton
	TServer provideThriftServer(@ServerPort Integer serverPort, Nim.Iface apiHandler) {
		try {
			return new TThreadPoolServer(
					new TThreadPoolServer.Args(
						new TServerSocket(serverPort))
						.processor(new Processor<Nim.Iface>(apiHandler)));
		}
		catch (TTransportException exception) {
			throw new RuntimeException(exception);
		}
	}
}
