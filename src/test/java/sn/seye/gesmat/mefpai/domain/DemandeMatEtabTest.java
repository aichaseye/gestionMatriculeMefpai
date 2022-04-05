package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class DemandeMatEtabTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DemandeMatEtab.class);
        DemandeMatEtab demandeMatEtab1 = new DemandeMatEtab();
        demandeMatEtab1.setId(1L);
        DemandeMatEtab demandeMatEtab2 = new DemandeMatEtab();
        demandeMatEtab2.setId(demandeMatEtab1.getId());
        assertThat(demandeMatEtab1).isEqualTo(demandeMatEtab2);
        demandeMatEtab2.setId(2L);
        assertThat(demandeMatEtab1).isNotEqualTo(demandeMatEtab2);
        demandeMatEtab1.setId(null);
        assertThat(demandeMatEtab1).isNotEqualTo(demandeMatEtab2);
    }
}
