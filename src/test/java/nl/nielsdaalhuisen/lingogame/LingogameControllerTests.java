package nl.nielsdaalhuisen.lingogame;

import nl.nielsdaalhuisen.lingogame.application.GameService;
import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.model.GameStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
public class LingogameControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GameService gameService;

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
                .andExpect(jsonPath("$.id").value(game.getId().toString()));
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
                .andExpect(jsonPath("$.id").value(game.getId().toString()));
    }
}
