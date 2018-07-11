package com.crossover.techtrial.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.crossover.techtrial.RandomGenerator;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.repository.PanelRepository;

@RunWith(MockitoJUnitRunner.class)
public class PanelServiceImplTest {

  @Mock
  PanelRepository panelRepository;

  @InjectMocks
  PanelServiceImpl service = new PanelServiceImpl();

  @Before
  public void before() {
    assertThat(mockingDetails(service.panelRepository).isMock(), is(true));
  }

  @Test
  public void findBySerialTest() {

    final String serial = RandomGenerator.randomPanelSerial();

    Panel target = new Panel();
    target.setId(RandomGenerator.generatedId());
    target.setLatitude(RandomGenerator.randomLatitude());
    target.setLongitude(RandomGenerator.randomLongitude());
    target.setSerial(serial);
    target.setBrand("Apple");

    when(panelRepository.findBySerial(target.getSerial())).thenReturn(target);

    Panel res = service.findBySerial(serial);
    assertThat(res, equalTo(target));

    Panel res2 = service.findBySerial("qqq");
    assertThat(res2, nullValue());
  }
}
