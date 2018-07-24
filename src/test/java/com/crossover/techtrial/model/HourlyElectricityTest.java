package com.crossover.techtrial.model;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class HourlyElectricityTest {

  @Test
  public void publicNoargsConstructorTest() {
    try {
      HourlyElectricity.class.getConstructor();
    } catch (NoSuchMethodException e) {
      fail("Public no-args constructor is required");
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
