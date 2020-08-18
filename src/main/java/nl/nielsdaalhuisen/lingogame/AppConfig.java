package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.application.*;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.client.WordClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public GameService gameService() {
        return new GameService();
    }

    @Bean
    public RoundService roundService() {
        return new RoundService();
    }

    @Bean
    public WordService wordService() {
        return new WordService();
    }

    @Bean
    public WordClient wordClient() {
        return new WordClient();
    }

    @Bean
    public TurnService turnService() {
        return new TurnService();
    }

    @Bean
    public FeedbackService feedbackService() {
        return new FeedbackService();
    }
}
