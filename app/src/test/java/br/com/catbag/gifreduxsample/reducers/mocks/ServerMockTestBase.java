package br.com.catbag.gifreduxsample.reducers.mocks;

/**
 * Created by niltonvasques on 11/2/16.
 */

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.Scanner;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 * JUnit + OkHTTP Mock Server Tests.
 *
 * @author <a href="mailto:jaredsburrows@gmail.com">Jared Burrows</a>
 */
public abstract class ServerMockTestBase {
    @Rule
    public final MockWebServer server = new MockWebServer();
    protected String mockEndPoint;

    @Before
    public void setUp() throws Exception {
        mockEndPoint = server.url("/").toString();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    protected void sendMockMessages(String fileName, int statusCode) throws Exception {
        final InputStream stream = getResourceAsStream(fileName);
        final String mockResponse = new Scanner(stream, Charset.defaultCharset().name())
                .useDelimiter("\\A").next();

        server.enqueue(new MockResponse()
                .setResponseCode(statusCode)
                .setBody(mockResponse));

        stream.close();
    }

    protected void sendMockMessages(String fileName) throws Exception {
        sendMockMessages(fileName, HttpURLConnection.HTTP_OK);
    }

    public static InputStream getResourceAsStream(String fileName) {
        return ServerMockTestBase.class.getResourceAsStream(fileName);
    }
}

