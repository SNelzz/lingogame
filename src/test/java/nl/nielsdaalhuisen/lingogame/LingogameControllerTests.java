package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.application.*;
import nl.nielsdaalhuisen.lingogame.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class LingogameControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GameService gameService;

    @MockBean
    HighscoreService highscoreService;

    @MockBean
    RoundService roundService;

    @MockBean
    TurnService turnService;

    @MockBean
    WordService wordService;

    @Test
    @DisplayName("Create and return a new game object - happy")
    void createNewGame() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameService.startGame()).thenReturn(game);

        mockMvc.perform(get("/game/new").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(game.getId().toString()))
                .andExpect(jsonPath("$.status").value(GameStatus.Started.toString()));
    }

    @Test
    @DisplayName("Get a game by id - happy")
    void GetGameById() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.Started);
        game.setId(UUID.randomUUID());

        Mockito.when(gameService.getGameById(game.getId())).thenReturn(game);

        mockMvc.perform(get("/game/" + game.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(game.getId().toString()))
                .andExpect(jsonPath("$.status").value(GameStatus.Started.toString()));
    }

    @Test
    @DisplayName("End a game by id - happy")
    void EndGameById() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.Ended);
        game.setId(UUID.randomUUID());

        Mockito.when(gameService.endGame(game.getId())).thenReturn(game);

        mockMvc.perform(patch("/game/" + game.getId() + "/end").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(game.getId().toString()))
                .andExpect(jsonPath("$.status").value(GameStatus.Ended.toString()));
    }

    @Test
    @DisplayName("save highscore of game - happy")
    void SaveHighscore() throws Exception {
        Game game = new Game();
        game.setStatus(GameStatus.Ended);
        game.setId(UUID.randomUUID());
        game.setScore(100);

        String playerName = "testPlayer";
        Highscore highscore = new Highscore(playerName, 100);

        Mockito.when(gameService.saveHighscore(game.getId(), playerName)).thenReturn(highscore);

        mockMvc.perform(post("/game/" + game.getId() + "/highscore").accept(MediaType.APPLICATION_JSON)
                .content(playerName))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.playerName").value(playerName))
                .andExpect(jsonPath("$.score").value(100));
    }

    @Test
    @DisplayName("get highscore - happy")
    void GetHighscore() throws Exception {
        String playerName = "testPlayer";
        Highscore highscore = new Highscore(playerName, 100);
        List<Highscore> highscores = new ArrayList<>();
        highscores.add(highscore);

        Mockito.when(highscoreService.getHighscores()).thenReturn(highscores);

        mockMvc.perform(get("/highscore").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].playerName").value(playerName))
                .andExpect(jsonPath("$[0].score").value(100));
    }

    @Test
    @DisplayName("start a new round - happy")
    void StartNewRound() throws Exception {
        Word word = new Word("tester");
        Round round = new Round();
        round.setWinningWord(word);
        round.setWinningWordLength(6);

        Mockito.when(roundService.startNewRound(any(UUID.class))).thenReturn(round);

        mockMvc.perform(get("/game/" + UUID.randomUUID() +"/round/new").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.winningWord").doesNotExist())
                .andExpect(jsonPath("$.winningWordLength").value(6));
    }

    @Test
    @DisplayName("process a new guess (turnController) - happy")
    void ProcessNewGuess() throws Exception {
        String guess = "tester";
        Turn turn = new Turn();
        turn.setId((long) 1);
        turn.setIdx(1);
        turn.setGuess(guess);
        List<Turn> turns = new ArrayList<>();
        turns.add(turn);

        Round round = new Round();
        round.setId((long) 2);
        round.setTurns(turns);

        Game game = new Game();
        game.setId(UUID.randomUUID());

        Mockito.when(turnService.processGuess(game.getId(), round.getId(), turn.getId(), guess)).thenReturn(round);

        mockMvc.perform(post("/game/" + game.getId() + "/round/" + round.getId() + "/turn/" + turn.getId() + "/guess").accept(MediaType.APPLICATION_JSON)
                .content(guess))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.turns[0].id").value(turn.getId()))
                .andExpect(jsonPath("$.turns[0].guess").value(guess))
                .andExpect(jsonPath("$.turns[0].idx").value(turn.getIdx()));
    }

    @Test
    @DisplayName("import words from source - happy")
    void ImportWords() throws Exception {
        Word word = new Word("tester");
        word.setId((long) 1);
        List<Word> words = new ArrayList<>();
        words.add(word);

        Mockito.when(wordService.importWords()).thenReturn(words);

        mockMvc.perform(get("/word/import").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(word.getId().toString()))
                .andExpect(jsonPath("$[0].value").value(word.getValue()));
    }
}
