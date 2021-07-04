package nexstudio.springframework.services;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import nexstudio.springframework.commands.RecipeCommand;
import nexstudio.springframework.converters.RecipeCommandToRecipe;
import nexstudio.springframework.converters.RecipeToRecipeCommand;
import nexstudio.springframework.model.Recipe;
import nexstudio.springframework.repositories.reactive.RecipeReactiveRepository;


/**
 * Created by jt on 6/21/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecipeServiceIT {

    public static final String NEW_DESCRIPTION = "New Description";

    @Autowired
    RecipeService recipeService;

    @Autowired
    RecipeReactiveRepository recipeReactiveRepository;

    @Autowired
    RecipeCommandToRecipe recipeCommandToRecipe;

    @Autowired
    RecipeToRecipeCommand recipeToRecipeCommand;

  //  @Transactional
    @Test
    public void testSaveOfDescription() throws Exception {
        //given
        List<Recipe> recipes = recipeReactiveRepository.findAll().collectList().block();
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testRecipeCommand = recipeToRecipeCommand.convert(testRecipe);

        //when
        testRecipeCommand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedRecipeCommand = recipeService.saveRecipeCommand(testRecipeCommand).block();

        //then
        assertEquals(NEW_DESCRIPTION, savedRecipeCommand.getDescription());
        assertEquals(testRecipe.getId(), savedRecipeCommand.getId());
        assertEquals(testRecipe.getCategories().size(), savedRecipeCommand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedRecipeCommand.getIngredients().size());
    }
}
