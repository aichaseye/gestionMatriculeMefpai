package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class DemandeMatAppTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeMatApp.class);
        DemandeMatApp demandeMatApp1 = new DemandeMatApp();
        demandeMatApp1.setId(1L);
        DemandeMatApp demandeMatApp2 = new DemandeMatApp();
        demandeMatApp2.setId(demandeMatApp1.getId());
        assertThat(demandeMatApp1).isEqualTo(demandeMatApp2);
        demandeMatApp2.setId(2L);
        assertThat(demandeMatApp1).isNotEqualTo(demandeMatApp2);
        demandeMatApp1.setId(null);
        assertThat(demandeMatApp1).isNotEqualTo(demandeMatApp2);
    }
}
