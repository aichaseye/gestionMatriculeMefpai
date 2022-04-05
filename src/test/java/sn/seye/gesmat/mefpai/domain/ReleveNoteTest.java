package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class ReleveNoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReleveNote.class);
        ReleveNote releveNote1 = new ReleveNote();
        releveNote1.setId(1L);
        ReleveNote releveNote2 = new ReleveNote();
        releveNote2.setId(releveNote1.getId());
        assertThat(releveNote1).isEqualTo(releveNote2);
        releveNote2.setId(2L);
        assertThat(releveNote1).isNotEqualTo(releveNote2);
        releveNote1.setId(null);
        assertThat(releveNote1).isNotEqualTo(releveNote2);
    }
}
