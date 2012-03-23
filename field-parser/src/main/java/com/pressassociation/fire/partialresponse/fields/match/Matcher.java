package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import com.pressassociation.fire.partialresponse.fields.ast.visitor.MatchesPathVisitor;
import com.pressassociation.fire.partialresponse.fields.parser.Parser;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A Matcher that will match against some AstNode a CharSequence.
 *
 * @author Matt Nathan
 */
public class Matcher implements Predicate<CharSequence> {

  /**
   * Get a new Matcher that will match against the given fields string. The string should be a valid fields string,
   * something like {@code items/name(type,value)}.
   *
   * @param fields The fields string
   * @return The matcher.
   */
  public static Matcher valueOf(CharSequence fields) {
    return new Matcher(new Parser().parse(checkNotNull(fields)));
  }

  private static final Splitter PATH_SPLITTER = Splitter.on('/').omitEmptyStrings();
  private final AstNode fields;

  public Matcher(AstNode fields) {
    this.fields = checkNotNull(fields);
  }

  public boolean matches(CharSequence input) {
    return new MatchesPathVisitor(PATH_SPLITTER.split(input)).applyTo(fields);
  }

  @Override
  public boolean apply(@Nullable CharSequence input) {
    return input != null && matches(input);
  }
}
