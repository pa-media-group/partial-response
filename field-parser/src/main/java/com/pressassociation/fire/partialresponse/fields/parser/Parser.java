package com.pressassociation.fire.partialresponse.fields.parser;

import com.google.common.base.CharMatcher;
import com.pressassociation.fire.partialresponse.fields.ast.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Parser for the getNext value.
 *
 * @author Matt Nathan
 */
public class Parser {
  // matcher for non-whitespace and non special chars ,/()*
  private static final CharMatcher WORD_CHAR_MATCHER = CharMatcher.WHITESPACE.negate().and(CharMatcher.noneOf(",/()*"));

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
