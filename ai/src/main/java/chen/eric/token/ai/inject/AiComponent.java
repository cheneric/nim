package chen.eric.token.ai.inject;

import javax.inject.Singleton;

import chen.eric.token.ai.Ai;
import dagger.Component;

@Component(modules = {AiModule.class})
@Singleton
public interface AiComponent {
	Ai getAi();
}
