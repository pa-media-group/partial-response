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

package com.pressassociation.pr.ast;

import com.google.common.collect.ImmutableList;
import com.google.common.testing.NullPointerTester;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;

/**
 * Checks all the constructors, methods and static methods of every concrete ast node implementation.
 *
 * @author Matt Nathan
 */
@RunWith(Parameterized.class)
public class AstNullPointerTest {
  @Parameterized.Parameters
  public static Collection<Object[]> parameters() {
    return ImmutableList.of(
        new Object[]{Fields.class, new Fields(Wildcard.getSharedInstance(), Wildcard.getSharedInstance())},
        new Object[]{Path.class, new Path(Wildcard.getSharedInstance(), Wildcard.getSharedInstance())},
        new Object[]{
            SubSelection.class, new SubSelection(Wildcard.getSharedInstance(), Wildcard.getSharedInstance())
        },
        new Object[]{Wildcard.class, Wildcard.getSharedInstance()},
        new Object[]{Word.class, new Word("word")});
  }

  private NullPointerTester tester;
  private final Class<?> type;
  private final Object instance;

  public AstNullPointerTest(Class<?> type, Object instance) {
    this.type = type;
    this.instance = instance;
  }

  @Before
  public void setUp() {
    tester = new NullPointerTester();
    tester.setDefault(AstNode.class, Wildcard.getSharedInstance());
    tester.setDefault(Field.class, Wildcard.getSharedInstance());
    tester.setDefault(Name.class, Wildcard.getSharedInstance());
    tester.setDefault(Node.class, Wildcard.getSharedInstance());
  }

  @Test
  public void testNulls() throws Exception {
    tester.testAllPublicConstructors(type);
    tester.testAllPublicStaticMethods(type);
    tester.testAllPublicInstanceMethods(instance);
  }
}
