package innolab.lora;

import io.micrometer.core.instrument.Metrics;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EventCounterResource {

    /**
     * This endpoint is used by the hardware sender to post updates on how many people have entered or left an event
     * @param eventUpdate the json body with the deviceId and counter amount
     * @param request
     */
    @RequestMapping(path = "/counter", method = RequestMethod.POST)
    public void addToList(@RequestBody EventUpdate eventUpdate, HttpServletRequest request){

        // Check that our variables are valid for making custom metrics
        int counter;

        if(eventUpdate.getDeviceId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "DeviceId variable must not be null");
        }
        try {
            counter = Integer.parseInt(eventUpdate.getCounter());
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Counter variable must be parsable as type int", e);
        }

        if (counter == 0) {
            return;

        } else if(counter > 0) {
            // Add to the incremental counter for this deviceId if positive. Normally the value would be 1, but it may
            // send a number greater than this if the Hardware sender was unable to reach the server.

            String metricName = "spring_arrival_" + eventUpdate.getDeviceId();

            while (counter > 0)
            {
                Metrics.counter(metricName, "app", "spring", "handler", request.getRequestURI()).increment();
                counter--;
            }
        } else if(counter < 0) {
            // Add to the decremental counter for this deviceId if negative. Normally the value would be -1, but it may
            // send a number greater than this if the Hardware sender was unable to reach the server.

            // Switch negative value to positive since we are counting up the people leaving.
            counter = Math.abs(counter);

            String metricName = "spring_departure_" + eventUpdate.getDeviceId();

            while (counter > 0)
            {
                Metrics.counter(metricName, "app", "spring", "handler", request.getRequestURI()).increment();
                counter--;
            }
        }

    }

    private static boolean isNumeric(String str){
        return str != null && str.matches("[0-9.]+");
    }
}