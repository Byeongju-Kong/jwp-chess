package wooteco.chess.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import wooteco.chess.service.SpringChessService;

import java.util.Map;

@Controller
public class SpringChessController {
    private final SpringChessService springChessService;

    public SpringChessController(SpringChessService springChessService) {
        this.springChessService = springChessService;
    }

    @GetMapping("/")
    public String chessGame() {
        return "index";
    }

    @PostMapping("/ready/{gameId}")
    public String enterGameRoom(@PathVariable String gameId, Model model) {
        boolean canResume = springChessService.canResume(gameId);

        model.addAttribute("canResume", canResume);
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/play")
    public String startGame(@RequestParam(value = "game_id") String gameId, Model model) {
        springChessService.startNewGame(gameId);

        model.addAllAttributes(springChessService.provideGameInfo(gameId));
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/resume")
    public String resumeGame(@RequestParam(value = "game_id") String gameId, Model model) {
        springChessService.resumeGame(gameId);

        model.addAllAttributes(springChessService.provideGameInfo(gameId));
        model.addAttribute("game_id", gameId);
        return "game_room";
    }

    @PostMapping("/move")
    public String move(@RequestParam Map<String, String> params, Model model) {
        String gameId = params.get("game_id");
        springChessService.move(gameId, params.get("source"), params.get("target"));

        model.addAllAttributes(springChessService.provideGameInfo(gameId));
        model.addAttribute("game_id", gameId);
        model.addAttribute("end", springChessService.provideWinner(gameId));
        return "game_room";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String errorMessage(Exception e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "game_room";
    }
}