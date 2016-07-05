package cheneric.nim.server.inject;

import javax.inject.Singleton;

import cheneric.nim.server.Server;
import dagger.Component;

@Component(modules = {ServerModule.class})
@Singleton
public interface ServerComponent {
	Server getServer();
}
