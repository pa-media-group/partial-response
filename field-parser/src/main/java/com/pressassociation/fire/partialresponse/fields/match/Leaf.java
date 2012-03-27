package com.pressassociation.fire.partialresponse.fields.match;

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

  public static Leaf fromPath(CharSequence path) {
    return copyOf(PATH_SPLITTER.split(path));
  }

  public static Leaf of(String pathPart) {
    return new Leaf(ImmutableList.of(pathPart));
  }

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
    return "Leaf[" + Joiner.on('/').join(path) + "]";
  }
}
