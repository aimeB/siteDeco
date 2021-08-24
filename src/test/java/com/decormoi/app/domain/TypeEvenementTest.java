package com.decormoi.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.decormoi.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeEvenementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeEvenement.class);
        TypeEvenement typeEvenement1 = new TypeEvenement();
        typeEvenement1.setId(1L);
        TypeEvenement typeEvenement2 = new TypeEvenement();
        typeEvenement2.setId(typeEvenement1.getId());
        assertThat(typeEvenement1).isEqualTo(typeEvenement2);
        typeEvenement2.setId(2L);
        assertThat(typeEvenement1).isNotEqualTo(typeEvenement2);
        typeEvenement1.setId(null);
        assertThat(typeEvenement1).isNotEqualTo(typeEvenement2);
    }
}
