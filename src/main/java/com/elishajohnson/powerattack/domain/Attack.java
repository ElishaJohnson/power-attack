package com.elishajohnson.powerattack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Attack.
 */
@Entity
@Table(name = "attack")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Attack implements Serializable {

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
    @Column(name = "attack_modifier", nullable = false)
    private Integer attackModifier;

    @NotNull
    @Max(value = 100)
    @Column(name = "crit_chance", nullable = false)
    private Integer critChance;

    @NotNull
    @Max(value = 100)
    @Column(name = "crit_damage", nullable = false)
    private Integer critDamage;

    @NotNull
    @Max(value = 100)
    @Column(name = "die_value", nullable = false)
    private Integer dieValue;

    @NotNull
    @Max(value = 100)
    @Column(name = "dice_count", nullable = false)
    private Integer diceCount;

    @NotNull
    @Max(value = 100)
    @Column(name = "damage_bonus", nullable = false)
    private Integer damageBonus;

    @ManyToOne
    @JsonIgnoreProperties("attacks")
    private Weapon weapon;

    @ManyToMany(mappedBy = "attacks")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<AttackCycle> attackCycles = new HashSet<>();

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

    public Attack name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAttackModifier() {
        return attackModifier;
    }

    public Attack attackModifier(Integer attackModifier) {
        this.attackModifier = attackModifier;
        return this;
    }

    public void setAttackModifier(Integer attackModifier) {
        this.attackModifier = attackModifier;
    }

    public Integer getCritChance() {
        return critChance;
    }

    public Attack critChance(Integer critChance) {
        this.critChance = critChance;
        return this;
    }

    public void setCritChance(Integer critChance) {
        this.critChance = critChance;
    }

    public Integer getCritDamage() {
        return critDamage;
    }

    public Attack critDamage(Integer critDamage) {
        this.critDamage = critDamage;
        return this;
    }

    public void setCritDamage(Integer critDamage) {
        this.critDamage = critDamage;
    }

    public Integer getDieValue() {
        return dieValue;
    }

    public Attack dieValue(Integer dieValue) {
        this.dieValue = dieValue;
        return this;
    }

    public void setDieValue(Integer dieValue) {
        this.dieValue = dieValue;
    }

    public Integer getDiceCount() {
        return diceCount;
    }

    public Attack diceCount(Integer diceCount) {
        this.diceCount = diceCount;
        return this;
    }

    public void setDiceCount(Integer diceCount) {
        this.diceCount = diceCount;
    }

    public Integer getDamageBonus() {
        return damageBonus;
    }

    public Attack damageBonus(Integer damageBonus) {
        this.damageBonus = damageBonus;
        return this;
    }

    public void setDamageBonus(Integer damageBonus) {
        this.damageBonus = damageBonus;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public Attack weapon(Weapon weapon) {
        this.weapon = weapon;
        return this;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Set<AttackCycle> getAttackCycles() {
        return attackCycles;
    }

    public Attack attackCycles(Set<AttackCycle> attackCycles) {
        this.attackCycles = attackCycles;
        return this;
    }

    public Attack addAttackCycle(AttackCycle attackCycle) {
        this.attackCycles.add(attackCycle);
        attackCycle.getAttacks().add(this);
        return this;
    }

    public Attack removeAttackCycle(AttackCycle attackCycle) {
        this.attackCycles.remove(attackCycle);
        attackCycle.getAttacks().remove(this);
        return this;
    }

    public void setAttackCycles(Set<AttackCycle> attackCycles) {
        this.attackCycles = attackCycles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attack)) {
            return false;
        }
        return id != null && id.equals(((Attack) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Attack{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", attackModifier=" + getAttackModifier() +
            ", critChance=" + getCritChance() +
            ", critDamage=" + getCritDamage() +
            ", dieValue=" + getDieValue() +
            ", diceCount=" + getDiceCount() +
            ", damageBonus=" + getDamageBonus() +
            "}";
    }
}
