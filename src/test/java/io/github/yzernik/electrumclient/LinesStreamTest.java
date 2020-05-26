package io.github.yzernik.electrumclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LinesStreamTest {

    @Before
    public void setUp() {
        // Nothing
    }

    @After
    public void tearDown() {
        // Nothing
    }

    @Test
    public void testGetLinesStream() throws Throwable {
        String notificationLine = " {\"jsonrpc\": \"2.0\", \"method\": \"blockchain.headers.subscribe\", \"params\": [{\"hex\": \"00e0ff2730474ed1def5b826e9ffcce8d8c61415fefd398b6c170a000000000000000000f63517f98c86cf28d508f32d2f8aea2e9f8d2a36fdb904198420d40e8c19896660b6c85ef6971217d8193d7b\", \"height\": 631384}]}";
        String[] lines = {
                notificationLine,
                notificationLine,
                notificationLine,
                null
        };

        Stream<String> linesStream = Stream.of(lines);

        linesStream.forEach(line -> {
            System.out.println(line);
        });


    }

}
