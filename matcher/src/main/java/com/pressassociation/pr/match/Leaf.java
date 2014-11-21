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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

/**
 * A path that represents a leaf in a search tree. This class is used by Matcher as the token to search for.
 *
 * @author Matt Nathan
 */
public class Leaf {
  private static final Splitter PATH_SPLITTER = Splitter.on('/').omitEmptyStrings();

  /**
   * Create a new leaf from the given path. This will split the path using the {@code /} character.
   */
  public static Leaf fromPath(CharSequence path) {
    return copyOf(PATH_SPLITTER.split(path));
  }

  /**
   * Create a new Leaf from a single part.
   */
  public static Leaf of(String pathPart) {
    return new Leaf(ImmutableList.of(pathPart));
  }

  /**
   * Create a Leaf from the list of parts.
   */
  public static Leaf copyOf(Iterable<String> pathParts) {
    return new Leaf(ImmutableList.copyOf(pathParts));
  }

  private final ImmutableList<String> path;

  Leaf(ImmutableList<String> path) {
    this.path = path;
  }

  public ImmutableList<String> getPath() {
    return path;
  }

  @Override
  public String toString() {
    return "Leaf[" + Joiner.on('/').join(path) + ']';
  }
}
