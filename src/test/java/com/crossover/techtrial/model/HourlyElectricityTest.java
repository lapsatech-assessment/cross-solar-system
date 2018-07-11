package com.crossover.techtrial.model;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class HourlyElectricityTest {

  @Test
  public void publicNoargsConstructorTest() {
    try {
      HourlyElectricity.class.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      fail("Public no-args constructor is required");
      return;
    }
  }

  @Test
  public void equalsContractTest() {
    EqualsVerifier.forClass(HourlyElectricity.class)
	.usingGetClass()
	.withIgnoredFields("panel", "generatedElectricity")
	.verify();
  }
}
