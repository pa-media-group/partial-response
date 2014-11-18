Partial Response
================

[![Build Status](https://travis-ci.org/PressAssociation/partial-response.svg)](https://travis-ci.org/PressAssociation/partial-response)

This project provides a library to help with the processing and implementation of
[partial response](http://googlecode.blogspot.co.uk/2010/03/making-apis-faster-introducing-partial.html) in an API.

The core library consists of parsers, AST, visitors and matcher for working with a partial response value. Higher level
functionality may be added over time including http filters for xml and json to post process your responses for
inclusion in the partial response.

Examples
========

Basic use in your application will generally be of the form:

```java
Matcher matcher = Matcher.of("name,address,spouse(name,address),children/*");

// matches all of these Leafs
assertTrue(matcher.matches("/name"));
assertTrue(matcher.matches("/address"));
assertTrue(matcher.matches("/address/houseNumber"));
assertTrue(matcher.matches("/spouse/name"));
assertTrue(matcher.matches("/spouse/address"));
assertTrue(matcher.matches("/children"));
assertTrue(matcher.matches("/children/any/path"));

// doesn't match any of these
assertFalse(matcher.matches("/unknown"));
assertFalse(matcher.matches("/spouse/children"));
// Note: spouse as a leaf is not included, only certain sub properties are
assertFalse(matcher.matches("/spouse"));
```
