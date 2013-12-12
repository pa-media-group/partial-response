package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.google.common.base.Preconditions;

import com.pressassociation.fire.partialresponse.fields.ast.*;
import com.pressassociation.fire.partialresponse.fields.parser.Parser;

/**
 * Postfix some node to the visited nodes.
 * <p/>
 * For example:<br/>
 * items/id,items/name with a postfix value of type will become items/id/type,items/name/type<br/>
 * items(id,name) with a postfix of type will become items(id,name)/type
 *
 * @author Matt Nathan
 */
public class PathPostfixingVisitor extends TransformingVisitor<AstNode> {

  private final Field postfix;
  private AstNode result;

  public PathPostfixingVisitor(Field postfix) {
    this.postfix = postfix;
  }

  @Override
  public void visitFields(Fields fields) {
    Field field = (Field) appendPostfix(fields.getField());
    AstNode next = new PathPostfixingVisitor(postfix).applyTo(fields.getNext());
    result = new Fields(field, next);
  }

  @Override
  public void visitPath(Path path) {
    result = appendPostfix(path);
  }

  @Override
  public void visitSubSelection(SubSelection subSelection) {
    result = appendPostfix(subSelection);
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    result = appendPostfix(wildcard);
  }

  @Override
  public void visitWord(Word word) {
    result = appendPostfix(word);
  }

  @Override
  public AstNode getResult() {
    Preconditions.checkState(result != null, "Cannot get the result without first using the visitor");
    return result;
  }

  private Path appendPostfix(Node wildcard) {
    return new Path(wildcard, postfix);
  }

  private AstNode appendPostfix(AstNode node) {
    // todo: replace parser usage with ast creation
    return new Parser().parse(node + "/" + postfix);
  }
}
