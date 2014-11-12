package com.pressassociation.fire.partialresponse.fields.ast;

import com.pressassociation.fire.partialresponse.fields.ast.visitor.AstVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a path segment composed of nodes.
 *
 * @author Matt Nathan
 */
public class Path extends Field {
  private final Node prefix;
  private final Field suffix;

  public Path(Node prefix, Field suffix) {
    this.prefix = checkNotNull(prefix);
    this.suffix = checkNotNull(suffix);
  }

  public Node getPrefix() {
    return prefix;
  }

  public Field getSuffix() {
    return suffix;
  }

  @Override
  public void apply(AstVisitor visitor) {
    visitor.visitPath(this);
  }
}
