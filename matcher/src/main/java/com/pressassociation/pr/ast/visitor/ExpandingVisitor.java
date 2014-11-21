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

import com.google.common.collect.Lists;

import com.pressassociation.pr.ast.*;

import java.util.Deque;

import static com.google.common.base.Preconditions.checkState;

/**
 * A visitor that will take an ast and expand out sub selection groups to their own paths.
 * <p/>
 * For example:<br/>
 * {@code items(id, name) -> items/id,items/name}<br/>
 * {@code items(id, homeTown/id)/auth -> items/id/auth,items/homeTown/id/auth}
 *
 * @author Matt Nathan
 */
public class ExpandingVisitor extends TransformingVisitor<AstNode> {

  private final Deque<AstNode> stack = Lists.newLinkedList();

  @Override
  public void visitFields(Fields fields) {
    super.visitFields(fields);
    // the last two items on the stack should be the items for this field
    AstNode top = stack.removeLast();
    AstNode bottom = stack.removeLast();
    stack.addLast(new Fields((Field) bottom, top));
  }

  @Override
  public void visitPath(Path path) {
    super.visitPath(path);
    // the last two things on the stack should be the parts of the path
    // they could be anything, for example items(id,type)/name(other,again) would resolve to
    // items/id,items/type and name/other,name/again
    AstNode top = stack.removeLast();
    AstNode bottom = stack.removeLast();
    if (bottom instanceof Fields) {
      if (top instanceof Fields) {
        // we need to cross join all the fields from the left with all the fields from the right
        // get all the fields from the top and bottom
        AstNode result = crossJoinFields((Fields) top, (Fields) bottom);
        stack.add(result);
      } else {
        AstNode left = new PathPostfixingVisitor((Field) top).applyTo(bottom);
        stack.addLast(left);
      }
    } else {
      assert bottom instanceof Node : "Bottom should be an instance of node, found: " + bottom;
      stack.addLast(new PathPrefixingVisitor((Node) bottom).applyTo(top));
    }
  }

  @Override
  public void visitSubSelection(SubSelection subSelection) {
    // first expand the sub selection
    AstNode expandedFields = new ExpandingVisitor().applyTo(subSelection.getFields());
    // then prefix the expanded fields with the sub selection name
    AstNode expandedSelection = new PathPrefixingVisitor(subSelection.getName()).applyTo(expandedFields);
    stack.addLast(expandedSelection);
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    stack.addLast(wildcard);
  }

  @Override
  public void visitWord(Word word) {
    stack.addLast(word);
  }

  /**
   * Get the result from the visitor traversing the ast. This is optional because if the visitor is never called then
   * it is possible that there is no ast to return.
   *
   * @return The result.
   */
  @Override
  public AstNode getResult() {
    checkState(!stack.isEmpty(), "Cannot get the result when the visitor hasn't been used");
    return stack.peek();
  }

  /**
   * Takes two Fields and combines all combinations of their root lists.
   * <p/>
   * For example:
   * item,id and type,kind becomes item/type,item/kind,id/type,id/kind
   *
   * @param first  The first Fields
   * @param second The second fields
   * @return The node representing the cross join of both fields
   */
  private AstNode crossJoinFields(Fields first, Fields second) {
    // first we compile the list of all top level fields (plus the end non field) for each Fields
    Iterable<Field> firstFields = new FindFieldsVisitor().applyTo(first);
    Iterable<Field> secondFields = new FindFieldsVisitor().applyTo(second);

    AstNode result = null;
    // then we join all of first with the all of second.
    for (Field secondField : secondFields) {
      for (Field firstField : firstFields) {
        if (result == null) {
          result = new PathPostfixingVisitor(firstField).applyTo(secondField);
        } else {
          Field left = (Field) new PathPostfixingVisitor(firstField).applyTo(secondField);
          result = new Fields(left, result);
        }
      }
    }
    return result;
  }
}
