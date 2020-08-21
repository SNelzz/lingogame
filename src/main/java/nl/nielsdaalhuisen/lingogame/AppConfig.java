package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.application.*;
import nl.nielsdaalhuisen.lingogame.infrastructure.data.SourceDeserializer;
import nl.nielsdaalhuisen.lingogame.infrastructure.web.client.WordClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
    public SourceDeserializer SourceDeserializer() {return new WordClient(); }

    @Bean
    public TurnService turnService() {
        return new TurnService();
    }

    @Bean
    public FeedbackService feedbackService() {
        return new FeedbackService();
    }

    @Bean
    public HighscoreService highscoreService() {
        return new HighscoreService();
    }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
