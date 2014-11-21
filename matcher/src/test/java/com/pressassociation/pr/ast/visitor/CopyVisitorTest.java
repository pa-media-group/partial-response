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

import com.google.common.collect.ImmutableList;

import com.pressassociation.pr.ast.AstNode;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.junit.Assert.assertNotSame;

/**
 * Tests for the {@link CopyVisitor}.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class CopyVisitorTest extends TransformingVisitorTestBase<CopyVisitor, AstNode> {
  @Parameterized.Parameters
  public static Collection<Object[]> data() {
    // can't put * on it's own here because Wildcard is a singleton
    return ImmutableList.of(
        args("item", "item"),
        args("item,items/id,other(id,value),*/title", "item,items/id,other(id,value),*/title"));
  }

  public CopyVisitorTest(String source, String expected) {
    super(source, expected);
  }

  @Override
  protected void checkResult(AstNode sourceNode, AstNode resultNode) {
    super.checkResult(sourceNode, resultNode);
    assertNotSame(sourceNode, resultNode);
  }

  @Override
  protected CopyVisitor createVisitor() {
    return new CopyVisitor();
  }
}
