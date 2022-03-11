package travel.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import travel.entity.*;
import travel.service.TravelService;

import java.util.ArrayList;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/travelservice")

public class TravelController {

    @Autowired
    private TravelService travelService;

    private static final String TRAIN_SERVICE = "travelServiceCB";

    public HttpEntity  myFallbackMethod(Exception e){
        TravelController.LOGGER.info("Fallback executed.");
        String s = "{\"message\": \"service unavailable due to some failure in method call.\"}";
        return new ResponseEntity<>(s, HttpStatus.SERVICE_UNAVAILABLE);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TravelController.class);



    @GetMapping(path = "/welcome")
//    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public String home(@RequestHeader HttpHeaders headers) {
        //new RestTemplate().getForObject("http://kapilkak.kakal",String.class);
        return "Welcome to [ Travel Service ] !";
    }

    @GetMapping(value = "/train_types/{tripId}")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity getTrainTypeByTripId(@PathVariable String tripId,
                                           @RequestHeader HttpHeaders headers) {
        // TrainType
        TravelController.LOGGER.info("Get train Type by Trip id,TripId: {}", tripId);
        return ok(travelService.getTrainTypeByTripId(tripId, headers));
    }

    @GetMapping(value = "/routes/{tripId}")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity getRouteByTripId(@PathVariable String tripId,
                                       @RequestHeader HttpHeaders headers) {
        TravelController.LOGGER.info("[Get Route By Trip ID], TripId: {}", tripId);
        //Route
        return ok(travelService.getRouteByTripId(tripId, headers));
    }

    @PostMapping(value = "/trips/routes")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity getTripsByRouteId(@RequestBody ArrayList<String> routeIds,
                                        @RequestHeader HttpHeaders headers) {
        // ArrayList<ArrayList<Trip>>
        TravelController.LOGGER.info("Get Trips by Route ids,RouteIds: {}", routeIds.size());
        return ok(travelService.getTripByRoute(routeIds, headers));
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity<?> createTrip(@RequestBody TravelInfo routeIds, @RequestHeader HttpHeaders headers) {
        // null
        TravelController.LOGGER.info("Create trip,TripId: {}", routeIds.getTripId());
        return new ResponseEntity<>(travelService.create(routeIds, headers), HttpStatus.CREATED);
    }

    /**
     * Return Trip only, no left ticket information
     *
     * @param tripId  trip id
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/trips/{tripId}")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity retrieve(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        // Trip
        TravelController.LOGGER.info("Retrieve trip,TripId: {}", tripId);
        return ok(travelService.retrieve(tripId, headers));
    }

    @CrossOrigin(origins = "*")
    @PutMapping(value = "/trips")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity updateTrip(@RequestBody TravelInfo info, @RequestHeader HttpHeaders headers) {
        // Trip
        TravelController.LOGGER.info("Update trip,TripId: {}", info.getTripId());
        return ok(travelService.update(info, headers));
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping(value = "/trips/{tripId}")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity deleteTrip(@PathVariable String tripId, @RequestHeader HttpHeaders headers) {
        // string
        TravelController.LOGGER.info("Delete trip,TripId: {}", tripId);
        return ok(travelService.delete(tripId, headers));
    }

    /**
     * Return Trips and the remaining tickets
     *
     * @param info    trip info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips/left")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity queryInfo(@RequestBody TripInfo info, @RequestHeader HttpHeaders headers) {
        if (info.getStartingPlace() == null || info.getStartingPlace().length() == 0 ||
                info.getEndPlace() == null || info.getEndPlace().length() == 0 ||
                info.getDepartureTime() == null) {
            TravelController.LOGGER.info("[[Travel Query] Fail.Something null.");
            ArrayList<TripResponse> errorList = new ArrayList<>();
            return ok(errorList);
        }
        TravelController.LOGGER.info(" Query TripResponse");
        return ok(travelService.query(info, headers));
    }

    /**
     * Return Trips and the remaining tickets
     *
     * @param info    trip info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trips/left_parallel")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity queryInfoInparallel(@RequestBody TripInfo info, @RequestHeader HttpHeaders headers) {
        if (info.getStartingPlace() == null || info.getStartingPlace().length() == 0 ||
                info.getEndPlace() == null || info.getEndPlace().length() == 0 ||
                info.getDepartureTime() == null) {
            TravelController.LOGGER.info("[[Travel Query] Fail.Something null.");
            ArrayList<TripResponse> errorList = new ArrayList<>();
            return ok(errorList);
        }
        TravelController.LOGGER.info(" Query TripResponse");
        return ok(travelService.queryInParallel(info, headers));
    }

    /**
     * Return a Trip and the remaining
     *
     * @param gtdi    trip all detail info
     * @param headers headers
     * @return HttpEntity
     */
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/trip_detail")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity getTripAllDetailInfo(@RequestBody TripAllDetailInfo gtdi, @RequestHeader HttpHeaders headers) {
        // TripAllDetailInfo
        // TripAllDetail tripAllDetail
        TravelController.LOGGER.info("Get trip detail,TripId: {}", gtdi.getTripId());
        return ok(travelService.getTripAllDetailInfo(gtdi, headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/trips")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        // List<Trip>
        TravelController.LOGGER.info("Query all trips");
        return ok(travelService.queryAll(headers));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/admin_trip")
    @CircuitBreaker(name = TRAIN_SERVICE, fallbackMethod = "myFallbackMethod")
    public HttpEntity adminQueryAll(@RequestHeader HttpHeaders headers) {
        // ArrayList<AdminTrip>
        TravelController.LOGGER.info("Admin query all trips");
        return ok(travelService.adminQueryAll(headers));
    }

}
