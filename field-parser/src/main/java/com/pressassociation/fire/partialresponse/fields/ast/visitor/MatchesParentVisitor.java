package com.pressassociation.fire.partialresponse.fields.ast.visitor;

import com.google.common.collect.ImmutableList;

import com.pressassociation.fire.partialresponse.fields.ast.*;

import java.util.Deque;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Queues.newArrayDeque;

/**
 * Visitor that will return a boolean to signify whether the path given in the constructor matches the Ast we visit.
 *
 * @author Matt Nathan
 */
public class MatchesParentVisitor extends TransformingVisitor<Boolean> {

  private final ImmutableList<String> pathParts;

  private final Deque<Integer> pathIndexStack = newArrayDeque(ImmutableList.of(0));
  private final Deque<Boolean> matchStack = newArrayDeque(ImmutableList.of(true));
  private boolean matched = false;

  public MatchesParentVisitor(Iterable<String> pathParts) {
    this.pathParts = ImmutableList.copyOf(checkNotNull(pathParts));
  }

  @Override
  public void visitFields(Fields fields) {
    pushPathIndex();
    super.visitFields(fields);
  }

  @Override
  protected boolean beforeFieldsNext(Fields fields) {
    if (matched || fieldStillMatches()) {
      // no point continuing if we've matched one of the fields already
      // if the field no longer matches then we try the other branch
      return false;
    } else {
      resetPathIndex();
      resetFieldMatchStatus();
      return true;
    }
  }

  @Override
  protected boolean beforePathField(Path path) {
    // no point continuing if we've already matched or if we know we don't match any more
    return !matched && fieldStillMatches();
  }

  @Override
  protected boolean beforeSubSelectionFields(SubSelection subSelection) {
    if (matched || !fieldStillMatches()) {
      // no point continuing if we've already matched or if we know we don't match any more
      return false;
    } else {
      matchStack.addLast(true);
      return true;
    }
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    // in the case of a wildcard we know that the remainder of the path must match the pattern
    matched = true;
  }

  @Override
  public void visitWord(Word word) {
    String wordString = word.getStringValue();
    int index = getAndIncrementPathIndex();
    String pathPart = pathParts.get(index);
    if (wordString.equals(pathPart)) {
      if (pathExhausted()) {
        // we know the entire path matches so short-cut any further checks
        matched = true;
      }
    } else {
      fieldNoLongerMatches();
    }
  }

  @Override
  public Boolean getResult() {
    return matched || pathFullyMatched();
  }

  private boolean pathFullyMatched() {
    return fieldStillMatches() && pathExhausted();
  }

  private boolean pathExhausted() {
    return getPathIndex() == pathParts.size();
  }

  private int getAndIncrementPathIndex() {
    int index = pathIndexStack.removeLast();
    pathIndexStack.addLast(index + 1);
    return index;
  }

  private int getPathIndex() {
    return pathIndexStack.peekLast();
  }

  private void pushPathIndex() {
    if (pathIndexStack.isEmpty()) {
      pathIndexStack.addLast(0);
    } else {
      pathIndexStack.addLast(pathIndexStack.peekLast());
    }
  }

  private void resetPathIndex() {
    pathIndexStack.removeLast();
    checkState(!pathIndexStack.isEmpty());
  }

  private boolean fieldStillMatches() {
    return matchStack.getLast();
  }

  private void fieldNoLongerMatches() {
    matchStack.removeLast();
    matchStack.addLast(false);
  }

  private void resetFieldMatchStatus() {
    matchStack.removeLast();
    matchStack.addLast(true);
  }
}
