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
