package guru.springframework.recipe.project.recipeproject.bootstrap;

import guru.springframework.recipe.project.recipeproject.domain.Ingredient;
import guru.springframework.recipe.project.recipeproject.domain.Recipe;
import guru.springframework.recipe.project.recipeproject.repositories.CategoryRepository;
import guru.springframework.recipe.project.recipeproject.repositories.RecipeRepository;
import guru.springframework.recipe.project.recipeproject.repositories.UnitOfMeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Component
public class RecipeBootStrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootStrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initData();
    }

    private void initData() {
        Recipe guacamoleRecipe = new Recipe();
        guacamoleRecipe.setDescription("How to Make Perfect Guacamole Recipe");
        guacamoleRecipe.setDirections("1 Cut avocado, remove flesh: Cut the avocados in half. Remove seed. Score the inside of the avocado " +
                "with a blunt knife and scoop out the flesh with a spoon. (See How to Cut and Peel an Avocado.) Place in a bowl.\n " +
                "2. Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky." +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will " +
                "provide some balance to the richness of the avocado and will help delay the avocados from turning brown." +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness." +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "4 Cover with plastic and chill to store: Place plastic wrap on the surface of the guacamole cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.");


        Set<Ingredient> guacamoleRecipeIngredients = new HashSet<>();

        Ingredient avacados = new Ingredient();
        avacados.setDescription("ripe avocados");
        avacados.setAmount(BigDecimal.valueOf(2));
        avacados.setRecipe(guacamoleRecipe);

        Ingredient limeJuice = new Ingredient();
        limeJuice.setDescription("fresh lime juice or lemon juice");
        limeJuice.setAmount(BigDecimal.valueOf(1));
        limeJuice.setUom(unitOfMeasureRepository.findByUom("Tablespoon").orElse(null));
        limeJuice.setRecipe(guacamoleRecipe);

        Ingredient onion = new Ingredient();
        onion.setDescription("minced red onion or thinly sliced green onion");
        onion.setAmount(BigDecimal.valueOf(0.25));
        onion.setUom(unitOfMeasureRepository.findByUom("Cup").orElse(null));
        onion.setRecipe(guacamoleRecipe);


        guacamoleRecipeIngredients.add(avacados);
        guacamoleRecipeIngredients.add(limeJuice);
        guacamoleRecipeIngredients.add(onion);

        guacamoleRecipe.setIngredients(guacamoleRecipeIngredients);

        recipeRepository.save(guacamoleRecipe);

    }
}
