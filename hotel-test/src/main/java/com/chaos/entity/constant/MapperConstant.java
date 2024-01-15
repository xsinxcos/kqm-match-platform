package com.chaos.entity.constant;

public class MapperConstant {
    public static final String MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"test\":{\n" +
            "      \"properties\": {\n" +
            "        \"id\":{\n" +
            "          \"type\":\"keyword\"\n" +
            "        },\n" +
            "        \"name\":{\n" +
            "          \"type\":\"text\",\n" +
            "          \"analyzer\":\"ik_max_word\"\n" +
            "        },\n" +
            "        \"address\":{\n" +
            "          \"type\":\"keyword\",\n" +
            "          \"index\": false\n" +
            "        },\n" +
            "        \"price\":{\n" +
            "          \"type\": \"integer\"\n" +
            "        },\n" +
            "        \"score\":{\n" +
            "          \"type\": \"integer\"\n" +
            "        },\n" +
            "        \"brand\":{\n" +
            "          \"type\": \"keyword\",\n" +
            "          \"copy_to\": \"name\"\n" +
            "        },\n" +
            "        \"city\":{\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"starName\":{\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"business\":{\n" +
            "          \"type\": \"keyword\"\n" +
            "        },\n" +
            "        \"location\":{\n" +
            "          \"type\": \"geo_point\",\n" +
            "          \"copy_to\": \"name\"\n" +
            "        },\n" +
            "        \"pic\":{\n" +
            "          \"type\": \"keyword\"\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
}
