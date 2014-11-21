package com.pressassociation.pr.ast.visitor;

import com.pressassociation.pr.ast.*;

/**
 * A visitor over an AST tree. While this class is abstract it doesn't require any methods to be implemented. The tree
 * is traversed automatically by default, sub-classes can override this behaviour if they need to.
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

  /**
   * Called before the right hand side of a Fields node is visited. Return false to stop processing of that node.
   */
  protected boolean beforeFieldsNext(Fields fields) {
    return true;
  }

  public void visitPath(Path path) {
    path.getPrefix().apply(this);
    if (beforePathField(path)) {
      path.getSuffix().apply(this);
    }
  }

  /**
   * Called before the suffix part of a Path node is visited. Return false to stop processing.
   */
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

  /**
   * Called before the fields part of a SubSelection is visited. Return false to stop processing.
   */
  protected boolean beforeSubSelectionFields(SubSelection subSelection) {
    return true;
  }

  /**
   * Called after the fields part of a SubSelection is visited.
   */
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
