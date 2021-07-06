package nexstudio.springframework.services;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import nexstudio.springframework.repositories.reactive.RecipeReactiveRepository;
import reactor.core.publisher.Mono;

/**
 * Created by jt on 7/3/17.
 */
@Service
public class ImageServiceImpl implements ImageService {


    private final RecipeReactiveRepository recipeReactiveRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeReactiveRepository) {
        this.recipeReactiveRepository = recipeReactiveRepository;
    }

    @Override
    public Mono<Void> saveImageFile(String recipeId, MultipartFile file) {

        recipeReactiveRepository.findById(recipeId)
                .map(recipe -> {
                    Byte[] byteObjects = new Byte[0];

                    try {
                        byteObjects = new Byte[file.getBytes().length];

                        int i = 0;

                        for (byte b : file.getBytes()){
                            byteObjects[i++] = b;
                        }
                
                        recipe.setImage(byteObjects);
                        
                        return recipe;
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }).publish(recipeMono -> recipeReactiveRepository.save(recipeMono.block())).block();

        return Mono.empty();
    }
}
