package nexstudio.springframework.controllers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import nexstudio.springframework.config.WebConfig;
import nexstudio.springframework.model.Recipe;
import nexstudio.springframework.services.RecipeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class RouterFunctionTest {
    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        WebConfig webConfig = new WebConfig();
        
        RouterFunction<?> routerFunction = webConfig.routes(recipeService);

        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build(); 
    }

    @Test
    public void shouldReturnRecipes_API() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setId("1");
        Recipe recipe2 = new Recipe();
        recipe2.setId("2");


        List<Recipe> listOfRecipes = Arrays.asList(
            recipe1,
            recipe2
        );

        given(recipeService.getRecipes()).willReturn(Flux.fromIterable(listOfRecipes));

        webTestClient.get()
                .uri("/api/recipes")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Recipe.class);
    }

    @Test
    public void shouldReturnRecipeById_API() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        webTestClient.get()
                .uri("/api/recipe/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Recipe.class);
    }
}
