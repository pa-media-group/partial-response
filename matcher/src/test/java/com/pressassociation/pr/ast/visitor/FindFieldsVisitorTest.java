package com.pressassociation.pr.ast.visitor;

import com.google.common.collect.ImmutableList;

import com.pressassociation.pr.ast.Field;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Tests for {@link FindFieldsVisitor}.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class FindFieldsVisitorTest extends TransformingVisitorTestBase<FindFieldsVisitor, Iterable<Field>> {
  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.<Object[]>builder()
                        .add(args("*", "[*]"))
                        .add(args("item", "[item]"))
                        .add(args("item,items", "[items, item]"))
                        .add(args("item1,item2,item3", "[item3, item2, item1]"))
                        .add(args("item(id)", "[item(id)]"))
                        .add(args("item(id,href)", "[item(id,href)]"))
                        .add(args("item/id", "[item/id]"))
                        .build();
  }

  public FindFieldsVisitorTest(String source, String expected) {
    super(source, expected);
  }

  @Override
  protected FindFieldsVisitor createVisitor() {
    return new FindFieldsVisitor();
  }
}
