package com.crossover.techtrial.service;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.crossover.techtrial.RandomGenerator;
import com.crossover.techtrial.dto.DailyElectricity;
import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.repository.HourlyElectricityRepository;

@RunWith(MockitoJUnitRunner.class)
public class HourlyElectricityServiceImplTest {

  @Mock
  HourlyElectricityRepository hourlyElectricityRepository;

  @InjectMocks
  HourlyElectricityServiceImpl service = new HourlyElectricityServiceImpl();

  @Before
  public void before() {
    assertThat(mockingDetails(service.hourlyElectricityRepository).isMock(), is(true));
  }

  @Test
  public void getPanelDailyStatisticsTest() {

    final Long validPanelId = 1L;
    final LocalDate today = LocalDate.now();
    final LocalDate yesterday = LocalDate.now().minusDays(1);
    final LocalDate dayBeforeYesterday = LocalDate.now().minusDays(2);
    final LocalDate day2BeforeYesterday = LocalDate.now().minusDays(3);

    when(hourlyElectricityRepository.findAllByPanelId(validPanelId))
	.thenAnswer(new Answer<Iterable<HourlyElectricity>>() {
	  @Override
	  public Iterable<HourlyElectricity> answer(InvocationOnMock invocation) throws Throwable {

	    Stream<HourlyElectricity> p1 = LongStream.of(1000L, 3000L, 5000L).mapToObj(el -> {
	      HourlyElectricity he = mock(HourlyElectricity.class);
	      when(he.getGeneratedElectricity()).thenReturn(el);
	      when(he.getReadingAt()).thenReturn(RandomGenerator.randomLocalDateTime(dayBeforeYesterday));
	      return he;
	    });

	    Stream<HourlyElectricity> p2 = LongStream.of(2000L, 4000L, 6000L).mapToObj(el -> {
	      HourlyElectricity he = mock(HourlyElectricity.class);
	      when(he.getGeneratedElectricity()).thenReturn(el);
	      when(he.getReadingAt()).thenReturn(RandomGenerator.randomLocalDateTime(yesterday));
	      return he;
	    });

	    Stream<HourlyElectricity> p3 = LongStream.of(3000L, 5000L, 7000L).mapToObj(el -> {
	      HourlyElectricity he = mock(HourlyElectricity.class);
	      when(he.getGeneratedElectricity()).thenReturn(el);
	      when(he.getReadingAt()).thenReturn(RandomGenerator.randomLocalDateTime(day2BeforeYesterday));
	      return he;
	    });

	    Stream<HourlyElectricity> todayHourlyIgnored = LongStream.of(10_000L, 11_000L, 12_000L).mapToObj(el -> {
	      HourlyElectricity he = mock(HourlyElectricity.class);
	      when(he.getReadingAt()).thenReturn(RandomGenerator.randomLocalDateTime(today));
	      return he;
	    });

	    return Stream.concat(p1, Stream.concat(p2, Stream.concat(p3, todayHourlyIgnored)))
		.collect(Collectors.toList());
	  }
	});

    final List<DailyElectricity> res = service.getPanelDailyStatistics(validPanelId);

    assertThat(res, allOf(not(nullValue()), hasSize(3)));
    DailyElectricity d1 = res.get(0);
    assertThat(d1.getDate(), equalTo(yesterday));
    assertThat(d1.getMax(), equalTo(6000L));
    assertThat(d1.getMin(), equalTo(2000L));
    assertThat(d1.getSum(), equalTo(12000L));
    assertThat(d1.getAverage(), equalTo(4000d));

    DailyElectricity d2 = res.get(1);
    assertThat(d2.getDate(), equalTo(dayBeforeYesterday));
    assertThat(d2.getMax(), equalTo(5000L));
    assertThat(d2.getMin(), equalTo(1000L));
    assertThat(d2.getSum(), equalTo(9000L));
    assertThat(d2.getAverage(), equalTo(3000d));

    DailyElectricity d3 = res.get(2);
    assertThat(d3.getDate(), equalTo(day2BeforeYesterday));
    assertThat(d3.getMax(), equalTo(7000L));
    assertThat(d3.getMin(), equalTo(3000L));
    assertThat(d3.getSum(), equalTo(15000L));
    assertThat(d3.getAverage(), equalTo(5000d));

    final long wrongPanelId = 12L;

    List<DailyElectricity> res2 = service.getPanelDailyStatistics(wrongPanelId);
    assertThat(res2, allOf(not(nullValue()), empty()));
  }
}
