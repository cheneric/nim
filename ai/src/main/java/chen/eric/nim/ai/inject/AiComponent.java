package chen.eric.nim.ai.inject;

import javax.inject.Singleton;

import chen.eric.nim.ai.Ai;
import dagger.Component;

@Component(modules = {AiModule.class})
@Singleton
public interface AiComponent {
	Ai getAi();
}
