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

package com.pressassociation.pr.parser;

import com.google.common.base.CharMatcher;

import com.pressassociation.pr.ast.*;
import com.pressassociation.pr.match.Matcher;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>Parser of partial response patterns. This parser outputs an AST representing the partial response structure.
 *
 * <p>It is very unlikely that you want to be working with the Parser and Ast directly, try {@link Matcher} instead.
 *
 * @author Matt Nathan
 */
public class Parser {
  // matcher for non-whitespace and non special chars ,/()*
  private static final CharMatcher WORD_CHAR_MATCHER = CharMatcher.WHITESPACE.negate().and(CharMatcher.noneOf(",/()*"));

  /**
   * Parse the given partial response input, return the Ast for the pattern.
   */
  public AstNode parse(CharSequence input) {
    return parse(new Input(checkNotNull(input)));
  }

  private AstNode parse(Input input) {
    AstNode result = parseFields(input);
    if (!input.isEof()) {
      throw new IllegalArgumentException("Unexpected characters '" + input.getRemaining() + '\'');
    }
    return result;
  }

  private AstNode parseFields(Input input) {
    Field field = parseField(input);
    if (input.consumeIf(',')) {
      AstNode fields = parseFields(input);
      return new Fields(field, fields);
    } else {
      return field;
    }
  }

  private Field parseField(Input input) {
    Node node = parseNode(input);
    if (input.consumeIf('/')) {
      Field field = parseField(input);
      return new Path(node, field);
    } else {
      return node;
    }
  }

  private Node parseNode(Input input) {
    Name name = parseName(input);
    if (input.consumeIf('(')) {
      AstNode fields = parseFields(input);
      input.consume(')');
      return new SubSelection(name, fields);
    } else {
      return name;
    }
  }

  private Name parseName(Input input) {
    return input.consumeIf('*') ?
           Wildcard.getSharedInstance() :
           new Word(input.consume(WORD_CHAR_MATCHER));
  }

  /**
   * Helper class for tracking the parsing position and helping with navigation through the chars.
   */
  private static final class Input {
    private final CharSequence chars;
    private int offset = 0;

    private Input(CharSequence chars) {
      this.chars = chars;
    }

    public boolean isEof() {
      return offset == chars.length();
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    public boolean consumeIf(char character) {
      if (!isEof() && chars.charAt(offset) == character) {
        offset++;
        return true;
      }
      return false;
    }

    public String consume(CharMatcher matcher) {
      int lastIndex = offset;
      while (lastIndex < chars.length() && matcher.matches(chars.charAt(lastIndex))) {
        lastIndex++;
      }
      if (lastIndex == offset) {
        throw new IllegalArgumentException("Was expecting at least one of " + matcher);
      }
      try {
        return chars.subSequence(offset, lastIndex).toString();
      } finally {
        offset = lastIndex;
      }
    }

    public void consume(char character) {
      if (isEof()) {
        throw new IllegalArgumentException("We reached the end of the file, expecting '" + character + '\'');
      }
      char value = chars.charAt(offset);
      if (value != character) {
        throw new IllegalArgumentException("Expecting '" + character + "' at offset " + offset);
      }

      offset++;
    }

    public String getRemaining() {
      return chars.subSequence(offset, chars.length()).toString();
    }
  }
}
