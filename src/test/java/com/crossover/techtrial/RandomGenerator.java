package com.crossover.techtrial;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LongSummaryStatistics;
import java.util.Random;
import java.util.UUID;

public class RandomGenerator {

  private static final Random rnd = new Random();

  public static String randomPanelSerial() {
    return UUID.randomUUID()
	.toString()
	.replace("-", "")
	.substring(0, 16);
  }

  public static BigDecimal randomLongitude() {
    return BigDecimal.valueOf(rnd.nextInt(180 * 1_000_000 * 2) - 180 * 1_000_000, 6);
  }

  public static BigDecimal randomLatitude() {
    return BigDecimal.valueOf(rnd.nextInt(90 * 1_000_000 * 2) - 90 * 1_000_000, 6);
  }

  public static Long generatedElectricity() {
    return Long.valueOf(rnd.nextInt(100_000));
  }

  private static long id = 1;

  public static Long generatedId() {
    return id++;
  }

  public static LocalTime randomLocalTime() {
    return LocalTime.ofSecondOfDay(rnd.nextInt(24 * 60 * 60 - 1));
  }

  public static LocalDate randomLocalDate() {
    return LocalDate.ofYearDay(LocalDate.now().getYear(), rnd.nextInt(365));
  }

  public static LocalDateTime randomLocalDateTime(final LocalDate date) {
    return date.atTime(randomLocalTime());
  }

  public static LocalDateTime randomLocalDateTime() {
    return randomLocalDateTime(randomLocalDate());

  }

  public static LongSummaryStatistics generatedElectricitySummaryStatistics() {
    return rnd.longs(1 + rnd.nextInt(14), 100, 100_000).summaryStatistics();
  }

}
