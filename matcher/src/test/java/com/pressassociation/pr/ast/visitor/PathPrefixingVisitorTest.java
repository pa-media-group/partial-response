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
import com.pressassociation.pr.ast.Field;
import com.pressassociation.pr.ast.Node;
import com.pressassociation.pr.ast.Word;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Tests for {@link PathPrefixingVisitor}.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class PathPrefixingVisitorTest extends TransformingVisitorTestBase<PathPrefixingVisitor, AstNode> {

  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.<Object[]>builder()
                        .add(args(new Word("prefix"), "items", "prefix/items"))
                        .add(args(new Word("prefix"), "items,type", "prefix/items,prefix/type"))
                        .add(args(new Word("prefix"), "items,type,href", "prefix/items,prefix/type,prefix/href"))
                        .add(args(new Word("prefix"), "items(id)", "prefix/items(id)"))
                        .add(args(new Word("prefix"), "items/id", "prefix/items/id"))
                        .build();
  }

  protected static Object[] args(Field prefix, String source, String expected) {
    return genArgs(prefix, source, expected);
  }

  private final Node prefix;

  public PathPrefixingVisitorTest(Node prefix, String source, String expected) {
    super(source, expected);
    this.prefix = prefix;
  }

  @Override
  protected PathPrefixingVisitor createVisitor() {
    return new PathPrefixingVisitor(prefix);
  }
}
