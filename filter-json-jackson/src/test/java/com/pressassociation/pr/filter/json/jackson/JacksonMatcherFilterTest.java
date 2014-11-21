package com.pressassociation.pr.filter.json.jackson;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.pressassociation.pr.match.Matcher;

import org.junit.Test;

import java.util.List;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link JacksonMatcherFilter}.
 */
public class JacksonMatcherFilterTest {
  @Test
  public void testAll() throws JsonProcessingException {
    assertMatchesJson(
        "*",
        "{\n" +
        "  \"type\" : \"type\",\n" +
        "  \"version\" : 1,\n" +
        "  \"address\" : {\n" +
        "    \"line1\" : \"123 Shop Road\",\n" +
        "    \"line2\" : \"Shop Town\",\n" +
        "    \"postCode\" : \"S12 3IG\"\n" +
        "  },\n" +
        "  \"pets\" : [ {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Lassie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Brian\"\n" +
        "  }, {\n" +
        "    \"type\" : \"cat\",\n" +
        "    \"name\" : \"Moggie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"horse\",\n" +
        "    \"name\" : \"Black Beauty\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testAllExplicit() throws JsonProcessingException {
    assertMatchesJson(
        "type,version,address(line1,line2,postCode),pets(type,name)",
        "{\n" +
        "  \"type\" : \"type\",\n" +
        "  \"version\" : 1,\n" +
        "  \"address\" : {\n" +
        "    \"line1\" : \"123 Shop Road\",\n" +
        "    \"line2\" : \"Shop Town\",\n" +
        "    \"postCode\" : \"S12 3IG\"\n" +
        "  },\n" +
        "  \"pets\" : [ {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Lassie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Brian\"\n" +
        "  }, {\n" +
        "    \"type\" : \"cat\",\n" +
        "    \"name\" : \"Moggie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"horse\",\n" +
        "    \"name\" : \"Black Beauty\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testNone() throws JsonProcessingException {
    assertMatchesJson("missing", "{ }");
  }

  @Test
  public void testNonePetsAge() throws JsonProcessingException {
    assertMatchesJson("pets/age", "{ }");
  }


  @Test
  public void testAnyNamed() throws JsonProcessingException {
    assertMatchesJson(
        "*/name",
        "{\n" +
        "  \"pets\" : [ {\n" +
        "    \"name\" : \"Lassie\"\n" +
        "  }, {\n" +
        "    \"name\" : \"Brian\"\n" +
        "  }, {\n" +
        "    \"name\" : \"Moggie\"\n" +
        "  }, {\n" +
        "    \"name\" : \"Black Beauty\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testAnyType() throws JsonProcessingException {
    assertMatchesJson(
        "*/type",
        "{\n" +
        "  \"pets\" : [ {\n" +
        "    \"type\" : \"dog\"\n" +
        "  }, {\n" +
        "    \"type\" : \"dog\"\n" +
        "  }, {\n" +
        "    \"type\" : \"cat\"\n" +
        "  }, {\n" +
        "    \"type\" : \"horse\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testTypeAndVersion() throws JsonProcessingException {
    assertMatchesJson(
        "type,version",
        "{\n" +
        "  \"type\" : \"type\",\n" +
        "  \"version\" : 1\n" +
        '}');
  }

  @Test
  public void testPostCode() throws JsonProcessingException {
    assertMatchesJson(
        "address/postCode",
        "{\n" +
        "  \"address\" : {\n" +
        "    \"postCode\" : \"S12 3IG\"\n" +
        "  }\n" +
        '}');
  }

  @Test
  public void testTypeAndAllAddress() throws JsonProcessingException {
    assertMatchesJson(
        "type,address",
        "{\n" +
        "  \"type\" : \"type\",\n" +
        "  \"address\" : {\n" +
        "    \"line1\" : \"123 Shop Road\",\n" +
        "    \"line2\" : \"Shop Town\",\n" +
        "    \"postCode\" : \"S12 3IG\"\n" +
        "  }\n" +
        '}');
  }

  @Test
  public void testTypeAndAddressLines() throws JsonProcessingException {
    assertMatchesJson(
        "type,address(line1,line2)",
        "{\n" +
        "  \"type\" : \"type\",\n" +
        "  \"address\" : {\n" +
        "    \"line1\" : \"123 Shop Road\",\n" +
        "    \"line2\" : \"Shop Town\"\n" +
        "  }\n" +
        '}');
  }

  @Test
  public void testPetTypes() throws JsonProcessingException {
    assertMatchesJson(
        "pets/type",
        "{\n" +
        "  \"pets\" : [ {\n" +
        "    \"type\" : \"dog\"\n" +
        "  }, {\n" +
        "    \"type\" : \"dog\"\n" +
        "  }, {\n" +
        "    \"type\" : \"cat\"\n" +
        "  }, {\n" +
        "    \"type\" : \"horse\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testPetNames() throws JsonProcessingException {
    assertMatchesJson(
        "pets/name",
        "{\n" +
        "  \"pets\" : [ {\n" +
        "    \"name\" : \"Lassie\"\n" +
        "  }, {\n" +
        "    \"name\" : \"Brian\"\n" +
        "  }, {\n" +
        "    \"name\" : \"Moggie\"\n" +
        "  }, {\n" +
        "    \"name\" : \"Black Beauty\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testAllPets() throws JsonProcessingException {
    assertMatchesJson(
        "pets",
        "{\n" +
        "  \"pets\" : [ {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Lassie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Brian\"\n" +
        "  }, {\n" +
        "    \"type\" : \"cat\",\n" +
        "    \"name\" : \"Moggie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"horse\",\n" +
        "    \"name\" : \"Black Beauty\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testAllPetsWildcard() throws JsonProcessingException {
    assertMatchesJson(
        "pets/*",
        "{\n" +
        "  \"pets\" : [ {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Lassie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"dog\",\n" +
        "    \"name\" : \"Brian\"\n" +
        "  }, {\n" +
        "    \"type\" : \"cat\",\n" +
        "    \"name\" : \"Moggie\"\n" +
        "  }, {\n" +
        "    \"type\" : \"horse\",\n" +
        "    \"name\" : \"Black Beauty\"\n" +
        "  } ]\n" +
        '}');
  }

  @Test
  public void testGetMatcher() {
    Matcher matcher = Matcher.of("foo/bar");
    assertSame(matcher, new JacksonMatcherFilter(matcher).getMatcher());
  }

  private void assertMatchesJson(CharSequence fields, String json) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT);
    JacksonMatcherFilter filter = new JacksonMatcherFilter(Matcher.of(fields));
    mapper.setFilters(new SimpleFilterProvider()
                          .addFilter("test", filter));
    assertEquals(json, mapper.writeValueAsString(new PetStore()));
    assertNull("Runtime state was not cleared", filter.state.get());
  }

  /**
   * Test object for serialisation.
   */
  @JsonFilter("test")
  @SuppressWarnings({"NonFinalFieldReferencedInHashCode", "NonFinalFieldReferenceInEquals"})
  public static class PetStore {
    public String type = "type";
    public int version = 1;

    public Address address = new Address();
    public List<Pet> pets = Lists.newArrayList(
        new Pet("dog", "Lassie"),
        new Pet("dog", "Brian"),
        new Pet("cat", "Moggie"),
        new Pet("horse", "Black Beauty")
    );

    @Override
    public int hashCode() {
      return Objects.hashCode(type, version, address, pets);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
      if (obj instanceof PetStore) {
        PetStore that = (PetStore) obj;
        return Objects.equal(type, that.type)
               && version == that.version
               && Objects.equal(address, that.address)
               && Objects.equal(pets, that.pets);
      }
      return false;
    }

    @Override
    public String toString() {
      return Objects.toStringHelper(this)
                    .add("type", type)
                    .add("version", version)
                    .add("address", address)
                    .add("pets", pets)
                    .toString();
    }

    /**
     * Test object for serialisation.
     */
    @JsonFilter("test")
    public static class Address {
      public String line1 = "123 Shop Road";
      public String line2 = "Shop Town";
      public String postCode = "S12 3IG";

      @Override
      public int hashCode() {
        return Objects.hashCode(line1, line2, postCode);
      }

      @Override
      public boolean equals(@Nullable Object obj) {
        if (obj instanceof Address) {
          Address that = (Address) obj;
          return Objects.equal(line1, that.line1)
                 && Objects.equal(line2, that.line2)
                 && Objects.equal(postCode, that.postCode);
        }
        return false;
      }

      @Override
      public String toString() {
        return Objects.toStringHelper(this)
                      .add("line1", line1)
                      .add("line2", line2)
                      .add("postCode", postCode)
                      .toString();
      }
    }

    /**
     * Test object for serialisation.
     */
    @JsonFilter("test")
    public static class Pet {
      public String type;
      public String name;

      public Pet(String type, String name) {
        this.type = checkNotNull(type);
        this.name = checkNotNull(name);
      }

      @Override
      public int hashCode() {
        return Objects.hashCode(type, name);
      }

      @Override
      public boolean equals(@Nullable Object obj) {
        if (obj instanceof Pet) {
          Pet that = (Pet) obj;
          return Objects.equal(type, that.type)
                 && Objects.equal(name, that.name);
        }
        return false;
      }

      @Override
      public String toString() {
        return Objects.toStringHelper(this)
                      .add("type", type)
                      .add("name", name)
                      .toString();
      }
    }
  }
}
