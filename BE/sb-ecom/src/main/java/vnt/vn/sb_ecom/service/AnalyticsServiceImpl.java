package vnt.vn.sb_ecom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vnt.vn.sb_ecom.payload.AnalyticsResponse;
import vnt.vn.sb_ecom.repository.OrderRepository;
import vnt.vn.sb_ecom.repository.ProductRepository;

@Service
public class AnalyticsServiceImpl implements  AnalyticsService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public AnalyticsResponse getAnalyticsdata() {

        AnalyticsResponse response = new AnalyticsResponse();

        long productCount = productRepository.count();
        long totalOrders = orderRepository.count();
        Double totalRevenue = orderRepository.getTotalRevenue();

        response.setProductsCount(String.valueOf(productCount));
        response.setTotalOrders(String.valueOf(totalOrders));
        response.setTotalRevenue(String.valueOf(totalRevenue != null ? totalRevenue : 0.0));
        return response;
    }
}
