package com.innometrics;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.test.framework.JerseyTest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

public class CounterServiceTest extends JerseyTest {

    public CounterServiceTest() throws Exception {
        super("com.innometrics");
    }

    @Test
    public void testForFailure() throws Exception {
        WebResource webResource = resource();

        String counterName = " ";
        try {
            webResource.path("counter/increment/" + counterName).post();
            Assert.fail("You should not provide empty space as a counter name");
        } catch (Exception ex) {
            // left blank on purpose
        }

        try {
            webResource.path("counter/value").queryParam("counterName", counterName).get(String.class);
            Assert.fail("You should not provide empty counter name");
        } catch (Exception ex) {
            // left blank on purpose
        }

        counterName = "someName";
        try {
            String value = webResource.path("counter/value").queryParam("counterName", counterName).get(String.class);
            Assert.assertEquals(Counter.NO_VALUE, Integer.parseInt(value));
        } catch (Exception ex) {
            // left blank on purpose
        }
    }

    @Test
    public void testForSuccess() throws Exception {
        final String counterName1 = "counter1";
        final String counterName2 = "counter2";
        final String counterName3 = "counter3";

        WebResource webResource = resource();
        webResource.path("counter/increment/" + counterName1).post();
        webResource.path("counter/increment/" + counterName2).post();
        webResource.path("counter/increment/" + counterName2).post();
        webResource.path("counter/increment/" + counterName3).post();
        webResource.path("counter/increment/" + counterName3).post();
        webResource.path("counter/increment/" + counterName3).post();

        String jsonString = webResource.path("counter/listAll")
                .accept(MediaType.APPLICATION_JSON).get(String.class);
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);
        Assert.assertEquals(3, jsonObject.size());

        String value = webResource.path("counter/value").queryParam("counterName", counterName2)
                .accept(MediaType.TEXT_PLAIN).get(String.class);
        Assert.assertEquals(2, Integer.parseInt(value));

        value = webResource.path("counter/value").queryParam("counterName", counterName3)
                .accept(MediaType.TEXT_PLAIN).get(String.class);
        Assert.assertEquals(3, Integer.parseInt(value));
    }
}