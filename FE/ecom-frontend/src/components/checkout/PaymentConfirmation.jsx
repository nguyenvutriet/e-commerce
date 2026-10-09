import { Skeleton } from "@mui/material";
import { useEffect, useState } from "react";
import { FaCheckCircle } from "react-icons/fa";
import { useDispatch, useSelector } from "react-redux";
import { useLocation } from "react-router-dom";
import { stripePaymentConfirmation } from "../../store/action";


const PaymentConfirmation = () => {

  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const dispatch = useDispatch();
  const {cart} = useSelector((state) => state.cart);
  const [loading, setLoading] = useState(true);

  const paymentIntent = searchParams.get("payment_intent");
  const clientSecret = searchParams.get("payment_intent_client_secret");
  const redirectStatus = searchParams.get("redirect_status");
  const [errorMessage, setErrorMessage] = useState("");
  const selectUserCheckoutAddress = localStorage.getItem("CHECKOUT_ADDRESS")
    ? JSON.parse(localStorage.getItem("CHECKOUT_ADDRESS"))
    : [];

  useEffect(() => {
    if(paymentIntent && clientSecret && redirectStatus && cart && cart.length > 0){

      const sendData = {
        addressId: selectUserCheckoutAddress.addressId,
        pgName: "Stripe",
        pgPaymentId: paymentIntent,
        pgStatus: "succeeded",
        pgResponseMessage: "Payment successful" 
      }
      console.log(selectUserCheckoutAddress);
      console.log("sendData", sendData);
      dispatch(stripePaymentConfirmation(sendData,setErrorMessage, setLoading, paymentIntent, clientSecret, redirectStatus, cart));
    }

  }, [paymentIntent, clientSecret, redirectStatus, cart]);



  return (
    <div className='min-h-screen flex items-center justify-center'>
      {loading ? (
        <div className='max-w-xl mx-auto'>
          <Skeleton />
        </div>

      ) : (
        <div className='p-8 rounded-lg shadow-lg text-center max-w-md mx-auto border '>
          <div className='text-green-500 mb-4 flex justify-center'>
            <FaCheckCircle size={64} />
          </div>

          <h2 className='text-3xl font-bold text-grvay-800 mb-2'>Payment Successful!</h2>
          <p>
            Thank you for your purchase! Your payment has been successfully processed. You will receive a confirmation email shortly.
          </p>

        </div>
      )}

    </div>
  )
};

export default PaymentConfirmation;