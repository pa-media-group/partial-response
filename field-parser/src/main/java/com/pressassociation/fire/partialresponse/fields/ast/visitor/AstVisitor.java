package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.pressassociation.fire.partialresponse.fields.ast.*;

/**
 * Generated JavaDoc Comment.
 *
 * @author Matt Nathan
 */
public abstract class AstVisitor {
  public void visitFields(Fields fields) {
    fields.getField().apply(this);
    beforeFieldsNext(fields);
    fields.getNext().apply(this);
  }

  protected void beforeFieldsNext(Fields fields) {
    // empty
  }

  public void visitPath(Path field) {
    field.getNode().apply(this);
    beforePathField(field);
    field.getField().apply(this);
  }

  protected void beforePathField(Path field) {
    // empty
  }

  public void visitSubSelection(SubSelection node) {
    node.getName().apply(this);
    beforeSubSelectionFields(node);
    node.getFields().apply(this);
    afterSubSelectionFields(node);
  }

  protected void beforeSubSelectionFields(Node node) {
    // empty
  }

  protected void afterSubSelectionFields(Node node) {
    // empty
  }

  public void visitWildcard(Wildcard wildcard) {
    // empty on purpose
  }

  public void visitWord(Word word) {
    // empty on purpose
  }
}
