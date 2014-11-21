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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import com.pressassociation.pr.ast.*;

import java.util.Deque;

import static com.google.common.collect.Iterables.getOnlyElement;

/**
 * Effectively removes a path prefix from the given AST.
 *
 * @author Matt Nathan
 */
public class NarrowScopeVisitor extends TransformingVisitor<Optional<AstNode>> {

  private final ImmutableList<String> path;
  private final Deque<AstNode> output = Lists.newLinkedList();
  private int pathIndex = 0;
  private int expectedNames = 1; // we always expect at least one Name
  private int treeDepth = 0;
  private boolean wasWildcard = false;

  public NarrowScopeVisitor(Iterable<String> path) {
    this.path = ImmutableList.copyOf(path);
  }

  @Override
  protected boolean beforePathField(Path path) {
    pathIndex++;
    treeDepth++;
    return true;
  }

  @Override
  public void visitPath(Path path) {
    expectedNames++; // paths have at least one more Name than without the Path
    int originalCount = output.size();
    super.visitPath(path);
    treeDepth--;
    int added = output.size() - originalCount;
    switch (added) {
      case 0:
        // nothing added so the entire branch shouldn't be in the output
        break;
      case 1:
        // single element added, leave it to represent us in the output
        break;
      case 2:
        // two items added, we need to replace them with a Path element
        // BUT only if we aren't the root or are expecting more nodes, otherwise drop it on the floor
        Field right = (Field) output.removeLast();
        Node left = (Node) output.removeLast();
        if (left instanceof Wildcard) {
          output.add(new Fields(right, new Path(left, right)));
        } else {
          if (expectedNames > 0 || treeDepth > 0) {
            output.addLast(new Path(left, right));
          }
        }
        break;
      default:
        throw new IllegalStateException("Unexpected number of items added to the stack:"
                                        + " originalCount:" + originalCount
                                        + " stack:" + output);
    }
  }

  @Override
  public void visitWord(Word word) {
    expectedNames--;
    if (pathIndex >= path.size()) {
      output.add(word);
    } else if (wasWildcard) {
      String pathPart = path.get(pathIndex);
      boolean matchesWord = pathPart.equals(word.getStringValue());
      if (!matchesWord) {
        wasWildcard = false;
        output.add(word);
      } else {
        pathIndex--;
      }
    } else {
      String pathPart = path.get(pathIndex);
      boolean matchesWord = pathPart.equals(word.getStringValue());
      if (matchesWord && expectedNames == 0) {
        // this is the last word that matches
        output.add(Wildcard.getSharedInstance());
      } else if (!matchesWord && expectedNames > 0) {
        output.add(word);
      }
    }
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    wasWildcard = true;
    expectedNames--;
//    pathIndex--;
//    if (expectedNames == 0) {
      output.add(wildcard);
//    } else {
      // the wildcard is not at the end of a path, because of this we need to re-work the tree a little to add an extra
      // field: */a => a,*/a
//    }
  }

  @Override
  public Optional<AstNode> getResult() {
    if (output.isEmpty()) {
      return Optional.absent();
    } else {
      return Optional.of(getOnlyElement(output));
    }
  }
}
