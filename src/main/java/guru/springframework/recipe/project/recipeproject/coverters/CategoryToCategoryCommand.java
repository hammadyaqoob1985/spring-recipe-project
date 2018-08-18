package guru.springframework.recipe.project.recipeproject.coverters;

import guru.springframework.recipe.project.recipeproject.commands.CategoryCommand;
import guru.springframework.recipe.project.recipeproject.domain.Category;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import static java.util.Objects.isNull;

@Controller
public class CategoryToCategoryCommand implements Converter<Category, CategoryCommand> {
    @Synchronized
    @Nullable
    @Override
    public CategoryCommand convert(Category source) {
        if(isNull(source)) {
            return null;
        }

        CategoryCommand categoryCommand = new CategoryCommand();
        categoryCommand.setId(source.getId());
        categoryCommand.setDescription(source.getDescription());
        return categoryCommand;

    }
}
