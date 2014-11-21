/*
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
 */

package com.pressassociation.pr.ast.visitor;

import com.pressassociation.pr.ast.AstNode;

/**
 * A visitor that produces some kind of output from the visitation of the ast.
 *
 * @param <T> The result type
 * @author Matt Nathan
 */
public abstract class TransformingVisitor<T> extends AstVisitor {
  /**
   * Apply this visitor to the given AST returning the result.
   */
  public T applyTo(AstNode node) {
    node.apply(this);
    return getResult();
  }

  /**
   * Return the result of visiting the AST.
   */
  public abstract T getResult();

  @Override
  public String toString() {
    return getResult().toString();
  }
}
