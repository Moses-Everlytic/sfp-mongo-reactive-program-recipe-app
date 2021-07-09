package nexstudio.springframework.controllers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import nexstudio.springframework.model.Recipe;
import nexstudio.springframework.services.RecipeService;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 6/19/17.
 */
@RunWith(SpringRunner.class)
@WebFluxTest(RecipeController.class)
@Import({ThymeleafAutoConfiguration.class})
public class RecipeControllerTest {

    @MockBean
    RecipeService recipeService;

    @Autowired
    RecipeController controller;

    @Autowired
    WebTestClient webTestClient;

    @Test
    public void testGetRecipe() throws Exception {

        Recipe recipe = new Recipe();
        recipe.setId("1");

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        webTestClient.get().uri("/recipe/1/show")
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody(String.class)
                        .consumeWith(repsonse -> {
                                String responseBody = Objects.requireNonNull(repsonse.getResponseBody());
                                assertTrue(responseBody.contains("recipe"));
                                assertTrue(responseBody.contains("recipe/show"));
                        });
    }

//     @Test
//     public void testGetRecipeNotFound() throws Exception {

//         when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

//         mockMvc.perform(get("/recipe/1/show"))
//                 .andExpect(status().isNotFound())
//                 .andExpect(view().name("404error"));
//     }

//     @Test
//     public void testGetNewRecipeForm() throws Exception {
//         // RecipeCommand command = new RecipeCommand();

//         mockMvc.perform(get("/recipe/new"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("recipe/recipeform"))
//                 .andExpect(model().attributeExists("recipe"));
//     }

//     @Test
//     public void testPostNewRecipeForm() throws Exception {
//         RecipeCommand command = new RecipeCommand();
//         command.setId("2");

//         when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

//         mockMvc.perform(post("/recipe")
//                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                 .param("id", "")
//                 .param("description", "some string")
//                 .param("directions", "some directions")
//         )
//                 .andExpect(status().is3xxRedirection())
//                 .andExpect(view().name("redirect:/recipe/2/show"));
//     }

//     @Test
//     public void testPostNewRecipeFormValidationFail() throws Exception {
//         RecipeCommand command = new RecipeCommand();
//         command.setId("2");

//         when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

//         mockMvc.perform(post("/recipe")
//                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                 .param("id", "")
//                 .param("cookTime", "3000")

//         )
//                 .andExpect(status().isOk())
//                 .andExpect(model().attributeExists("recipe"))
//                 .andExpect(view().name("recipe/recipeform"));
//     }

//     @Test
//     public void testGetUpdateView() throws Exception {
//         RecipeCommand command = new RecipeCommand();
//         command.setId("2");

//         when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

//         mockMvc.perform(get("/recipe/1/update"))
//                 .andExpect(status().isOk())
//                 .andExpect(view().name("recipe/recipeform"))
//                 .andExpect(model().attributeExists("recipe"));
//     }

//     @Test
//     public void testDeleteAction() throws Exception {
//         mockMvc.perform(get("/recipe/1/delete"))
//                 .andExpect(status().is3xxRedirection())
//                 .andExpect(view().name("redirect:/"));

//         verify(recipeService, times(1)).deleteById(anyString());
//     }
}