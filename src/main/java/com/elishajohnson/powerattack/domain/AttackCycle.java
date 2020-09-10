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
 * A AttackCycle.
 */
@Entity
@Table(name = "attack_cycle")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AttackCycle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "attack_cycle_attack",
               joinColumns = @JoinColumn(name = "attack_cycle_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "attack_id", referencedColumnName = "id"))
    private Set<Attack> attacks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("attackCycles")
    private Character character;

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

    public AttackCycle name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Attack> getAttacks() {
        return attacks;
    }

    public AttackCycle attacks(Set<Attack> attacks) {
        this.attacks = attacks;
        return this;
    }

    public AttackCycle addAttack(Attack attack) {
        this.attacks.add(attack);
        attack.getAttackCycles().add(this);
        return this;
    }

    public AttackCycle removeAttack(Attack attack) {
        this.attacks.remove(attack);
        attack.getAttackCycles().remove(this);
        return this;
    }

    public void setAttacks(Set<Attack> attacks) {
        this.attacks = attacks;
    }

    public Character getCharacter() {
        return character;
    }

    public AttackCycle character(Character character) {
        this.character = character;
        return this;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttackCycle)) {
            return false;
        }
        return id != null && id.equals(((AttackCycle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AttackCycle{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
