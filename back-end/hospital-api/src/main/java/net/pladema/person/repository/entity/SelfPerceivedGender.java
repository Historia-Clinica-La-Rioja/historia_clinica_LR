package net.pladema.person.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "self_perceived_gender")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelfPerceivedGender  implements Serializable {
        /*
         */
        private static final long serialVersionUID = 5871312991523529690L;
        //El orden de las variables es el definido por la columna Order en el script: incrementales-v1_24
        public static final short MUJER_CIS = 1;
		public static final short VARON_CIS = 2;
		public static final short TRAVESTI = 3;
		public static final short MUJER_TRANS = 4;
        public static final short NO_BINARIE = 5;
        public static final short VARON_TRANS = 6;
        public static final short GAY = 7;
        public static final short LESBIANA = 8;
        public static final short GENERO_FLUIDO = 9;
        public static final short NINGUNA_DE_LAS_ANTERIORES = 10;
        public static final List<Short> GENDERS = List.of(MUJER_CIS, VARON_CIS, TRAVESTI, MUJER_TRANS, NO_BINARIE, VARON_TRANS, GAY, LESBIANA, GENERO_FLUIDO, NINGUNA_DE_LAS_ANTERIORES);

        @Id
        @Column(name = "id", nullable = false)
        private Short id;

        @Column(name = "description", length = 25, nullable = false)
        private String description;

        @Column(name = "orden", nullable = false)
        private Short orden;
}
