package ca.jrvs.apps.stockquote.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;

public class JsonParser {
    /**
     * Convert a java object to JSON string
     * @param object input object
     * @param prettyJson flag to output as pretty json
     * @param includeNullValues flag to include/exclude null values
     * @return JSON String
     * @throws JsonProcessingException
     */
    public static String toJson(Object object, boolean prettyJson, boolean includeNullValues)
            throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        if(!includeNullValues) {
            m.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        }
        if(prettyJson) {
            m.enable(SerializationFeature.INDENT_OUTPUT);
        }
        return m.writeValueAsString(object);
    }

    /**
     * Parse JSON string to an object
     * @param json JSON str
     * @param clazz object class
     * @param <T> Type
     * @return object
     * @throws IOException
     */
    public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
        ObjectMapper m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        return (T) m.readValue(json, clazz);
    }

}
