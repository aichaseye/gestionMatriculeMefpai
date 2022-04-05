package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class InscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscription.class);
        Inscription inscription1 = new Inscription();
        inscription1.setId(1L);
        Inscription inscription2 = new Inscription();
        inscription2.setId(inscription1.getId());
        assertThat(inscription1).isEqualTo(inscription2);
        inscription2.setId(2L);
        assertThat(inscription1).isNotEqualTo(inscription2);
        inscription1.setId(null);
        assertThat(inscription1).isNotEqualTo(inscription2);
    }
}
