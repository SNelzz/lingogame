package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GameService gameService(GameRepository gameRepository) {
        return new GameService(gameRepository);
    }
}
