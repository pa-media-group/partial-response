package com.pressassociation.pr.ast.visitor;

import com.pressassociation.pr.ast.*;

/**
 * Generates a string representation of the visited AST. While the output string may not be equal to the pattern that
 * was used to generate the AST it will be functionally equivalent.
 *
 * @author Matt Nathan
 */
public class ToStringVisitor extends TransformingVisitor<String> {
  @SuppressWarnings("StringBufferField")
  private final StringBuilder buffer = new StringBuilder();

  @Override
  protected void afterSubSelectionFields(SubSelection subSelection) {
    buffer.append(')');
  }

  @Override
  protected boolean beforePathField(Path path) {
    buffer.append('/');
    return true;
  }

  @Override
  protected boolean beforeFieldsNext(Fields fields) {
    buffer.append(',');
    return true;
  }

  @Override
  protected boolean beforeSubSelectionFields(SubSelection subSelection) {
    buffer.append('(');
    return true;
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    buffer.append('*');
  }

  @Override
  public void visitWord(Word word) {
    buffer.append(word.getStringValue());
  }

  @Override
  public String getResult() {
    return buffer.toString();
  }
}
