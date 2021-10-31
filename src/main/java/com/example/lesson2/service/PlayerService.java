package com.example.lesson2.service;

import com.example.lesson2.data.PlayerRepository;
import com.example.lesson2.dto.PlayerDTO;
import com.example.lesson2.dto.ResponseDTO;
import com.example.lesson2.entity.PlayerEntity;
import com.example.lesson2.entity.PlayerStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j //позволяет прописывать логи в консоль
@Service //класс стал бином для бизнес-логики
@RequiredArgsConstructor //конструктор для финальных полей
public class PlayerService {

    //todo Почему финальное поле, имеющее тип данных интерфейс может реализовывать методы?
    private final PlayerRepository playerRepository;

    public PlayerEntity getPlayerById(Long id) {
        log.info("getPlayerById.in - searching player {}", id);
        Optional<PlayerEntity> byId = playerRepository.findById(id);
        if (byId.isPresent()) {
            log.info("getPlayerById.out - found player {}", id);
            return byId.get();
        } else {
            log.error("getPlayerById.out - not found player {}", id);
            throw new EntityNotFoundException("Entity with given id not found!");
        }
    }

    public ResponseEntity<ResponseDTO> findPlayersByStatus(PlayerStatus status) {
        try{
            List<PlayerDTO> collect = playerRepository.findByTerminated(status.isVal()).stream()
                    .map(this::map)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ResponseDTO.builder().result(collect).build());
        } catch(Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public void createPlayer(PlayerEntity playerEntity){
        playerRepository.save(playerEntity);
    }

    public void updatePlayer(PlayerEntity playerEntity){
        playerRepository.save(playerEntity);
    }

    public double getPercentOfPlayersWithSimilarNickNames(String nickName){
        StringBuilder str = new StringBuilder(".*");

        for (int i = 0; i < nickName.length(); i++){
            str.append(nickName.charAt(i)).append(".*");
        }
        //todo Есть ли смысл в создании переменной pattern?
        String pattern = str.toString();
        List<PlayerEntity> playerEntities = playerRepository.findAll();
        List<PlayerEntity> playerFiltered = playerEntities.stream()
                .filter(playerEntity -> Pattern.matches(pattern, playerEntity.getNickName()))
                .collect(Collectors.toList());

        return (double) playerFiltered.size() / playerEntities.size();
    }

    public List<PlayerEntity> getTerminatedPlayersByDate(Date beforeDate) throws InterruptedException, ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date afterDate = dateFormat.parse(new Date().toString());
        beforeDate = dateFormat.parse(beforeDate.toString());
        List<PlayerEntity> playerEntitiesNotTerminated = playerRepository.findByTerminated(false)
                .stream()
                .collect(Collectors.toList());
        long diff = beforeDate.getTime() - afterDate.getTime();
        try {
            Thread.sleep(diff);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<PlayerEntity> playerEntitiesTerminatedByDate = playerEntitiesNotTerminated.stream()
                .filter(playerEntity -> playerEntity.isTerminated())
                .collect(Collectors.toList());
        return playerEntitiesTerminatedByDate;
    }

    public PlayerDTO map(PlayerEntity playerEntity){
        return PlayerDTO.builder()
                .nickname(playerEntity.getNickName())
                .id(playerEntity.getId())
                .description(playerEntity.getProfileInfo())
                .build();
    }
}
