package io.github.yzernik.electrumclient;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class ElectrumRPCClientTest {

    private ByteArrayOutputStream byteArrayOutputStream;
    private ElectrumRPCClient electrumRPCClient;

    @Before
    public void setUp() {
        this.byteArrayOutputStream = new ByteArrayOutputStream();
        this.electrumRPCClient = new ElectrumRPCClient(this.byteArrayOutputStream);
    }

    @After
    public void tearDown() {
        electrumRPCClient = null;
    }

    @Test
    public void testMakeRequestGetBlockHeader() throws Throwable {
        electrumRPCClient.makeRequestGetBlockHeader(27);
        String request = new String(byteArrayOutputStream.toByteArray());
        String expectedRequest = "{ \"id\": \"blk\", \"method\": \"blockchain.block.header\", \"params\": [23]}";

        assertEquals(expectedRequest, request);
    }

}