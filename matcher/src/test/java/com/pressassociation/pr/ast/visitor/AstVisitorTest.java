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
