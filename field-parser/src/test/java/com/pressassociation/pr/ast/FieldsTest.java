package com.pressassociation.pr.ast;

import com.google.common.testing.EqualsTester;

import org.junit.Test;

/**
 * Tests for {@link Fields}.
 *
 * @author Matt Nathan
 */
public class FieldsTest {

  @Test
  public void testEquality() {
    new EqualsTester()
        .addEqualityGroup(new Fields(new Word("name"), new Word("value")),
                          new Fields(new Word("name"), new Word("value")))
        .addEqualityGroup(new Fields(Wildcard.getSharedInstance(), Wildcard.getSharedInstance()),
                          new Fields(Wildcard.getSharedInstance(), Wildcard.getSharedInstance()))
        .testEquals();
  }
}
