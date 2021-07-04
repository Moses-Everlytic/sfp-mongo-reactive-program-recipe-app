package nexstudio.springframework.repositories.reactive;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import nexstudio.springframework.model.Recipe;

public interface RecipeReactiveRepository extends ReactiveMongoRepository<Recipe, String> {  
}
