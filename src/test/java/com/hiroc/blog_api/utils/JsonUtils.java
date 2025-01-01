package com.hiroc.blog_api.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class JsonUtils {
    private static final ObjectWriter ow = new ObjectMapper().writerWithDefaultPrettyPrinter();
    public static String toJson(Object obj) throws JsonProcessingException {
        return ow.writeValueAsString(obj);
    }
}
