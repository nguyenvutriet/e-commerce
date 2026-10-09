import {FormControl, FormControlLabel, Radio, RadioGroup} from "@mui/material";
import { useDispatch, useSelector } from "react-redux";
import { addPaymentMethod, createUserCart } from "../../store/action";
import { useEffect } from "react";

const PaymentMethod = () => {

  const dispatch = useDispatch();

  const {paymentMethod} = useSelector((state) => state.payment);
  const {cart, cartId} = useSelector((state) => state.cart);
  const {isLoading, errorMessage} = useSelector((state) => state.errors);

  useEffect(() => {
    if(cart.length > 0 && !cartId && !errorMessage){
      const sendCartItems = cart.map((item) => {
        return {
          productId: item.productId,
          quantity: item.quantity,
        }
      });
      dispatch(createUserCart(sendCartItems));
    }
  }, [dispatch, cartId]);

  const paymentMethodChangeHandler = (method) => {
    dispatch(addPaymentMethod(method));
  }
  return (
    <div className='max-w-md mx-auto p-5 bg-white shadow-md rounded-lg mt-16 border'>
      <h1 className='text-2xl font-semibold mb-4'>
        Select Payment Method
      </h1>
      <FormControl>
        <RadioGroup
          aria-label="payment methodd"
          name="payymentMethod"
          value={paymentMethod}
          onChange={(e) => paymentMethodChangeHandler(e.target.value)}
        >
          <FormControlLabel value="Stripe" control={<Radio color="primary" />} label="Stripe" className='text-gray-700' />
          <FormControlLabel value="Payple" control={<Radio color="primary" />} label="Payple" className='text-gray-700' />
        </RadioGroup>
      </FormControl>

    </div>
  )
};

export default PaymentMethod;