package com.pressassociation.pr.ast.visitor;

import com.google.common.collect.Lists;

import com.pressassociation.pr.ast.*;

import java.util.Deque;

import static com.google.common.base.Preconditions.checkState;

/**
 * Visitor that copies (deeply) the nodes of an ast.
 *
 * @author Matt Nathan
 */
public class CopyVisitor extends TransformingVisitor<AstNode> {

  private final Deque<AstNode> stack = Lists.newLinkedList();

  @Override
  public void visitFields(Fields fields) {
    super.visitFields(fields);
    AstNode next = stack.removeLast();
    Field field = (Field) stack.removeLast();
    stack.addLast(createFieldsCopy(field, next));
  }

  @Override
  public void visitPath(Path path) {
    super.visitPath(path);
    Field suffix = (Field) stack.removeLast();
    Node prefix = (Node) stack.removeLast();
    stack.addLast(createPathCopy(prefix, suffix));
  }

  @Override
  public void visitSubSelection(SubSelection subSelection) {
    super.visitSubSelection(subSelection);
    AstNode fields = stack.removeLast();
    Name name = (Name) stack.removeLast();
    stack.addLast(createSubSelectionCopy(name, fields));
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    stack.addLast(wildcard);
  }

  @Override
  public void visitWord(Word word) {
    stack.addLast(createWordCopy(word.getStringValue()));
  }

  @Override
  public AstNode getResult() {
    checkState(!stack.isEmpty(), "Cannot get the result when the visitor hasn't been used yet");
    return stack.getLast();
  }

  protected Fields createFieldsCopy(Field field, AstNode next) {
    return new Fields(field, next);
  }

  protected Path createPathCopy(Node prefix, Field suffix) {
    return new Path(prefix, suffix);
  }

  protected SubSelection createSubSelectionCopy(Name name, AstNode fields) {
    return new SubSelection(name, fields);
  }

  protected Word createWordCopy(String stringValue) {
    return new Word(stringValue);
  }
}
