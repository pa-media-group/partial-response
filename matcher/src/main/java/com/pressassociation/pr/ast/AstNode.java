/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Press Association Limited
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.pressassociation.pr.ast;

import com.google.common.base.Objects;

import com.pressassociation.pr.ast.visitor.AstVisitor;
import com.pressassociation.pr.ast.visitor.ToStringVisitor;

import javax.annotation.Nullable;

/**
 * Represents a node in a partial response AST. All other nodes types extend from this type.
 *
 * @author Matt Nathan
 */
public abstract class AstNode {
  /**
   * Apply the given visitor over the tree represented by this node.
   */
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
