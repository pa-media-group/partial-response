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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link TransformingVisitor}.
 *
 * @author Matt Nathan
 */
public class TransformingVisitorTest {
  @Test
  public void testToString() {
    TransformingVisitor<String> subject = new TransformingVisitor<String>() {
      @Override
      public String getResult() {
        return "expected";
      }
    };
    assertEquals("expected", subject.toString());
  }
}
