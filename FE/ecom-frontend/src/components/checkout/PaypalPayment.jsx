import { Alert, AlertTitle } from "@mui/material"


const PaypalPayment = () => {
    return (
      <div className='h-96 flex justify-center items-center'>
        <Alert severity="warning" variant="filled" style={{maxWidth: "400px"}}>
          <AlertTitle>
            PayPal Method Unavailable
          </AlertTitle>
          Paypal payment is not unavailable at the moment. Please use another payment method.
        </Alert>
      </div>
    )
}

export default PaypalPayment