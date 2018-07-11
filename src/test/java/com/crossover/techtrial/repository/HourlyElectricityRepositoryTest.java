package com.crossover.techtrial.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.crossover.techtrial.RandomGenerator;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;

@RunWith(SpringRunner.class)
@DataJpaTest
public class HourlyElectricityRepositoryTest {

  @Autowired
  TestEntityManager entityManager;

  @Autowired
  HourlyElectricityRepository employeeRepository;

  Panel panel1;
  Collection<HourlyElectricity> electricities1;

  Panel panel2;
  Collection<HourlyElectricity> electricities2;

  @Before
  public void tuneUp() {
    // given
    panel1 = entityManager
	.persist(new Panel(RandomGenerator.randomPanelSerial(), RandomGenerator.randomLongitude(),
	    RandomGenerator.randomLatitude(), "Solar inc."));

    electricities1 = Collections.unmodifiableList(IntStream.range(0, 10)
	.mapToObj(x -> new HourlyElectricity(panel1, RandomGenerator.generatedElectricity(),
	    LocalDateTime.now()))
	.map(entityManager::persist)
	.collect(Collectors.toList()));

    panel2 = entityManager
	.persist(new Panel(RandomGenerator.randomPanelSerial(), RandomGenerator.randomLongitude(),
	    RandomGenerator.randomLatitude(), "Paneling inc."));

    electricities2 = Collections.unmodifiableList(IntStream.range(0, 20)
	.mapToObj(x -> new HourlyElectricity(panel2, RandomGenerator.generatedElectricity(),
	    LocalDateTime.now()))
	.map(entityManager::persist)
	.collect(Collectors.toList()));

    entityManager.flush();

  }

  @Test
  public void findAllByPanelIdTest() {

    // when
    final Iterable<HourlyElectricity> found1 = employeeRepository.findAllByPanelId(panel1.getId());
    final Iterable<HourlyElectricity> found2 = employeeRepository.findAllByPanelId(panel2.getId());

    // then
    assertThat(found1)
	.hasSize(electricities1.size())
	.containsAll(electricities1)
	.doesNotContainAnyElementsOf(electricities2);

    assertThat(found2)
	.hasSize(electricities2.size())
	.containsAll(electricities2)
	.doesNotContainAnyElementsOf(electricities1);
  }

  @Test
  public void findAllByPanelIdOrderByReadingAtDescTest() {

    // when
    final Page<HourlyElectricity> found1 = employeeRepository.findAllByPanelIdOrderByReadingAtDesc(panel1.getId(),
	Pageable.unpaged());
    final Page<HourlyElectricity> found2 = employeeRepository.findAllByPanelIdOrderByReadingAtDesc(panel2.getId(),
	Pageable.unpaged());

    // then
    assertThat(found1)
	.hasSize(electricities1.size())
	.containsAll(electricities1)
	.doesNotContainAnyElementsOf(electricities2);

    assertThat(found2)
	.hasSize(electricities2.size())
	.containsAll(electricities2)
	.doesNotContainAnyElementsOf(electricities1);
  }
}
