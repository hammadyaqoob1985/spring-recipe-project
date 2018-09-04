package guru.springframework.recipe.project.recipeproject.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private Integer source;
    private String url;
    @Lob
    private String directions;

    //cascase all persist all operations, Recipe is owner. Will be stored in ingredient recipe attribute
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "recipe")
    private Set<Ingredient> ingredients = new HashSet<>();

    @Lob //for large information in hibernate as hibernate only support 255 characted max. Will be stored is clob in hibernate
    private Byte[] image;

    //Use string rather than ordinal as ordinal takes index of enum and if number changes the index will be messed up
    @Enumerated(value = EnumType.STRING)
    private Difficulty difficulty;

    @OneToOne(cascade = CascadeType.ALL)
    private Notes notes;

    //added join table as only need one table to show recipes to categories. Without it two tables would be created
    @ManyToMany
    @JoinTable(name = "recipe_category",
            joinColumns = @JoinColumn(name = "recipe_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    //since method already here lombok wont override it
    //added so that when we add a note to a recipe the bidierectional relation is automatically set
    public void setNotes(Notes notes) {
        if (notes != null) {
            this.notes = notes;
            notes.setRecipe(this);
        }
    }

    //added so that when adding Ingredient to recipe bidirectional relation set
    public Recipe addIngredient(Ingredient ingredient) {
        ingredient.setRecipe(this);
        this.ingredients.add(ingredient);
        return this;
    }
}
