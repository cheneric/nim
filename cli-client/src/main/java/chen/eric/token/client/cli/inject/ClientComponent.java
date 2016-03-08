package chen.eric.token.client.cli.inject;

import javax.inject.Singleton;

import chen.eric.token.client.cli.Client;
import dagger.Component;

@Component(modules = {ClientModule.class})
@Singleton
public interface ClientComponent {
	Client getClient();
}
