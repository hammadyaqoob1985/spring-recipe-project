package guru.springframework.recipe.project.recipeproject.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
//avoid addind recipes in hashcode as can cause circular reference error
@EqualsAndHashCode(exclude = {"recipe"})
@Entity
public class Notes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    //does not need cascade as when we delete note we do not want to delete recipe
    @OneToOne
    private Recipe recipe;

    @Lob //for large information in hibernate as hibernate only support 255 characted max. Will be stored is clob in hibernate
    private String recipeNotes;

}
