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

import com.pressassociation.pr.ast.visitor.AstVisitor;

/**
 * Represents a wildcard name.
 *
 * @author Matt Nathan
 */
public class Wildcard extends Name {
  @SuppressWarnings("UtilityClassWithoutPrivateConstructor")
  private static final class Holder {
    private static final Wildcard INSTANCE = new Wildcard();
  }

  public static Wildcard getSharedInstance() {
    return Holder.INSTANCE;
  }

  private Wildcard() {
  }

  @Override
  public void apply(AstVisitor visitor) {
    visitor.visitWildcard(this);
  }
}
