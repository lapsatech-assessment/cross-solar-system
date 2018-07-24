package com.crossover.techtrial.model;

import static org.junit.Assert.*;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

public class PanelTest {

  @Test
  public void publicNoargsConstructorTest() {
    try {
      Panel.class.getConstructor();
    } catch (NoSuchMethodException e) {
      fail("Public no-args constructor is required");
    }
  }

  @Test
  public void equalsContractTest() {
    EqualsVerifier.forClass(Panel.class)
	.usingGetClass()
	.withIgnoredFields("longitude", "latitude")
	.verify();
  }
}
