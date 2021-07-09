package nexstudio.springframework.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;

import nexstudio.springframework.model.Recipe;
import nexstudio.springframework.services.RecipeService;
import reactor.core.publisher.Flux;

/**
 * Created by jt on 6/17/17.
 */
@RunWith(SpringRunner.class)
@WebFluxTest(IndexController.class)
@Import({ThymeleafAutoConfiguration.class})
public class IndexControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    Model model;

    @Autowired
    IndexController controller;

    @Test
    public void testMockMVC() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setDescription("Life Lemons");

        when(recipeService.getRecipes()).thenReturn(Flux.just(recipe));

        webTestClient.get().uri("/")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .consumeWith(response -> {
                    String responseBody = Objects.requireNonNull(response.getResponseBody());

                    assertTrue(responseBody.contains("Life Lemons"));
                });

        verify(recipeService, times(1)).getRecipes();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getIndexPage() throws Exception {

        //given
        Set<Recipe> recipes = new HashSet<>();
        recipes.add(new Recipe());

        Recipe recipe = new Recipe();
        recipe.setId("1");
        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));

        ArgumentCaptor<Flux<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Flux.class);

        //when
        String viewName = controller.getIndexPage(model);


        //then
        assertEquals("index", viewName);
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"), argumentCaptor.capture());
        Flux<Recipe> fluxInController = argumentCaptor.getValue();
        List<Recipe> recipeLists = fluxInController.collectList().block();
        assertEquals(2, recipeLists.size());
    }

}