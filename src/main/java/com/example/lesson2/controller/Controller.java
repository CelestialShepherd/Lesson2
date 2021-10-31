package com.example.lesson2.controller;

import com.example.lesson2.dto.ResponseDTO;
import com.example.lesson2.entity.PlayerEntity;
import com.example.lesson2.entity.PlayerStatus;
import com.example.lesson2.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class Controller {

    private final PlayerService service;

    @GetMapping("/player/{id}") // .../player/...
    public PlayerEntity loadPlayer(@PathVariable("id") Long id) {
        return service.getPlayerById(id);
    }

    @GetMapping("/player") // .../player?status=...
    public ResponseEntity<ResponseDTO> loadPlayersByStatus(@RequestParam("status") PlayerStatus status) {
        return service.findPlayersByStatus(status);
    }

    @GetMapping("/player/stat") // .../player/stat?nickname=...
    public String getPercentOfPlayersWithSimilarNickNames(@RequestParam("nickname") String nickName) {
        return new String ("There are " + new DecimalFormat("#0.00").format(service.getPercentOfPlayersWithSimilarNickNames(nickName)) + "% of similar to \"" + nickName + "\" nickname in database") ;
    }

    @GetMapping("player/term")
    public List<PlayerEntity> getTerminatedPlayersByDate(@RequestParam("before") Date beforeDate) throws ParseException, InterruptedException {
        return service.getTerminatedPlayersByDate(beforeDate);
    }

    @PostMapping("/player/create")
    public String createPlayer(@RequestBody PlayerEntity playerEntity) {
        service.createPlayer(playerEntity);
        System.out.println(playerEntity);
        return "Player was successfully created";
    }

    @PutMapping("/player/update")
    public String update(@RequestBody PlayerEntity playerEntity) {
        service.updatePlayer(playerEntity);
        System.out.println(playerEntity);
        return "Player was successfully updated";
    }
}
