package guru.springframework.recipe.project.recipeproject.controllers;

import guru.springframework.recipe.project.recipeproject.commands.RecipeCommand;
import guru.springframework.recipe.project.recipeproject.domain.Recipe;
import guru.springframework.recipe.project.recipeproject.exceptions.NotFoundException;
import guru.springframework.recipe.project.recipeproject.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
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

       when(recipeService.findById(anyLong())).thenReturn(recipe1);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
       mockMvc.perform(get("/recipe/1/show"))
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

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(recipeCommand);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + recipeCommand.getId() + "/show/"));

    }

    @Test
    public void saveOrUpdate() {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.saveRecipeCommand(any(RecipeCommand.class))).thenReturn(recipeCommand);

        String returnedString = recipeController.saveOrUpdate(recipeCommand);

        assertEquals(returnedString,"redirect:/recipe/" + recipeCommand.getId() + "/show/");

        ArgumentCaptor<RecipeCommand> recipeCommandArgumentCaptor = ArgumentCaptor.forClass(RecipeCommand.class);
        verify(recipeService,times(1)).saveRecipeCommand(recipeCommandArgumentCaptor.capture());

        assertEquals(recipeCommandArgumentCaptor.getValue(),recipeCommand);

    }

    @Test
    public void testGetUpdatedView() throws Exception {
        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        mockMvc.perform(get("/recipe/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));

    }

    @Test
    public void delete() {

        String returnedString = recipeController.deleteById("1");

        assertEquals(returnedString,"redirect:/");

        verify(recipeService,times(1)).deleteById(new Long(1));
    }

    @Test
    public void testDeleteRecipe() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        mockMvc.perform(get("/recipe/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        verify(recipeService,times(1)).deleteById(new Long(1));

    }

    @Test
    public void recipeNotFound() throws Exception {

        when(recipeService.findById(anyLong())).thenThrow(NotFoundException.class);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(recipeController).build();
        mockMvc.perform(get("/recipe/1/show"))
                .andExpect(status().isNotFound());

    }
}