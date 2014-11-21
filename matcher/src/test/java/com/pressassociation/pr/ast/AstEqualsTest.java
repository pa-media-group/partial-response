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

import com.google.common.testing.EqualsTester;

import org.junit.Test;

/**
 * Tests to check the equality of different AST configurations.
 *
 * @author Matt Nathan
 */
public class AstEqualsTest {
  @Test
  public void testEquals() {
    Wildcard wildcard = Wildcard.getSharedInstance();
    new EqualsTester()
        .addEqualityGroup(new Fields(wildcard, wildcard), new Fields(wildcard, wildcard))
        .addEqualityGroup(new Path(wildcard, wildcard), new Path(wildcard, wildcard))
        .addEqualityGroup(new SubSelection(wildcard, wildcard), new SubSelection(wildcard, wildcard))
        .addEqualityGroup(wildcard, wildcard)
        .addEqualityGroup(new Word("name"), new Word("name"))
        .addEqualityGroup(new Word("other"), new Word("other"))
        .testEquals();
  }
}
