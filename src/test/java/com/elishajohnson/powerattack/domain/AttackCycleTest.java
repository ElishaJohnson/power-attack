package com.elishajohnson.powerattack.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.elishajohnson.powerattack.web.rest.TestUtil;

public class AttackCycleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttackCycle.class);
        AttackCycle attackCycle1 = new AttackCycle();
        attackCycle1.setId(1L);
        AttackCycle attackCycle2 = new AttackCycle();
        attackCycle2.setId(attackCycle1.getId());
        assertThat(attackCycle1).isEqualTo(attackCycle2);
        attackCycle2.setId(2L);
        assertThat(attackCycle1).isNotEqualTo(attackCycle2);
        attackCycle1.setId(null);
        assertThat(attackCycle1).isNotEqualTo(attackCycle2);
    }
}
