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
