package nexstudio.springframework.repositories.reactive;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;

import nexstudio.springframework.model.UnitOfMeasure;    

@RunWith(SpringRunner.class)
@DataMongoTest
public class UnitOfMeasureReactiveRepositoryTest {

    @Autowired
    private UnitOfMeasureReactiveRepository unitOfMeasureReactiveRepository;

    @Before
    public void setup(){
        unitOfMeasureReactiveRepository.deleteAll();
    }
        
    @Test
    public void shouldSaveUnitOfMeasure() throws Exception {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription("Cup");

        unitOfMeasureReactiveRepository.save(uom).block();

        Long count = unitOfMeasureReactiveRepository.count().block();

        assertEquals(Long.valueOf(1L), count);
    }

    @Test
	public void shouldFindByDescription() throws Exception {
		UnitOfMeasure uom = new UnitOfMeasure();
		uom.setDescription("Each");

		unitOfMeasureReactiveRepository.save(uom).block();

		UnitOfMeasure fetchUOM = unitOfMeasureReactiveRepository.findByDescription("Each").block();

		assertNotNull(fetchUOM.getId());
	}
}
    