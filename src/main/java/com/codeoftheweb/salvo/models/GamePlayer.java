package com.codeoftheweb.salvo.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id // Identificador que diferencia una entidad del resto.
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game game;

    private LocalDateTime joinDate;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes;


    // -- Constructores -- //
    public GamePlayer() {
        this.joinDate = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
    }

    public GamePlayer(Player player, Game game) {
        this();
        this.player = player;
        this.game = game;
    }


    // -- Getters -- //
    public long getId() {
        return id;
    }

    @JsonIgnore
    public Player getPlayer() {
        return player;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }


    // -- Setters -- //
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }


    // -- Metodos -- //
    public void addShips(Set<Ship> ships) {
        ships.forEach(ship -> {
            ship.setGamePlayer(this);
            this.ships.add(ship);
        });
    }

    public void addSalvo(Salvo salvo) {

        salvo.setGamePlayer(this);
        this.salvoes.add(salvo);
    }

    public boolean hasSalvo(Salvo salvo) {

        for (Salvo salvo1 : salvoes) {

            if (salvo1.getTurn() == salvo.getTurn()) {

                return true;
            }
        }
        return false;
    }


    public Map<String, Object> makeGamePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        dto.put("id", getId());
        dto.put("player", player.makePlayerDTO());

        return dto;
    }

    public Optional<Score> getScore() {
        return player.getScore(this.game);
    }
}