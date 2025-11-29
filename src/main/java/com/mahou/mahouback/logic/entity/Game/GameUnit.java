package com.mahou.mahouback.logic.entity.Game;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_units")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column(nullable = false)
    private String unitId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UnitType type;

    @Column(nullable = false)
    private Integer owner;

    @Column(nullable = false)
    private Integer q;

    @Column(nullable = false)
    private Integer r;

    @Column(nullable = false)
    private Integer health;

    @Column(nullable = false)
    private Integer maxHealth;

    @Column(nullable = false)
    private Integer attack;

    @Column(nullable = false)
    private Integer defense;

    @Column(nullable = false)
    private Integer movement;

    @Column(nullable = false)
    private Integer remainingMovement;

    @Column(nullable = false)
    private Boolean hasAttacked = false;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private String icon;

    public enum UnitType {
        WARRIOR,
        ARCHER,
        CAVALRY,
        SETTLER
    }
}