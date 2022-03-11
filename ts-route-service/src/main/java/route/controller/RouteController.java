package route.controller;

import edu.fudan.common.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import route.entity.RouteInfo;
import route.service.RouteService;

import static org.springframework.http.ResponseEntity.ok;

/**
 * @author fdse
 */
@RestController
@RequestMapping("/api/v1/routeservice")
public class RouteController {
    public static int captureVal = 0;
    public static int percentageVal = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(RouteController.class);
    @Autowired
    private RouteService routeService;

    @GetMapping(path = "/exceptionMonitor")
    public void exceptionManager(@RequestParam("capture") String capture,@RequestParam("percentage") String percentage) {
        captureVal = Integer.parseInt(capture);
        percentageVal = Integer.parseInt(percentage);
        System.out.println("Inside exceptionManager");
        System.out.println(captureVal + " " + percentageVal);
    }

    @GetMapping(path = "/welcome")
    public String home() {
        return "Welcome to [ Route Service ] !";
    }

    @PostMapping(path = "/routes")
    public ResponseEntity<Response> createAndModifyRoute(@RequestBody RouteInfo createAndModifyRouteInfo, @RequestHeader HttpHeaders headers) {
        RouteController.LOGGER.info("Create route, start: {}, end: {}", createAndModifyRouteInfo.getStartStation(),createAndModifyRouteInfo.getEndStation());
        return ok(routeService.createAndModify(createAndModifyRouteInfo, headers));
    }

    @DeleteMapping(path = "/routes/{routeId}")
    public HttpEntity deleteRoute(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        RouteController.LOGGER.info("Delete route,RouteId: {}", routeId);
        return ok(routeService.deleteRoute(routeId, headers));
    }

    @GetMapping(path = "/routes/{routeId}")
    public HttpEntity queryById(@PathVariable String routeId, @RequestHeader HttpHeaders headers) {
        System.out.println("Insinde queryById method ....");
        RouteController.LOGGER.info("Query route by id, RouteId: {}", routeId);
        Response response = null;

        try {
            response = routeService.getRouteById(routeId, headers);
            LOGGER.info("queryById: Try block Info Logger" + response.toString());
        }catch(Exception e){
            System.out.println("queryById: Catch block info logger......................" );
            LOGGER.info("Some exception occured in method: queryById " +e.getMessage());
            LOGGER.info(e.getStackTrace().toString());
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("queryById: Outside try catch logger........" + response.toString());
        return ok(response);
    }

    @GetMapping(path = "/routes")
    public HttpEntity queryAll(@RequestHeader HttpHeaders headers) {
        System.out.println("Insinde queryAll method ....");
        RouteController.LOGGER.info("Query all routes");
        Response response = null;
        try{
            response = routeService.getAllRoutes(headers);
            LOGGER.info("queryAll: Try block Info Logger" + response.toString());
        }catch (Exception e){
            LOGGER.info("queryAll: Some exception occured in method: queryById " +e.getMessage());
            System.out.println("Class name is: " + LOGGER.getClass().getName());
            LOGGER.info(e.getStackTrace().toString());
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
        LOGGER.info("queryAll: Outside try catch logger........" + response.toString());
        return ok(response);
    }

    @GetMapping(path = "/routes/{startId}/{terminalId}")
    public HttpEntity queryByStartAndTerminal(@PathVariable String startId,
                                              @PathVariable String terminalId,
                                              @RequestHeader HttpHeaders headers) {
        RouteController.LOGGER.info("Query routes, startId : {}, terminalId: {}", startId, terminalId);
        return ok(routeService.getRouteByStartAndTerminal(startId, terminalId, headers));
    }

}