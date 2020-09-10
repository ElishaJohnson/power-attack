package com.elishajohnson.powerattack.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Character.
 */
@Entity
@Table(name = "jhi_character")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Character implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Max(value = 100)
    @Column(name = "base_attack", nullable = false)
    private Integer baseAttack;

    @OneToMany(mappedBy = "character")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Weapon> weapons = new HashSet<>();

    @OneToMany(mappedBy = "character")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AttackCycle> attackCycles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("characters")
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Character name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBaseAttack() {
        return baseAttack;
    }

    public Character baseAttack(Integer baseAttack) {
        this.baseAttack = baseAttack;
        return this;
    }

    public void setBaseAttack(Integer baseAttack) {
        this.baseAttack = baseAttack;
    }

    public Set<Weapon> getWeapons() {
        return weapons;
    }

    public Character weapons(Set<Weapon> weapons) {
        this.weapons = weapons;
        return this;
    }

    public Character addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
        weapon.setCharacter(this);
        return this;
    }

    public Character removeWeapon(Weapon weapon) {
        this.weapons.remove(weapon);
        weapon.setCharacter(null);
        return this;
    }

    public void setWeapons(Set<Weapon> weapons) {
        this.weapons = weapons;
    }

    public Set<AttackCycle> getAttackCycles() {
        return attackCycles;
    }

    public Character attackCycles(Set<AttackCycle> attackCycles) {
        this.attackCycles = attackCycles;
        return this;
    }

    public Character addAttackCycle(AttackCycle attackCycle) {
        this.attackCycles.add(attackCycle);
        attackCycle.setCharacter(this);
        return this;
    }

    public Character removeAttackCycle(AttackCycle attackCycle) {
        this.attackCycles.remove(attackCycle);
        attackCycle.setCharacter(null);
        return this;
    }

    public void setAttackCycles(Set<AttackCycle> attackCycles) {
        this.attackCycles = attackCycles;
    }

    public User getUser() {
        return user;
    }

    public Character user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Character)) {
            return false;
        }
        return id != null && id.equals(((Character) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Character{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", baseAttack=" + getBaseAttack() +
            "}";
    }
}
