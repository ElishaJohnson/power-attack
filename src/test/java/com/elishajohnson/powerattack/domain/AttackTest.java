package com.elishajohnson.powerattack.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.elishajohnson.powerattack.web.rest.TestUtil;

public class AttackTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attack.class);
        Attack attack1 = new Attack();
        attack1.setId(1L);
        Attack attack2 = new Attack();
        attack2.setId(attack1.getId());
        assertThat(attack1).isEqualTo(attack2);
        attack2.setId(2L);
        assertThat(attack1).isNotEqualTo(attack2);
        attack1.setId(null);
        assertThat(attack1).isNotEqualTo(attack2);
    }
}
