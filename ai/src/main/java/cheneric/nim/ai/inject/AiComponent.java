package cheneric.nim.ai.inject;

import javax.inject.Singleton;

import cheneric.nim.ai.Ai;
import dagger.Component;

@Component(modules = {AiModule.class})
@Singleton
public interface AiComponent {
	Ai getAi();
}
