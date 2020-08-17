package nl.nielsdaalhuisen.lingogame.infrastructure.web.controller;

import nl.nielsdaalhuisen.lingogame.domain.model.Game;
import nl.nielsdaalhuisen.lingogame.domain.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("game")
public class GameController {
    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/new")
    public @ResponseBody String startGame() {
        Game g = new Game();
        g.setScore(10);
        gameRepository.save(g);
        return "saved";
    }

    @GetMapping
    public @ResponseBody List<UUID> getGames() {
        List<UUID> ids = new ArrayList<>();
        for(Game g : gameRepository.findAll()) {
            ids.add(g.getId());
        }
        return ids;
    }
}
