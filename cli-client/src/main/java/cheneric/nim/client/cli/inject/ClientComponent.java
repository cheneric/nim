package cheneric.nim.client.cli.inject;

import javax.inject.Singleton;

import cheneric.nim.client.cli.Client;
import dagger.Component;

@Component(modules = {ClientModule.class})
@Singleton
public interface ClientComponent {
	Client getClient();
}
