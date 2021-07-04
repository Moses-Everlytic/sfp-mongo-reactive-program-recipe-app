package nexstudio.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import nexstudio.springframework.model.UnitOfMeasure;

import java.util.Optional;

/**
 * Created by jt on 6/13/17.
 */
public interface UnitOfMeasureRepository extends CrudRepository<UnitOfMeasure, String> {

    Optional<UnitOfMeasure> findByDescription(String description);
}
