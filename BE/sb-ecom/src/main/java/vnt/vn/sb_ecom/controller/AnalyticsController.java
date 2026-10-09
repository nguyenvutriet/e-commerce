package vnt.vn.sb_ecom.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vnt.vn.sb_ecom.payload.AnalyticsResponse;
import vnt.vn.sb_ecom.service.AnalyticsService;

@RestController
@RequestMapping("/api")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/admin/app/analytics")
    public ResponseEntity<AnalyticsResponse> getAnalytics(){
        AnalyticsResponse response = analyticsService.getAnalyticsdata();
        return new ResponseEntity<AnalyticsResponse>(response, HttpStatus.OK);
    }
}
