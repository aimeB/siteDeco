package com.decormoi.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.decormoi.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategorieProduitTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorieProduit.class);
        CategorieProduit categorieProduit1 = new CategorieProduit();
        categorieProduit1.setId(1L);
        CategorieProduit categorieProduit2 = new CategorieProduit();
        categorieProduit2.setId(categorieProduit1.getId());
        assertThat(categorieProduit1).isEqualTo(categorieProduit2);
        categorieProduit2.setId(2L);
        assertThat(categorieProduit1).isNotEqualTo(categorieProduit2);
        categorieProduit1.setId(null);
        assertThat(categorieProduit1).isNotEqualTo(categorieProduit2);
    }
}
