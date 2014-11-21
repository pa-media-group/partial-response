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

package com.pressassociation.pr.ast.visitor;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import com.pressassociation.pr.ast.*;

import java.util.List;

import static com.google.common.base.Objects.firstNonNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * Visitor that collects all top level fields of the ast.
 * <p/>
 * For example:
 * Given {@code item, href, people(id)} this will return {@code [people(id), href, item]} as a collection of field.
 * <p/>
 * Results are returned in what you might consider reverse order
 *
 * @author Matt Nathan
 */
public class FindFieldsVisitor extends TransformingVisitor<Iterable<Field>> {

  // this visitor only works because we are assuming that all sub-selections have already been expanded
  private final List<Fields> result = Lists.newArrayList();
  private Field last;

  @Override
  public void visitFields(Fields fields) {
    result.add(fields);
    super.visitFields(fields);
  }

  @Override
  public Iterable<Field> getResult() {
    return getFields();
  }

  @Override
  public void visitPath(Path path) {
    last = firstNonNull(last, path);
  }

  @Override
  public void visitSubSelection(SubSelection subSelection) {
    last = firstNonNull(last, subSelection);
  }

  @Override
  public void visitWildcard(Wildcard wildcard) {
    last = firstNonNull(last, wildcard);
  }

  @Override
  public void visitWord(Word word) {
    last = firstNonNull(last, word);
  }

  private Iterable<Field> getFields() {
    if (result.isEmpty()) {
      checkState(last != null, "Cannot get the result if the visitor hasn't been used");
      return ImmutableList.of(last);
    }
    List<Field> allButLast = Lists.transform(result, new Function<Fields, Field>() {
      @Override
      public Field apply(Fields input) {
        return input.getField();
      }
    });
    return Iterables.concat(ImmutableList.of(getEnd()), Lists.reverse(allButLast));
  }

  private Field getEnd() {
    // we know this is safe because all of the Fields have been expanded
    return (Field) Iterables.getLast(result).getNext();
  }
}
