package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import com.pressassociation.fire.partialresponse.fields.parser.Parser;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Matcher that will match against some AstNode a CharSequence.
 *
 * @author Matt Nathan
 */
public abstract class Matcher implements Predicate<CharSequence> {

  /**
   * Holder of lazy singletons
   */
  @SuppressWarnings("UtilityClassWithoutPrivateConstructor")
  private static final class Holder {
    private static final Matcher ALL_INSTANCE = new AllMatcher();
  }

  /**
   * Return a matcher that will match all paths.
   *
   * @return The matcher.
   */
  public static Matcher all() {
    return Holder.ALL_INSTANCE;
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

  public abstract boolean matches(CharSequence path);

  @Override
  public boolean apply(@Nullable CharSequence input) {
    return input != null && matches(input);
  }

  @Override
  public String toString() {
    return "Matcher[" + patternString() + ']';
  }

  protected abstract String patternString();

  @Override
  public boolean equals(Object obj) {
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
