package nexstudio.springframework.repositories.reactive;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import nexstudio.springframework.model.Recipe;

@RunWith(SpringRunner.class)
@DataMongoTest
public class RecipeReactiveRepositoryTest {

	@Autowired
	private RecipeReactiveRepository recipeReactiveRepository;

	@Before
	public void setup() throws Exception {
		recipeReactiveRepository.deleteAll().block();
	}

	@Test
	public void shouldSaveRecipe() throws Exception {
		Recipe recipe = new Recipe();
		recipe.setDescription("Food Stuff");

		recipeReactiveRepository.save(recipe).block();

		Long count = recipeReactiveRepository.count().block();

		assertEquals(Long.valueOf(1L), count);
	}
}