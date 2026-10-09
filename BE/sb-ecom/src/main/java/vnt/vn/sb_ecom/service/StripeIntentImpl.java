package vnt.vn.sb_ecom.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerSearchResult;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vnt.vn.sb_ecom.model.Payment;
import vnt.vn.sb_ecom.payload.StripePaymentDTO;

@Service
@Transactional
public class StripeIntentImpl implements StripeService{

    @Value("${stripe.secret.key}")
    private String stripeAouKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = stripeAouKey;
    }

    @SneakyThrows
    @Override
    public PaymentIntent paymentIntent(StripePaymentDTO stripePaymentDTO) {

        Customer customer;
        // Retrieve and check if customer exist
        CustomerSearchParams searchParams = CustomerSearchParams.builder()
                .setQuery("email:'" + stripePaymentDTO.getEmail() + "'")
                .build();

        CustomerSearchResult customers = Customer.search(searchParams);
        if(customers.getData().isEmpty()){
            // Create a new customer
            CustomerCreateParams customerParams =
                    CustomerCreateParams.builder()
                            .setName(stripePaymentDTO.getName())
                            .setEmail(stripePaymentDTO.getEmail())
                            .setAddress(
                                    CustomerCreateParams.Address.builder()
                                            .setLine1(stripePaymentDTO.getAddress().getStreet())
                                            .setCity(stripePaymentDTO.getAddress().getCity())
                                            .setState(stripePaymentDTO.getAddress().getState())
                                            .setPostalCode(stripePaymentDTO.getAddress().getPinCode())
                                            .setCountry(stripePaymentDTO.getAddress().getCountry())
                                            .build()
                            )
                            .build();

            customer = Customer.create(customerParams);


        }else{
            // Fetch the customer that exist
            customer = customers.getData().get(0);
        }

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(stripePaymentDTO.getAmount())
                .setCurrency(stripePaymentDTO.getCurrency())
                .setCustomer(customer.getId())
                .setDescription(stripePaymentDTO.getDescription())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                ).build();

//        PaymentIntent paymentIntent = PaymentIntent.create(params);

        try {
            return PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }
}
