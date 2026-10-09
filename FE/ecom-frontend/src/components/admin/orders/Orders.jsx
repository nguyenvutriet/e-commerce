import { FaShoppingCart } from 'react-icons/fa'
import OrderTable from './OrderTable';
import { useSelector } from 'react-redux';
import useOrderFilter from '../../../hooks/useOrderFilter';

const Orders = () => {

  // const adminOrders = [{
  //   "orderId": 17,
  //   "email": "user@example.com",
  //   "totalAmount": 100.00,
  //   "status": "Pending",
  //   "date": "2023-06-15"
  // }];
  // const pagination = {
  //   pageNumber: 0,
  //   pageSize: 10,
  //   totalPages: 1,
  //   totalElements: 1
  // }

  const { adminOrders, pagination } = useSelector((state) => state.order);

  const emptyOrder = !adminOrders || adminOrders?.length === 0;

  useOrderFilter();


  return (
    <div className='pb-6 pt-20'>

      {emptyOrder ?(
        <div className='flex flex-col items-center justify-center text-gray-600 py-10'>
          <FaShoppingCart size={50} className='mb-3' />
          <h2 className='text-2xl font-semibold'>No Orders Placed Yet.</h2>

        </div>
      ) : (
        <OrderTable adminOrders={adminOrders} pagination={pagination} />
      )}

    </div>
  )
}

export default Orders