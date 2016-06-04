package chen.eric.nim.client.cli.inject;

import javax.inject.Singleton;

import chen.eric.nim.client.cli.Client;
import dagger.Component;

@Component(modules = {ClientModule.class})
@Singleton
public interface ClientComponent {
	Client getClient();
}
