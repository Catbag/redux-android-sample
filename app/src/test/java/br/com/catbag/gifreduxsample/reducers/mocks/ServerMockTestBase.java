package br.com.catbag.gifreduxsample.reducers.mocks;

/**
 Copyright 26/10/2016
 Felipe Piñeiro (fpbitencourt@gmail.com),
 Nilton Vasques (nilton.vasques@gmail.com) and
 Raul Abreu (raulccabreu@gmail.com)

 Be free to ask for help, email us!

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 in compliance with the License. You may obtain a copy of the License at
 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under the License
 is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 implied. See the License for the specific language governing permissions and limitations under
 the License.
 **/

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
    public final MockWebServer mServer = new MockWebServer();

    protected String mMockEndPoint;

    @Before
    public void setUp() throws Exception {
        mMockEndPoint = mServer.url("/").toString();
    }

    @After
    public void tearDown() throws Exception {
        mServer.shutdown();
    }

    protected void sendMockMessages(String fileName, int statusCode) throws Exception {
        final InputStream stream = getResourceAsStream(fileName);
        final String mockResponse = new Scanner(stream, Charset.defaultCharset().name())
                .useDelimiter("\\A").next();

        mServer.enqueue(new MockResponse()
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

