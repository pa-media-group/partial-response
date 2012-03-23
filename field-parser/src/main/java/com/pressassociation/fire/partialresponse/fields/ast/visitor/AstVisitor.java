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
    if (beforeFieldsNext(fields)) {
      fields.getNext().apply(this);
    }
  }

  protected boolean beforeFieldsNext(Fields fields) {
    return true;
  }

  public void visitPath(Path path) {
    path.getPrefix().apply(this);
    if (beforePathField(path)) {
      path.getSuffix().apply(this);
    }
  }

  protected boolean beforePathField(Path path) {
    return true;
  }

  public void visitSubSelection(SubSelection subSelection) {
    subSelection.getName().apply(this);
    if (beforeSubSelectionFields(subSelection)) {
      subSelection.getFields().apply(this);
      afterSubSelectionFields(subSelection);
    }
  }

  protected boolean beforeSubSelectionFields(SubSelection subSelection) {
    return true;
  }

  protected void afterSubSelectionFields(SubSelection subSelection) {
    // empty
  }

  public void visitWildcard(Wildcard wildcard) {
    // empty on purpose
  }

  public void visitWord(Word word) {
    // empty on purpose
  }
}
