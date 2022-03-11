package route.init;

import route.entity.RouteInfo;
import route.service.RouteService;

import java.util.Calendar;

public class InitThread implements Runnable {
    RouteService routeService;

    long duration;
    long frequency;
    long startTime;

    public InitThread(RouteService routeService, long duration, long frequency) {
        System.out.println("Thread: " + frequency + " , " + duration);
        startTime = Calendar.getInstance().getTimeInMillis();
        this.routeService = routeService;
        this.duration = duration;
        this.frequency = frequency;
    }


    @Override
    public void run() {
        RouteInfo info = new RouteInfo();
        long currentTime = Calendar.getInstance().getTimeInMillis();
        while ((currentTime - startTime) <= (duration*1000)){
            info.setId("0b23bd3e-876a-4af3-b920-c50a90c90b04");
            info.setStartStation("shanghai");
            info.setEndStation("taiyuan");
            info.setStationList("shanghai,nanjing,shijiazhuang,taiyuan");
            info.setDistanceList("0,350,1000,1300");
            routeService.createAndModify(info, null);
            try {
                routeService.getAllRoutes(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            routeService.deleteRoute(info.getId(), null);
            currentTime = Calendar.getInstance().getTimeInMillis();
        }

        info.setId("9fc9c261-3263-4bfa-82f8-bb44e06b2f52");
        info.setStartStation("nanjing");
        info.setEndStation("beijing");
        info.setStationList("nanjing,xuzhou,jinan,beijing");
        info.setDistanceList("0,500,700,1200");
        routeService.createAndModify(info,null);
        try {
            routeService.getAllRoutes(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
