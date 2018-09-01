package guru.springframework.recipe.project.recipeproject.services;

import guru.springframework.recipe.project.recipeproject.commands.IngredientCommand;
import guru.springframework.recipe.project.recipeproject.coverters.IngredientToIngredientCommand;
import guru.springframework.recipe.project.recipeproject.coverters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.recipe.project.recipeproject.domain.Ingredient;
import guru.springframework.recipe.project.recipeproject.domain.Recipe;
import guru.springframework.recipe.project.recipeproject.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class IngredientServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;


    private final IngredientToIngredientCommand ingredientToIngredientCommand;

    private IngredientService ingredientService;


    public IngredientServiceImplTest() {
        this.ingredientToIngredientCommand = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientToIngredientCommand);
    }

    @Test
    public void findByRecipeIdAndIngredientId() {

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setRecipe(recipe);
        Ingredient ingredient2 = new Ingredient();
        ingredient2.setId(2L);
        ingredient2.setRecipe(recipe);


        recipe.setIngredients(new HashSet<>(Arrays.asList(ingredient, ingredient2)));

        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(recipe));

        IngredientCommand ingredientCommand = ingredientService.findByRecipeIdAndIngredientId(1L,2L);

        assertEquals(ingredientCommand.getId(),Long.valueOf(2));
        assertEquals(ingredientCommand.getRecipeId(),Long.valueOf(1));

    }
}