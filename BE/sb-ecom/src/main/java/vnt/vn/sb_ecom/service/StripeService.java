package vnt.vn.sb_ecom.service;

import com.stripe.model.PaymentIntent;
import vnt.vn.sb_ecom.payload.StripePaymentDTO;

public interface StripeService {

    PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDTO);
}
