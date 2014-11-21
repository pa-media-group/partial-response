/*
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
 */

package com.pressassociation.pr.ast.visitor;

import com.pressassociation.pr.ast.*;

/**
 * Generates a string representation of the visited AST. While the output string may not be equal to the pattern that
 * was used to generate the AST it will be functionally equivalent.
 *
 * @author Matt Nathan
 */
public class ToStringVisitor extends TransformingVisitor<String> {
  @SuppressWarnings("StringBufferField")
  private final StringBuilder buffer = new StringBuilder();

  @Override
  protected void afterSubSelectionFields(SubSelection subSelection) {
    buffer.append(')');
  }

  @Override
  protected boolean beforePathField(Path path) {
    buffer.append('/');
    return true;
  }

  @Override
  protected boolean beforeFieldsNext(Fields fields) {
    buffer.append(',');
    return true;
  }

  @Override
  protected boolean beforeSubSelectionFields(SubSelection subSelection) {
    buffer.append('(');
    return true;
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    buffer.append('*');
  }

  @Override
  public void visitWord(Word word) {
    buffer.append(word.getStringValue());
  }

  @Override
  public String getResult() {
    return buffer.toString();
  }
}
