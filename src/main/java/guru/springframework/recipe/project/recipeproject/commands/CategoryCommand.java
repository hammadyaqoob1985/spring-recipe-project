package guru.springframework.recipe.project.recipeproject.commands;

import guru.springframework.recipe.project.recipeproject.domain.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryCommand {
    private Long Id;
    private String description;
    Set<Recipe> recipes;
}
