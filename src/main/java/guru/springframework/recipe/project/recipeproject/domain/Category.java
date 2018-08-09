package guru.springframework.recipe.project.recipeproject.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

//lombok creates getters, setters, constructors, toequals and tostring methods for you for each of your properties
@Data
//avoid addind recipes in hashcode as can cause circular reference error
@EqualsAndHashCode(exclude = {"recipes"})
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String description;

    //do not need to have table in jpa for this as already done in recipe categories variable
    @ManyToMany(mappedBy = "categories")
    Set<Recipe> recipes;

}
