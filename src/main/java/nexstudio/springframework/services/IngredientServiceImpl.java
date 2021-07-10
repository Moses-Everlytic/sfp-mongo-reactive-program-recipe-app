package nexstudio.springframework.services;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import nexstudio.springframework.commands.IngredientCommand;
import nexstudio.springframework.converters.IngredientCommandToIngredient;
import nexstudio.springframework.converters.IngredientToIngredientCommand;
import nexstudio.springframework.model.Ingredient;
import nexstudio.springframework.model.Recipe;
import nexstudio.springframework.model.UnitOfMeasure;
import nexstudio.springframework.repositories.reactive.RecipeReactiveRepository;
import nexstudio.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/28/17.
 */
@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientToIngredientCommand ingredientToIngredientCommand;
    private final IngredientCommandToIngredient ingredientCommandToIngredient;
    private final RecipeReactiveRepository recipeReactiveRepository;
    private final UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    public IngredientServiceImpl(IngredientToIngredientCommand ingredientToIngredientCommand, IngredientCommandToIngredient ingredientCommandToIngredient, RecipeReactiveRepository recipeReactiveRepository, UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository) {
        this.ingredientToIngredientCommand = ingredientToIngredientCommand;
        this.ingredientCommandToIngredient = ingredientCommandToIngredient;
        this.recipeReactiveRepository = recipeReactiveRepository;
        this.unitOfMeasureReactiveRepository = unitOfMeasureReactiveRepository;
    }

    @Override
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {

        return recipeReactiveRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToIngredientCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    @Transactional
    public Mono<IngredientCommand> saveIngredientCommand(IngredientCommand command) {
        
        Recipe recipe = recipeReactiveRepository.findById(command.getRecipeId())
                .switchIfEmpty(Mono.error(new RuntimeException("Recipe not found: " + command.getRecipeId())))
                .doOnError(thr -> log.error("error saving ingredient{}", command, thr))
                .toProcessor().block();


        Optional<Ingredient> ingredientOptional = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findAny();

        if (ingredientOptional.isPresent()) {
            Ingredient ingredientFound = ingredientOptional.get();

            ingredientFound.setDescription(command.getDescription());
            ingredientFound.setAmount(command.getAmount());
            UnitOfMeasure unitOfMeasure = getUnitOfMeasure(command).toProcessor().block();
            ingredientFound.setUom(unitOfMeasure);
        } else {
            recipe.addIngredient(ingredientCommandToIngredient.convert(command));
        }

        Recipe savedRecipe = recipeReactiveRepository.save(recipe).toProcessor().block();

        Ingredient savedIngredient = findIngredient(command, savedRecipe);

        IngredientCommand savedCommand = ingredientToIngredientCommand.convert(savedIngredient);
        savedCommand.setRecipeId(recipe.getId());
        return Mono.just(savedCommand);
    }

    private Mono<UnitOfMeasure> getUnitOfMeasure(IngredientCommand command) {
        String uomId = command.getUom().getId();
        return unitOfMeasureReactiveRepository.findById(uomId);
    }

    private Ingredient findIngredient(IngredientCommand command, Recipe savedRecipe) {
        return savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(command.getId()))
                .findAny()
                .orElse(savedRecipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                .findFirst()
                .orElse(null));
    }

    @Override
    public Mono<Void> deleteById(String recipeId, String idToDelete) {
        log.debug("Deleting ingredient: " + recipeId + ":" + idToDelete);

        return recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    recipe.getIngredients().removeIf(ingredient -> ingredient.getId().equals(idToDelete));
                    return recipe;
                })
                .flatMap(recipeReactiveRepository::save)
                .doOnError(error -> log.error("error deleting ingredient {} from recipe {}", idToDelete, recipeId, error))
                .then();                  
    }
}
