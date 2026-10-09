import DashboardOverview from "./DashboardOverview";
import { FaBoxOpen, FaShoppingCart  } from "react-icons/fa";
import { MdAttachMoney } from "react-icons/md";
import { useDispatch, useSelector } from "react-redux";
import { useEffect } from "react";
import Loader from "../../shared/Loader";
import ErrorPage from "../../shared/ErrorPage";
import { analyticsAction } from "../../../store/action";

const Dashboard = () => {

  const dispatch = useDispatch();
  const {isLoading, errrorMessage} = useSelector((state) => state.errors);

  const {analytics: {productCount, totalRevenue, totalOrders}} = useSelector((state) => state.admin);

  useEffect(() => {
    dispatch(analyticsAction())
  }, [dispatch]);

  if(isLoading){
    return <Loader />
  }

  if(errrorMessage){
    return <ErrorPage message={errrorMessage} />
  }






  return (
    <div className=''>
      <div className='flex md:flex-row mt-8 flex-col lg:justify-between 
        border border-slate-400 rounded-lg bg-linear-to-r from-blue-50 to-blue-100 shadow-lg'>
          <DashboardOverview 
            title="Total Products"
            amount={productCount}
            Icon={FaBoxOpen}
          />
          <DashboardOverview 
            title="Total Orders"
            amount={totalOrders}
            Icon={FaShoppingCart}
          />

          <DashboardOverview 
            title="Total Revenue"
            amount={totalRevenue}
            Icon={MdAttachMoney}
          />

      </div>

    </div>
  )
};

export default Dashboard;