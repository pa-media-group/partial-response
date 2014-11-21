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

package com.pressassociation.pr.match;

import com.pressassociation.pr.ast.AstNode;
import com.pressassociation.pr.ast.visitor.MatchesParentVisitor;
import com.pressassociation.pr.ast.visitor.MatchesPathVisitor;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matcher based of a PartialResponse Ast.
 *
 * @author Matt Nathan
 */
class AstMatcher extends Matcher {
  final AstNode fields;

  AstMatcher(AstNode fields) {
    this.fields = checkNotNull(fields);
  }

  @Override
  public boolean matches(Leaf input) {
    return new MatchesPathVisitor(input.getPath()).applyTo(fields);
  }

  @Override
  public boolean matchesParent(Leaf node) {
    return new MatchesParentVisitor(node.getPath()).applyTo(fields);
  }

  @Override
  protected String patternString() {
    return fields.toString();
  }

  @Override
  protected AstNode getAstNode() {
    return fields;
  }
}
