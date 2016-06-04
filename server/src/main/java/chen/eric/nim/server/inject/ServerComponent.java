package chen.eric.nim.server.inject;

import javax.inject.Singleton;

import chen.eric.nim.server.Server;
import dagger.Component;

@Component(modules = {ServerModule.class})
@Singleton
public interface ServerComponent {
	Server getServer();
}
