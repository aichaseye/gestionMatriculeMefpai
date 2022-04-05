package sn.seye.gesmat.mefpai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import sn.seye.gesmat.mefpai.web.rest.TestUtil;

class NoteProgrammeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NoteProgramme.class);
        NoteProgramme noteProgramme1 = new NoteProgramme();
        noteProgramme1.setId(1L);
        NoteProgramme noteProgramme2 = new NoteProgramme();
        noteProgramme2.setId(noteProgramme1.getId());
        assertThat(noteProgramme1).isEqualTo(noteProgramme2);
        noteProgramme2.setId(2L);
        assertThat(noteProgramme1).isNotEqualTo(noteProgramme2);
        noteProgramme1.setId(null);
        assertThat(noteProgramme1).isNotEqualTo(noteProgramme2);
    }
}
