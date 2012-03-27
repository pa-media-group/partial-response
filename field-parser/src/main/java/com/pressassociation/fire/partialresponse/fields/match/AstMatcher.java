package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Splitter;
import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import com.pressassociation.fire.partialresponse.fields.ast.visitor.MatchesPathVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matcher based of a PartialResponse Ast
 *
 * @author Matt Nathan
 */
class AstMatcher extends Matcher {
  private static final Splitter PATH_SPLITTER = Splitter.on('/').omitEmptyStrings();
  private final AstNode fields;

  AstMatcher(AstNode fields) {
    this.fields = checkNotNull(fields);
  }

  @Override
  public boolean matches(CharSequence input) {
    return new MatchesPathVisitor(PATH_SPLITTER.split(input)).applyTo(fields);
  }

  @Override
  protected String patternString() {
    return fields.toString();
  }
}
