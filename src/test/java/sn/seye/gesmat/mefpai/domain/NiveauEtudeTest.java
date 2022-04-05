package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class NiveauEtudeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NiveauEtude.class);
        NiveauEtude niveauEtude1 = new NiveauEtude();
        niveauEtude1.setId(1L);
        NiveauEtude niveauEtude2 = new NiveauEtude();
        niveauEtude2.setId(niveauEtude1.getId());
        assertThat(niveauEtude1).isEqualTo(niveauEtude2);
        niveauEtude2.setId(2L);
        assertThat(niveauEtude1).isNotEqualTo(niveauEtude2);
        niveauEtude1.setId(null);
        assertThat(niveauEtude1).isNotEqualTo(niveauEtude2);
    }
}
