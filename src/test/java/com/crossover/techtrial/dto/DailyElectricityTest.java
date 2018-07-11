package com.crossover.techtrial.dto;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.Month;
import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class DailyElectricityTest {

  @Test
  public void publicNoargsConstructorTest() {
    try {
      DailyElectricity.class.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      fail("Public no-args constructor is required");
      return;
    }
  }

  @Test
  public void constructor1Test() {
    final LongSummaryStatistics stat = LongStream.of(1000L, 2000L, 3000L, 4000L)
	.summaryStatistics();
    final LocalDate date = LocalDate.of(2018, Month.JULY, 5);
    final DailyElectricity de = new DailyElectricity(date, stat);

    assertThat(de.getDate(), allOf(not(nullValue()), equalTo(date)));
    assertThat(de.getAverage(), allOf(not(nullValue()), equalTo(2500D)));
    assertThat(de.getMax(), allOf(not(nullValue()), equalTo(4000L)));
    assertThat(de.getMin(), allOf(not(nullValue()), equalTo(1000L)));
    assertThat(de.getSum(), allOf(not(nullValue()), equalTo(10000L)));
  }

  @Test(expected = NullPointerException.class)
  public void constructorEception1Test() {
    new DailyElectricity(LocalDate.now(), null);
  }

  @Test(expected = NullPointerException.class)
  public void constructorEception2Test() {
    new DailyElectricity(null, new LongSummaryStatistics());
  }

  @Test
  public void toStringTest() {
    final LongSummaryStatistics stat = LongStream.of(1000L, 2000L, 3000L, 4000L)
	.summaryStatistics();
    final LocalDate date = LocalDate.of(2018, Month.JULY, 5);
    final DailyElectricity de = new DailyElectricity(date, stat);
    assertThat(de.toString(),
	equalTo("DailyElectricity [date=2018-07-05, sum=10000, average=2500.0, min=1000, max=4000]"));
  }

  @Test
  public void equalsContractTest() {
    EqualsVerifier.forClass(DailyElectricity.class)
	.suppress(Warning.NONFINAL_FIELDS)
	.usingGetClass()
	.verify();
  }
}
