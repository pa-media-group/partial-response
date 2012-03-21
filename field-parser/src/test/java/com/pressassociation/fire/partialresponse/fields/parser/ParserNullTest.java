package com.pressassociation.fire.partialresponse.fields.parser;

import com.google.common.testing.NullPointerTester;
import org.junit.Test;

/**
 * Generated JavaDoc Comment.
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
