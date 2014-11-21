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

import com.google.common.collect.ImmutableList;

import com.pressassociation.pr.ast.AstNode;
import com.pressassociation.pr.ast.Field;
import com.pressassociation.pr.ast.Word;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Tests for {@link PathPostfixingVisitor}.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class PathPostfixingVisitorTest extends TransformingVisitorTestBase<PathPostfixingVisitor, AstNode> {

  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.<Object[]>builder()
                        .add(args(new Word("after"), "*", "*/after"))
                        .add(args(new Word("after"), "items", "items/after"))
                        .add(args(new Word("after"), "items,other", "items/after,other/after"))
                        .add(args(new Word("after"), "items/id", "items/id/after"))
                        .add(args(new Word("after"), "items/id,other/*", "items/id/after,other/*/after"))
                        .add(args(new Word("after"), "items(id)", "items(id)/after"))
                        .build();
  }

  private static Object[] args(Field postfix, String source, String expected) {
    return genArgs(postfix, source, expected);
  }

  private final Field postfix;

  public PathPostfixingVisitorTest(Field postfix, String source, String expected) {
    super(source, expected);
    this.postfix = postfix;
  }

  @Override
  protected PathPostfixingVisitor createVisitor() {
    return new PathPostfixingVisitor(postfix);
  }
}
