package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;

import com.pressassociation.fire.partialresponse.fields.parser.Parser;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Matches leaf nodes against a pattern. This class represents the core abstraction of partial responses. A Matcher is
 * built around a pattern, once built you can query whether certain leaf nodes (or paths) are included in the pattern.
 *
 * @author Matt Nathan
 */
public abstract class Matcher implements Predicate<Leaf> {

  /**
   * Return a matcher that will match all paths. Equivalent to {@code Matchers.of("*")}.
   *
   * @return The matcher.
   */
  public static Matcher all() {
    return AllMatcher.INSTANCE;
  }

  /**
   * Get a new Matcher that will match against the given fields string. The string should be a valid fields string,
   * something like {@code items/name(type,value)}.
   *
   * @param fields The fields string
   * @return The matcher.
   */
  public static Matcher of(CharSequence fields) {
    // the default case is worth shortcutting
    if ("*".equals(fields)) {
      return all();
    }
    return new AstMatcher(new Parser().parse(checkNotNull(fields)));
  }

  /**
   * Return whether this matcher pattern applies to the given leaf node.
   */
  public abstract boolean matches(Leaf leaf);

  /**
   * @deprecated This method exists solely to satisfy the Predicate contract, use {@link #matches(Leaf)} instead.
   */
  @Override
  @Deprecated
  public boolean apply(@Nullable Leaf input) {
    return input != null && matches(input);
  }

  @Override
  public String toString() {
    if (Matchers.matchesAll(this)) {
      return "Matcher.all()";
    }
    return "Matcher.of(" + patternString() + ')';
  }

  /**
   * Get the pattern this Matcher represents. Used for toString().
   */
  protected abstract String patternString();

  @Override
  public boolean equals(@Nullable Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Matcher)) {
      return false;
    }
    Matcher other = (Matcher) obj;
    return other.patternString().equals(patternString());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(Matcher.class, patternString());
  }
}
