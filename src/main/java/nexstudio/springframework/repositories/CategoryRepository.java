package nexstudio.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import nexstudio.springframework.model.Category;

import java.util.Optional;

/**
 * Created by jt on 6/13/17.
 */
public interface CategoryRepository extends CrudRepository<Category, String> {

    Optional<Category> findByDescription(String description);
}
