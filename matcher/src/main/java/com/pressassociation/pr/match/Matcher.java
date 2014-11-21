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

package com.pressassociation.pr.match;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.base.Predicate;

import com.pressassociation.pr.ast.AstNode;
import com.pressassociation.pr.ast.Word;
import com.pressassociation.pr.ast.visitor.CopyVisitor;
import com.pressassociation.pr.parser.Parser;

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
   * Return a Matcher that will <em>not</em> match any path. Equivalent to {@code Matchers.of("")}.
   */
  public static Matcher none() {
    return NoneMatcher.INSTANCE;
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
    if (fields.toString().isEmpty()) {
      return none();
    }
    return new AstMatcher(new Parser().parse(checkNotNull(fields)));
  }

  // package-private to stop any custom implementations.
  Matcher() {
  }

  /**
   * Return whether this matcher should match all paths. This can be used to optimise certain code paths that would do
   * extra work to support matchers when it would not be needed.
   *
   * @return {@code true} if the matcher should match all paths.
   */
  public boolean matchesAll() {
    return "*".equals(patternString());
  }

  /**
   * Return whether this matcher pattern exactly matches the given leaf. This will not return true if the leaf
   * represents a node that not explicitly matched. For example a pattern of {@code my/property} will not match the
   * leaf value {@code my}.
   *
   * @see #matchesParent(Leaf)
   */
  public abstract boolean matches(Leaf leaf);

  /**
   * Return whether this matcher pattern applies to the given path. The path will be split according to
   * {@link Leaf#fromPath(CharSequence)}.
   *
   * @see #matches(Leaf)
   */
  public boolean matches(CharSequence path) {
    return matches(Leaf.fromPath(path));
  }

  /**
   * Returns whether any of the leafs that this instance {@link #matches(Leaf) matches} start with the given node. For
   * example, given the pattern {@code my/property} this method will return true for both {@code my} and
   * {@code my/property}. This method is also aware of wildcards in the pattern such that a pattern of
   * {@code my/*&#47;property} will match any node beginning with {@code my}
   */
  public abstract boolean matchesParent(Leaf node);

  /**
   * Returns whether any of the leafs that this instance {@link #matches(Leaf) matches} start with the given node. The
   * path will be split according to {@link Leaf#fromPath(CharSequence)}.
   *
   * @see #matchesParent(Leaf)
   */
  public boolean matchesParent(CharSequence path) {
    return matchesParent(Leaf.fromPath(path));
  }

  /**
   * Transform the words in the given matcher according to the nameTransformer given. This can be used to fulfil
   * certain
   * use-cases, for example to expand namespaces in patterns like {@code foaf:name/foaf:familyName} to their
   * fully-qualified forms.
   *
   * @param nameTransformer The function used to transform the matchers words
   * @return The new matcher
   */
  public Matcher transform(final Function<? super String, String> nameTransformer) {
    checkNotNull(nameTransformer);
    if (matchesAll()) {
      return this;
    }
    AstNode ast = getAstNode();

    AstNode transformed = new CopyVisitor() {
      @Override
      protected Word createWordCopy(String stringValue) {
        return super.createWordCopy(nameTransformer.apply(stringValue));
      }
    }.applyTo(ast);
    return new AstMatcher(transformed);
  }

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
    if (matchesAll()) {
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

  /**
   * Get the ast node this matcher represents.
   */
  protected AstNode getAstNode() {
    return new Parser().parse(patternString());
  }
}
