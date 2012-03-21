package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.pressassociation.fire.partialresponse.fields.ast.*;

/**
 * Generated JavaDoc Comment.
 *
 * @author Matt Nathan
 */
public class ToStringVisitor extends AstVisitor {
  @SuppressWarnings("StringBufferField")
  private final StringBuilder buffer = new StringBuilder();

  @Override
  protected void afterSubSelectionFields(Node node) {
    buffer.append(')');
  }

  @Override
  protected void beforePathField(Path field) {
    buffer.append('/');
  }

  @Override
  protected void beforeFieldsNext(Fields fields) {
    buffer.append(',');
  }

  @Override
  protected void beforeSubSelectionFields(Node node) {
    buffer.append('(');
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
  public String toString() {
    return buffer.toString();
  }
}
