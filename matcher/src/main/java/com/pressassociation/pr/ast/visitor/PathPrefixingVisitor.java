package com.pressassociation.pr.ast.visitor;

import com.pressassociation.pr.ast.*;

import static com.google.common.base.Preconditions.checkState;

/**
 * Prepends a prefix to the visited ast.
 * <p/>
 * For example:
 * {@code items/id} with prefix {@code nested} becomes {@code nested/items/id}
 *
 * @author Matt Nathan
 */
public class PathPrefixingVisitor extends TransformingVisitor<AstNode> {

  private final Node prefix;
  private AstNode result;

  public PathPrefixingVisitor(Node prefix) {
    this.prefix = prefix;
  }

  @Override
  public void visitFields(Fields fields) {
    if (result == null) {
      result = new Fields(new Path(prefix, fields.getField()),
                          new PathPrefixingVisitor(prefix).applyTo(fields.getNext()));
    } else {
      super.visitFields(fields);
    }
  }

  @Override
  public void visitPath(Path path) {
    if (result == null) {
      result = new Path(prefix, path);
    } else {
      super.visitPath(path);
    }
  }

  @Override
  public void visitSubSelection(SubSelection subSelection) {
    if (result == null) {
      result = new Path(prefix, subSelection);
    } else {
      super.visitSubSelection(subSelection);
    }
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    if (result == null) {
      result = new Path(prefix, wildcard);
    } else {
      super.visitWildcard(wildcard);
    }
  }

  @Override
  public void visitWord(Word word) {
    if (result == null) {
      result = new Path(prefix, word);
    } else {
      super.visitWord(word);
    }
  }

  @Override
  public AstNode getResult() {
    checkState(result != null, "Cannot get the result if this visitor hasn't been used.");
    return result;
  }
}
