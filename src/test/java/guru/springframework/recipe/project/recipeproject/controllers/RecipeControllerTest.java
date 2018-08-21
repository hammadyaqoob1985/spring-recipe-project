package guru.springframework.recipe.project.recipeproject.controllers;

import guru.springframework.recipe.project.recipeproject.commands.RecipeCommand;
import guru.springframework.recipe.project.recipeproject.domain.Recipe;
import guru.springframework.recipe.project.recipeproject.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RecipeControllerTest {

    RecipeController recipeController;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        recipeController = new RecipeController(recipeService);
    }

    @Test
    public void getRecipeMockMvc() throws Exception {
        Recipe recipe1 =  new Recipe();
        recipe1.setId(1L);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

       when(recipeService.findById(anyLong())).thenReturn(recipe1);

       mockMvc.perform(get("/recipe/show/1"))
               .andExpect(status().isOk())
               .andExpect(view().name("/recipe/show"))
       .andExpect(model().attributeExists("recipe"));

    }

    @Test
    public void getRecipe() {
        Recipe recipe1 =  new Recipe();
        recipe1.setId(1L);

        when(recipeService.findById(anyLong())).thenReturn(recipe1);

        String returnedString = recipeController.getRecipe(model,"1");

        assertEquals(returnedString,"/recipe/show");

        ArgumentCaptor<Recipe> captor = ArgumentCaptor.forClass(Recipe.class);
        verify(model,times(1)).addAttribute(eq("recipe"), captor.capture());
        verify(recipeService,times(1)).findById(1L);

        assertEquals(captor.getValue(),recipe1);

    }

    @Test
    public void showAddRecipePageMockMvc() throws Exception {

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    public void showAddRecipePage() {

        String returnedString = recipeController.newRecipe(model);

        assertEquals(returnedString,"recipe/recipeform");

        verify(model,times(1)).addAttribute(eq("recipe"), any(RecipeCommand.class));

    }

    @Test
    public void saveOrUpdateMockMvc() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(recipeCommand);

        mockMvc.perform(post("/recipe/", recipeCommand))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/show/" + recipeCommand.getId()));

    }

    @Test
    public void saveOrUpdate() {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(recipeCommand);

        String returnedString = recipeController.saveOrUpdate(recipeCommand);

        assertEquals(returnedString,"redirect:/recipe/show/" + recipeCommand.getId());

        ArgumentCaptor<RecipeCommand> recipeCommandArgumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);
        verify(recipeService,times(1)).saveRecipeCommand(recipeCommandArgumentCaptor.capture());

        assertEquals(recipeCommandArgumentCaptor.getValue(),recipeCommand);

    }
}