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
import com.pressassociation.pr.parser.Parser;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link AstVisitor}.
 *
 * @author Matt Nathan
 */
public class AstVisitorTest {
  private Parser parser;

  @Before
  public void setUp() {
    parser = new Parser();
  }

  @Test
  public void testMethodCalled() {

    AstVisitor visitor;
    visitor = visitParsedAst("item");
    verify(visitor).visitWord(new Word("item"));

    visitor = visitParsedAst("item,guid");
    verify(visitor).visitWord(new Word("item"));
    verify(visitor).visitWord(new Word("guid"));
    verify(visitor).visitFields(new Fields(new Word("item"), new Word("guid")));
    verify(visitor).beforeFieldsNext(new Fields(new Word("item"), new Word("guid")));

    visitor = visitParsedAst("item(id,name)");
    verify(visitor).visitWord(new Word("item"));
    verify(visitor).visitWord(new Word("id"));
    verify(visitor).visitWord(new Word("name"));
    verify(visitor).visitSubSelection(new SubSelection(new Word("item"), new Fields(new Word("id"), new Word("name"))));
    verify(visitor).beforeSubSelectionFields(
        new SubSelection(new Word("item"), new Fields(new Word("id"), new Word("name"))));
    verify(visitor).afterSubSelectionFields(
        new SubSelection(new Word("item"), new Fields(new Word("id"), new Word("name"))));
    verify(visitor).visitFields(new Fields(new Word("id"), new Word("name")));
    verify(visitor).beforeFieldsNext(new Fields(new Word("id"), new Word("name")));

    visitor = visitParsedAst("*");
    verify(visitor).visitWildcard(Wildcard.getSharedInstance());

    visitor = visitParsedAst("items/*");
    verify(visitor).visitWildcard(Wildcard.getSharedInstance());
    verify(visitor).visitWord(new Word("items"));
    verify(visitor).visitPath(new Path(new Word("items"), Wildcard.getSharedInstance()));
    verify(visitor).beforePathField(new Path(new Word("items"), Wildcard.getSharedInstance()));
  }

  private AstVisitor visitParsedAst(CharSequence fields) {
    AstNode item;
    AstVisitor visitor;
    item = parser.parse(fields);
    visitor = spy(new TestVisitor());
    item.apply(visitor);
    return visitor;
  }

  @SuppressWarnings("EmptyClass")
  private static class TestVisitor extends AstVisitor {
  }
}
