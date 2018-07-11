package com.crossover.techtrial.repository;

import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.RandomGenerator;
import com.crossover.techtrial.model.Panel;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PanelRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private PanelRepository panelRepository;

  @Test
  public void findBySerialTest() {

    // given

    final Panel panel1 = entityManager
	.persist(new Panel(RandomGenerator.randomPanelSerial(), RandomGenerator.randomLongitude(),
	    RandomGenerator.randomLatitude(), "Solar inc."));

    entityManager.flush();

    // when

    final Panel found = panelRepository.findBySerial(panel1.getSerial());

    // then

    assertThat(found)
	.isEqualTo(panel1);
  }
}
