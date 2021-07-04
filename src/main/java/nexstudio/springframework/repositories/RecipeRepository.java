package nexstudio.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import nexstudio.springframework.model.Recipe;

/**
 * Created by jt on 6/13/17.
 */
public interface RecipeRepository extends CrudRepository<Recipe, String> {
}
