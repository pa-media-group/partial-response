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

package com.pressassociation.pr.match;

import com.google.common.base.Optional;

import com.pressassociation.pr.ast.AstNode;
import com.pressassociation.pr.ast.Wildcard;
import com.pressassociation.pr.ast.visitor.MatchesParentVisitor;
import com.pressassociation.pr.ast.visitor.MatchesPathVisitor;
import com.pressassociation.pr.ast.visitor.NarrowScopeVisitor;

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

  @Override
  public Matcher rebase(Leaf path) {
    Optional<AstNode> astNode = new NarrowScopeVisitor(path.getPath()).applyTo(fields);
    if (astNode.isPresent()) {
      if (Wildcard.getSharedInstance().equals(astNode.get())) {
        return AllMatcher.INSTANCE;
      } else {
        return new AstMatcher(astNode.get());
      }
    } else {
      // missing result means it doesn't match anything
      return NoneMatcher.INSTANCE;
    }
  }
}
