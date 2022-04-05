package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class ClasseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classe.class);
        Classe classe1 = new Classe();
        classe1.setId(1L);
        Classe classe2 = new Classe();
        classe2.setId(classe1.getId());
        assertThat(classe1).isEqualTo(classe2);
        classe2.setId(2L);
        assertThat(classe1).isNotEqualTo(classe2);
        classe1.setId(null);
        assertThat(classe1).isNotEqualTo(classe2);
    }
}
