package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class ImageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Image.class);
        Image image1 = new Image();
        image1.setId(1L);
        Image image2 = new Image();
        image2.setId(image1.getId());
        assertThat(image1).isEqualTo(image2);
        image2.setId(2L);
        assertThat(image1).isNotEqualTo(image2);
        image1.setId(null);
        assertThat(image1).isNotEqualTo(image2);
    }
}
