package nexstudio.springframework.services;

import java.util.Set;

import nexstudio.springframework.commands.RecipeCommand;
import nexstudio.springframework.model.Recipe;

/**
 * Created by jt on 6/13/17.
 */
public interface RecipeService {

    Set<Recipe> getRecipes();

    Recipe findById(String id);

    RecipeCommand findCommandById(String id);

    RecipeCommand saveRecipeCommand(RecipeCommand command);

    void deleteById(String idToDelete);
}
