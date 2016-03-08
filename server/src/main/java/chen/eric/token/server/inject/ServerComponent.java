package chen.eric.token.server.inject;

import javax.inject.Singleton;

import chen.eric.token.server.Server;
import dagger.Component;

@Component(modules = {ServerModule.class})
@Singleton
public interface ServerComponent {
	Server getServer();
}
