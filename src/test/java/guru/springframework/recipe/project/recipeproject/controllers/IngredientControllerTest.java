package guru.springframework.recipe.project.recipeproject.controllers;

import guru.springframework.recipe.project.recipeproject.commands.IngredientCommand;
import guru.springframework.recipe.project.recipeproject.commands.RecipeCommand;
import guru.springframework.recipe.project.recipeproject.commands.UnitOfMeasureCommand;
import guru.springframework.recipe.project.recipeproject.services.IngredientService;
import guru.springframework.recipe.project.recipeproject.services.RecipeService;
import guru.springframework.recipe.project.recipeproject.services.UomService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class IngredientControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    IngredientService ingredientService;

    @Mock
    UomService uomService;

    @Mock
    Model model;

    @Captor
    private ArgumentCaptor<Set<UnitOfMeasureCommand>> captor;

    IngredientController ingredientController;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        ingredientController = new IngredientController(recipeService, ingredientService, uomService);
        mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
    }

    @Test
    public void testListIngredientsView() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        mockMvc.perform(get("/recipe/1/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/list"))
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService, times(1)).findCommandById(anyLong());
    }

    @Test
    public void testListIngredients() throws Exception {

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

        ingredientController.getIngredientsList(model,"1");

        ArgumentCaptor<RecipeCommand> argumentCaptorRecipeCommand = ArgumentCaptor.forClass(RecipeCommand.class);
        ArgumentCaptor<Long> argumentCaptorLong = ArgumentCaptor.forClass(Long.class);

        verify(recipeService, times(1)).findCommandById(argumentCaptorLong.capture());
        assertEquals(argumentCaptorLong.getValue(), new Long("1"));

        verify(model, times(1)).addAttribute(eq("recipe"), argumentCaptorRecipeCommand.capture());
        assertEquals(argumentCaptorRecipeCommand.getValue(), recipeCommand);
    }

    @Test
    public void testIndividualIngredientView() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/show"))
                .andExpect(model().attributeExists("ingredient"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
    }

    @Test
    public void testIndividualIngredient() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);

        RecipeCommand recipeCommand = new RecipeCommand();
        recipeCommand.setId(1L);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);

        assertEquals(ingredientController.showIngredientForRecipe(model, "1", "2"), "recipe/ingredient/show");

        ArgumentCaptor<IngredientCommand> argumentCaptorIngredientCommand = ArgumentCaptor.forClass(IngredientCommand.class);
        ArgumentCaptor<Long> argumentCaptorLong = ArgumentCaptor.forClass(Long.class);

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(argumentCaptorLong.capture(), argumentCaptorLong.capture());
        assertEquals(argumentCaptorLong.getAllValues().get(0), new Long("1"));
        assertEquals(argumentCaptorLong.getAllValues().get(1), new Long("2"));

        verify(model, times(1)).addAttribute(eq("ingredient"), argumentCaptorIngredientCommand.capture());
        assertEquals(argumentCaptorIngredientCommand.getValue(), ingredientCommand);

    }

    @Test
    public void testUpdateIngredientView() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);

        Set<UnitOfMeasureCommand> unitOfMeasureCommands =  new HashSet<>();
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(1L);

        UnitOfMeasureCommand unitOfMeasureCommand1 = new UnitOfMeasureCommand();
        unitOfMeasureCommand1.setId(2L);

        unitOfMeasureCommands.add(unitOfMeasureCommand);
        unitOfMeasureCommands.add(unitOfMeasureCommand1);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        when(uomService.getAllUoms()).thenReturn(unitOfMeasureCommands);

        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredient/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));

        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(anyLong(), anyLong());
        verify(uomService, times(1)).getAllUoms();
    }

    @Test
    public void testUpdateIngredient() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(2L);

        Set<UnitOfMeasureCommand> unitOfMeasureCommands =  new HashSet<>();
        UnitOfMeasureCommand unitOfMeasureCommand = new UnitOfMeasureCommand();
        unitOfMeasureCommand.setId(1L);

        UnitOfMeasureCommand unitOfMeasureCommand1 = new UnitOfMeasureCommand();
        unitOfMeasureCommand1.setId(2L);

        unitOfMeasureCommands.add(unitOfMeasureCommand);
        unitOfMeasureCommands.add(unitOfMeasureCommand1);

        when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong())).thenReturn(ingredientCommand);
        when(uomService.getAllUoms()).thenReturn(unitOfMeasureCommands);

        ingredientController.updateIngredientForRecipe(model, "1", "2");

        ArgumentCaptor<Long> argumentCaptorLong = ArgumentCaptor.forClass(Long.class);
        verify(ingredientService, times(1)).findByRecipeIdAndIngredientId(argumentCaptorLong.capture(), argumentCaptorLong.capture());
        assertEquals(argumentCaptorLong.getAllValues().get(0), new Long("1"));
        assertEquals(argumentCaptorLong.getAllValues().get(1), new Long("2"));

        ArgumentCaptor<IngredientCommand> argumentCaptorIngredientCommand = ArgumentCaptor.forClass(IngredientCommand.class);
        verify(model,times(1)).addAttribute(eq("ingredient"), argumentCaptorIngredientCommand.capture());
        assertEquals(ingredientCommand, argumentCaptorIngredientCommand.getValue());

        verify(model,times(1)).addAttribute(eq("uomList"), captor.capture());
        assertEquals(unitOfMeasureCommands, captor.getValue());
    }

    @Test
    public void saveOrUpdateMockMvc() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);
        ingredientCommand.setRecipeId(3L);

        when(ingredientService.saveIngredientCommand(any(IngredientCommand.class))).thenReturn(ingredientCommand);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ingredientController).build();
        mockMvc.perform(post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", "some string"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/3/ingredient/1/show"));
    }

    @Test
    public void saveOrUpdate() throws Exception {

        IngredientCommand ingredientCommand = new IngredientCommand();
        ingredientCommand.setId(1L);
        ingredientCommand.setRecipeId(3L);

        when(ingredientService.saveIngredientCommand(any(IngredientCommand.class))).thenReturn(ingredientCommand);

        String page = ingredientController.saveOrUpdate(ingredientCommand);

        assertEquals(page, "redirect:/recipe/3/ingredient/1/show");

        ArgumentCaptor<IngredientCommand> ingredientCommandArgumentCaptor = ArgumentCaptor.forClass(IngredientCommand.class);
        verify(ingredientService,times(1)).saveIngredientCommand(ingredientCommandArgumentCaptor.capture());
        assertEquals(ingredientCommand, ingredientCommandArgumentCaptor.getValue());


    }
}