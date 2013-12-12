package com.pressassociation.fire.partialresponse.fields.ast;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Checks all the constructors, methods and static methods of every concrete ast node implementation
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class AstNullPointerTest {
  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.of(
        new Object[]{Fields.class, new Fields(Wildcard.getSharedInstance(), Wildcard.getSharedInstance())},
        new Object[]{Path.class, new Path(Wildcard.getSharedInstance(), Wildcard.getSharedInstance())},
        new Object[]{
            SubSelection.class, new SubSelection(Wildcard.getSharedInstance(), Wildcard.getSharedInstance())
        },
        new Object[]{Wildcard.class, Wildcard.getSharedInstance()},
        new Object[]{Word.class, new Word("word")});
  }

  private NullPointerTester tester;
  private final Class<?> type;
  private final Object instance;

  public AstNullPointerTest(Class<?> type, Object instance) {
    this.type = type;
    this.instance = instance;
  }

  @Before
  public void setUp() {
    tester = new NullPointerTester();
    tester.setDefault(AstNode.class, Wildcard.getSharedInstance());
    tester.setDefault(Field.class, Wildcard.getSharedInstance());
    tester.setDefault(Name.class, Wildcard.getSharedInstance());
    tester.setDefault(Node.class, Wildcard.getSharedInstance());
  }

  @Test
  public void testNulls() throws Exception {
    tester.testAllPublicConstructors(type);
    tester.testAllPublicStaticMethods(type);
    tester.testAllPublicInstanceMethods(instance);
  }
}
