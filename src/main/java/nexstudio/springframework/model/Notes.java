package nexstudio.springframework.model;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;


/**
 * Created by jt on 6/13/17.
 */
@Getter
@Setter
public class Notes {

    @Id
    private String id;
    private Recipe recipe;
    private String recipeNotes;

}
