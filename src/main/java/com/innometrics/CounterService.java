package com.innometrics;

import org.json.simple.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

@Path("counter")
public class CounterService {

    @GET
    @Path("value")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getValue(@QueryParam("counterName") String counterName) {
        if (!validateCounterName(counterName)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Counter counter = Counter.getInstance();

        int counterValue;
        if (!counter.exists(counterName)) {
            counterValue = Counter.NO_VALUE;
            System.out.printf("A counter with such name:%s does not exist.\n", counterName);
        } else {
            counterValue = counter.getValue(counterName);
            System.out.printf("Counter name: %s; counter value: %d\n", counterName, counterValue);
        }

        return Response.ok(String.valueOf(counterValue)).build();
    }

    @POST
    @Path("increment/{counterName}")
    public Response increment(@PathParam("counterName") String counterName) {
        if (!validateCounterName(counterName)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        System.out.printf("Incrementing following counter's value by one: %s\n", counterName);
        Counter.getInstance().increment(counterName);
        return Response.ok("Successfully incremented").build();
    }

    @GET
    @Path("listAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        Map<String, Integer> counterList = Counter.getInstance().listAll();
        JSONObject jsonObject = new JSONObject(counterList);
        System.out.println("Listing all available counters..");
        return Response.ok(jsonObject.toJSONString()).build();
    }

    /**
     * Validates a given counter name.
     *
     * @param counterName - a name of counter
     * @return true if passes from validation, otherwise false
     */
    private boolean validateCounterName(String counterName) {
        if (counterName == null || counterName.trim().equals("")) {
            System.err.println("Counter name cannot be null or empty!");

            return false;
        }

        return true;
    }
}
