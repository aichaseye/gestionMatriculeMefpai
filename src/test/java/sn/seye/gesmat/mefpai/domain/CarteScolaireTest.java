package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class CarteScolaireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CarteScolaire.class);
        CarteScolaire carteScolaire1 = new CarteScolaire();
        carteScolaire1.setId(1L);
        CarteScolaire carteScolaire2 = new CarteScolaire();
        carteScolaire2.setId(carteScolaire1.getId());
        assertThat(carteScolaire1).isEqualTo(carteScolaire2);
        carteScolaire2.setId(2L);
        assertThat(carteScolaire1).isNotEqualTo(carteScolaire2);
        carteScolaire1.setId(null);
        assertThat(carteScolaire1).isNotEqualTo(carteScolaire2);
    }
}
