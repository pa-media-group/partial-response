package com.pressassociation.pr.match;

import com.pressassociation.pr.ast.AstNode;
import com.pressassociation.pr.ast.visitor.MatchesParentVisitor;
import com.pressassociation.pr.ast.visitor.MatchesPathVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matcher based of a PartialResponse Ast.
 *
 * @author Matt Nathan
 */
class AstMatcher extends Matcher {
  final AstNode fields;

  AstMatcher(AstNode fields) {
    this.fields = checkNotNull(fields);
  }

  @Override
  public boolean matches(Leaf input) {
    return new MatchesPathVisitor(input.getPath()).applyTo(fields);
  }

  @Override
  public boolean matchesParent(Leaf node) {
    return new MatchesParentVisitor(node.getPath()).applyTo(fields);
  }

  @Override
  protected String patternString() {
    return fields.toString();
  }

  @Override
  protected AstNode getAstNode() {
    return fields;
  }
}
