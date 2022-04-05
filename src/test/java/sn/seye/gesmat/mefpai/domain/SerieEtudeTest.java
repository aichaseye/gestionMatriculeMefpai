package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class SerieEtudeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SerieEtude.class);
        SerieEtude serieEtude1 = new SerieEtude();
        serieEtude1.setId(1L);
        SerieEtude serieEtude2 = new SerieEtude();
        serieEtude2.setId(serieEtude1.getId());
        assertThat(serieEtude1).isEqualTo(serieEtude2);
        serieEtude2.setId(2L);
        assertThat(serieEtude1).isNotEqualTo(serieEtude2);
        serieEtude1.setId(null);
        assertThat(serieEtude1).isNotEqualTo(serieEtude2);
    }
}
