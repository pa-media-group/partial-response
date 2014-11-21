package com.pressassociation.pr.ast.visitor;

import com.google.common.collect.ImmutableList;

import com.pressassociation.pr.ast.*;

import java.util.ArrayDeque;
import java.util.Deque;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Visitor that will return a boolean to signify whether the path given in the constructor matches the Ast we visit.
 *
 * @author Matt Nathan
 */
public class MatchesPathVisitor extends TransformingVisitor<Boolean> {

  private boolean wasWildcard = false;
  private final ImmutableList<String> pathParts;

  private final Deque<Integer> pathIndexStack = new ArrayDeque<Integer>(ImmutableList.of(0));
  private final Deque<Boolean> matchesStack = new ArrayDeque<Boolean>(ImmutableList.of(false));

  public MatchesPathVisitor(Iterable<String> pathParts) {
    this.pathParts = ImmutableList.copyOf(checkNotNull(pathParts));
  }

  @Override
  protected boolean beforeFieldsNext(Fields fields) {
    if (matches()) {
      // no need to continue if we have found something that matches
      return false;
    } else {
      // if we still don't match then reset the path index the the one from the parent stack.
      pathIndexStack.removeLast();
      pathIndexStack.add(firstNonNull(pathIndexStack.peekLast(), 0));
      return true;
    }
  }

  @Override
  protected boolean beforePathField(Path path) {
    return matches();
  }

  @Override
  protected boolean beforeSubSelectionFields(SubSelection subSelection) {
    boolean matches = matches();
    if (matches) {
      pathIndexStack.addLast(pathIndexStack.peekLast());
      matchesStack.addLast(matches);
      return true;
    } else {
      return false;
    }
  }

  @Override
  protected void afterSubSelectionFields(SubSelection subSelection) {
    int index = pathIndexStack.removeLast();
    pathIndexStack.removeLast();
    pathIndexStack.addLast(index);
    setMatches(matchesStack.removeLast());
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    // wildcard matches anything
    setMatches(true);
    wasWildcard = true;
    getAndIncrementPathIndex();
  }

  @Override
  public void visitWord(Word word) {
    String wordString = word.getStringValue();
    int index = getAndIncrementPathIndex();
    if (wasWildcard) {
      // if the last leaf was a wildcard then we search the rest of the path to find a word matching this word
      boolean found = false;
      while (index < pathParts.size()) {
        if (wordString.equals(pathParts.get(index))) {
          found = true;
          break;
        }
        index = getAndIncrementPathIndex();
      }
      setMatches(found);
    } else {
      // if there is another part in the path to match and we match the next part
      setMatches(index < pathParts.size() && pathParts.get(index).equals(wordString));
    }
  }

  @Override
  public Boolean getResult() {
    return matches();
  }

  private int getAndIncrementPathIndex() {
    int index = pathIndexStack.removeLast();
    pathIndexStack.addLast(index + 1);
    return index;
  }

  private boolean matches() {
    return matchesStack.peekLast();
  }

  private void setMatches(boolean matches) {
    matchesStack.removeLast();
    matchesStack.addLast(matches);
  }
}
