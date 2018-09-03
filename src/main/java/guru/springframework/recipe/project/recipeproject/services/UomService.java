package guru.springframework.recipe.project.recipeproject.services;

import guru.springframework.recipe.project.recipeproject.commands.UnitOfMeasureCommand;

import java.util.Set;

public interface UomService {

    Set<UnitOfMeasureCommand> getAllUoms();
}
