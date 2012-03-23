package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.google.common.collect.ImmutableList;
import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Test the ExpandingVisitor
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class ExpandingVisitorTest extends TransformingVisitorTestBase<ExpandingVisitor, AstNode> {
  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.<Object[]>builder()
                        .add(args("items", "items"))
                        .add(args("items/id", "items/id"))
                        .add(args("items,kind", "items,kind"))
                        .add(args("items,kind/id/type", "items,kind/id/type"))
                        .add(args("*", "*"))
                        .add(args("items/*", "items/*"))
                        .add(args("items/*/id", "items/*/id"))
                        .add(args("items,other/*", "items,other/*"))
                        .add(args("items(id)", "items/id"))
                        .add(args("items(*)", "items/*"))
                        .add(args("items(id/value)", "items/id/value"))
                        .add(args("items(id/*)", "items/id/*"))
                        .add(args("items(*/value)", "items/*/value"))
                        .add(args("items(id,name)", "items/id,items/name"))
                        .add(args("items(id,name/type)", "items/id,items/name/type"))
                        .add(args("items(id/*,name/value,*/label)", "items/id/*,items/name/value,items/*/label"))
                        .add(args("root/items(id,name)", "root/items/id,root/items/name"))
                        .add(args("root/items(id/*,*/title,name/value)",
                                  "root/items/id/*,root/items/*/title,root/items/name/value"))
                        .add(args("items(id,name)/type", "items/id/type,items/name/type"))
                        .add(args("items(id,name)/type(kind,value)",
                                  "items/id/type/kind,items/id/type/value,items/name/type/kind,items/name/type/value"))
                        .build();
  }

  public ExpandingVisitorTest(String source, String expected) {
    super(source, expected);
  }

  @Override
  protected ExpandingVisitor createVisitor() {
    return new ExpandingVisitor();
  }
}
