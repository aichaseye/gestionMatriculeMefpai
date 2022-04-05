package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class DiplomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Diplome.class);
        Diplome diplome1 = new Diplome();
        diplome1.setId(1L);
        Diplome diplome2 = new Diplome();
        diplome2.setId(diplome1.getId());
        assertThat(diplome1).isEqualTo(diplome2);
        diplome2.setId(2L);
        assertThat(diplome1).isNotEqualTo(diplome2);
        diplome1.setId(null);
        assertThat(diplome1).isNotEqualTo(diplome2);
    }
}
