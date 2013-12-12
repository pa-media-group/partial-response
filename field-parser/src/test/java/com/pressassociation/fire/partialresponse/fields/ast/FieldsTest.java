package com.pressassociation.fire.partialresponse.fields.ast;

import com.google.common.testing.EqualsTester;

import org.junit.Test;

/**
 * Generated JavaDoc Comment.
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
