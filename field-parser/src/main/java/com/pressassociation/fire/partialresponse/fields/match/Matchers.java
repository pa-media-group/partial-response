package com.pressassociation.fire.partialresponse.fields.match;

import com.google.common.base.Function;
import com.pressassociation.fire.partialresponse.fields.ast.AstNode;
import com.pressassociation.fire.partialresponse.fields.ast.Word;
import com.pressassociation.fire.partialresponse.fields.ast.visitor.CopyVisitor;
import com.pressassociation.fire.partialresponse.fields.parser.Parser;

/**
 * Utilities relating to Matcher and associated classes.
 *
 * @author Matt Nathan
 */
public class Matchers {
  /**
   * Return whether this matcher (the the best of our knowledge) should match all paths. This can be used to optimise
   * certain code paths that would do extra work to support matchers when it would not be needed.
   *
   * @param matcher The matcher to check
   * @return {@code true} if the matcher should match all paths.
   */
  public static boolean matchesAll(Matcher matcher) {
    return matcher instanceof AllMatcher ||
           matcher.patternString().equals("*");
  }

  /**
   * Transform the words in the given matcher according to the nameTransformer given. This only works for the matchers
   * created by the Matcher classes static methods. Custom matchers are not supported (unless they provide a decent
   * patternString implementation that can be parsed by a {@link Parser})
   *
   * @param matcher         The matcher to transform
   * @param nameTransformer The function used to transform the matchers words
   * @return The new matcher
   */
  public static Matcher transform(Matcher matcher, final Function<? super String, String> nameTransformer) {
    if (matchesAll(matcher)) {
      return matcher;
    }
    AstNode ast;
    if (matcher instanceof AstMatcher) {
      ast = ((AstMatcher) matcher).fields;
    } else {
      ast = new Parser().parse(matcher.patternString());
    }

    AstNode transformed = new CopyVisitor() {
      @Override
      protected Word createWordCopy(String stringValue) {
        return super.createWordCopy(nameTransformer.apply(stringValue));
      }
    }.applyTo(ast);
    return new AstMatcher(transformed);
  }

  private Matchers() {
  }
}
