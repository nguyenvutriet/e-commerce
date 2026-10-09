import { FaExclamationTriangle } from "react-icons/fa";




const ErrorPage = ({message}) => {
  
    return (

      <div className='flex flex-col items-center justify-center px-6 py-14'>
        
        <FaExclamationTriangle className='text-red-500 text-6xl mb-4' />

        <p className='text-gray-600 mb-6 text-center'>
          <div>{message ? message : "An unexpected error has occured"}</div>
        
        </p>
      </div>


      
      
    )
};

export default ErrorPage;