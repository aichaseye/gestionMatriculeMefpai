package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class ApprenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Apprenant.class);
        Apprenant apprenant1 = new Apprenant();
        apprenant1.setId(1L);
        Apprenant apprenant2 = new Apprenant();
        apprenant2.setId(apprenant1.getId());
        assertThat(apprenant1).isEqualTo(apprenant2);
        apprenant2.setId(2L);
        assertThat(apprenant1).isNotEqualTo(apprenant2);
        apprenant1.setId(null);
        assertThat(apprenant1).isNotEqualTo(apprenant2);
    }
}
