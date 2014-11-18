package com.pressassociation.pr.parser;

import com.google.common.testing.NullPointerTester;

import org.junit.Test;

/**
 * Tests for {@link Parser} for null checking.
 *
 * @author Matt Nathan
 */
public class ParserNullTest {
  @Test
  public void testNulls() throws Exception {
    NullPointerTester tester = new NullPointerTester();
    tester.testAllPublicConstructors(Parser.class);
    tester.testAllPublicStaticMethods(Parser.class);
    tester.testAllPublicInstanceMethods(new Parser());
  }
}
