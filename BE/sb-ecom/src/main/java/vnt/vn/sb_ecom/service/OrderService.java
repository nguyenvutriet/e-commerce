package vnt.vn.sb_ecom.service;

import jakarta.transaction.Transactional;
import vnt.vn.sb_ecom.payload.OrderDTO;
import vnt.vn.sb_ecom.payload.OrderResponse;

public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String emailId, Long addressId, String paymentMethod, String pgName, String pgPaymentId, String pgStatus, String pgResponseMessage);

    OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    OrderDTO updateOrder(Long orderId, String status);

    OrderResponse getAllSellerOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
