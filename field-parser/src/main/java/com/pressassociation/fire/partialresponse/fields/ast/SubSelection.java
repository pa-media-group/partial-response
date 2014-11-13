package com.pressassociation.fire.partialresponse.fields.ast;

import com.pressassociation.fire.partialresponse.fields.ast.visitor.AstVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Represents a sub selection for a named path entry.
 *
 * @author Matt Nathan
 */
public class SubSelection extends Node {
  private final Name name;
  private final AstNode fields;

  public SubSelection(Name name, AstNode fields) {
    this.name = checkNotNull(name);
    this.fields = checkNotNull(fields);
  }

  public AstNode getFields() {
    return fields;
  }

  public Name getName() {
    return name;
  }

  @Override
  public void apply(AstVisitor visitor) {
    visitor.visitSubSelection(this);
  }
}
