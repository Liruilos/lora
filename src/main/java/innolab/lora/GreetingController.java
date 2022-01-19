package innolab.lora;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This is a class of example custom metrics for future reference
 */
@RestController
public class GreetingController {
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private List<String> listOfStrings= new ArrayList<>();

    private MeterRegistry meterRegistry;

    public GreetingController(MeterRegistry meterRegistry) {

        this.meterRegistry = meterRegistry;
        Gauge.builder("spring.listOfThings", listOfStrings, Collection::size)
                .description("A guage of a list of things")
                .register(meterRegistry);
    }

    @Timed(value = "greeting.time", description = "Time taken to return greeting")
    @RequestMapping(path = "/greeting", method = RequestMethod.GET)
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletRequest request){
        Metrics.counter("spring_boot_requests_total", "app", "test-spring", "handler", request.getRequestURI()).increment();
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(path = "/list", method = RequestMethod.POST)
    public void addToList(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletRequest request){

        listOfStrings.add("new request at" + request.getRequestURI());

        Metrics.counter("spring_boot_requests_total", "app", "test-spring", "handler", request.getRequestURI()).increment();

    }

    @RequestMapping(path = "/list", method = RequestMethod.DELETE)
    public void removeFromList(@RequestParam(value = "name", defaultValue = "World") String name, HttpServletRequest request){

        if (!listOfStrings.isEmpty())
        {
            listOfStrings.remove(0);
        }

        Metrics.counter("spring_boot_requests_total", "app", "test-spring", "handler", request.getRequestURI()).increment();

    }

}
