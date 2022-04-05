package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class BonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bon.class);
        Bon bon1 = new Bon();
        bon1.setId(1L);
        Bon bon2 = new Bon();
        bon2.setId(bon1.getId());
        assertThat(bon1).isEqualTo(bon2);
        bon2.setId(2L);
        assertThat(bon1).isNotEqualTo(bon2);
        bon1.setId(null);
        assertThat(bon1).isNotEqualTo(bon2);
    }
}
