package com.pressassociation.fire.partialresponse.fields.ast;

import com.google.common.base.Objects;
import com.pressassociation.fire.partialresponse.fields.ast.visitor.AstVisitor;
import com.pressassociation.fire.partialresponse.fields.ast.visitor.ToStringVisitor;

import javax.annotation.Nullable;

/**
 * Generated JavaDoc Comment.
 *
 * @author Matt Nathan
 */
public abstract class AstNode {
  public abstract void apply(AstVisitor visitor);

  @Override
  public String toString() {
    return new ToStringVisitor().applyTo(this);
  }

  /**
   * Compares objects by string.
   *
   * @param other The other object
   * @return Whether the string representations of this and other are the same
   */
  @Override
  public boolean equals(@Nullable Object other) {
    if (other == this) {
      return true;
    }
    if (other == null) {
      return false;
    }
    if (!getClass().isInstance(other)) {
      return false;
    }
    String otherString = other.toString();
    return toString().equals(otherString);
  }

  /**
   * Uses the string representation to compute the hash.
   *
   * @return The hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hashCode(getClass(), toString().hashCode());
  }
}
