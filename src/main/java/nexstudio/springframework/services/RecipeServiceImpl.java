package nexstudio.springframework.services;

import org.springframework.stereotype.Service;

import nexstudio.springframework.commands.RecipeCommand;
import nexstudio.springframework.converters.RecipeCommandToRecipe;
import nexstudio.springframework.converters.RecipeToRecipeCommand;
import nexstudio.springframework.model.Recipe;
import nexstudio.springframework.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/13/17.
 */
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeReactiveRepository;
    private final RecipeCommandToRecipe recipeCommandToRecipe;
    private final RecipeToRecipeCommand recipeToRecipeCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeReactiveRepository,
            RecipeCommandToRecipe recipeCommandToRecipe, RecipeToRecipeCommand recipeToRecipeCommand) {
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.recipeCommandToRecipe = recipeCommandToRecipe;
        this.recipeToRecipeCommand = recipeToRecipeCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        return recipeReactiveRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeReactiveRepository.findById(id);
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeReactiveRepository.findById(id)
                .map(recipe -> {
                    RecipeCommand recipeCommand = recipeToRecipeCommand.convert(recipe);

                    recipeCommand.getIngredients().forEach(rc -> {
                        rc.setRecipeId(recipeCommand.getId());
                    });

                    return recipeCommand;
                });
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        // Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

        // Recipe savedRecipe = recipeReactiveRepository.save(detachedRecipe).block();
        // log.debug("Saved RecipeId:" + savedRecipe.getId());

        // return recipeToRecipeCommand.convert(savedRecipe);

        return recipeReactiveRepository.save(recipeCommandToRecipe.convert(command))
                .map(recipeToRecipeCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String idToDelete) {
        recipeReactiveRepository.deleteById(idToDelete).block();
        return Mono.empty();
    }
}
