import { MdAddShoppingCart } from 'react-icons/md';
import { useDispatch, useSelector } from 'react-redux';
import Loader from '../../shared/Loader';
import { FaBoxOpen } from 'react-icons/fa';
import { DataGrid } from '@mui/x-data-grid';
import { useState } from 'react';
import { useDashboardProductFilter } from '../../../hooks/useProductFilter';
import Modal from '../../shared/Modal';
import AddProductForm from './AddProductForm';
import DeleteModal from '../../shared/DeleteModal';
import toast from 'react-hot-toast';
import { deleteProduct } from '../../../store/action';
import ImageUploadform from './ImageUploadform';
import ProductViewModal from '../../shared/ProductViewModal';
import { useLocation, useNavigate, useSearchParams } from 'react-router-dom';
import { adminProductTableColumn } from '../../helper/tableColumn';

const AdminProducts = () => {

  // const products = [
  //   {

  //   }
  // ];
  // const pagination = {

  // }
  const dispatch = useDispatch();
  const {products, pagination} = useSelector((state) => state.products);
  const {isLoading, errorMessage} = useSelector((state) => state.errors);
  const [openUpdateModal, setOpenUpdateModal] = useState(false);
  const emptyProducts = !products || products?.length === 0;
  const [selectedProduct, setSelectedProduct] = useState('');
  const [currentPage, setCurrentPage] = useState(
    pagination?.pageNumber + 1 || 1
  );
  const [openAddModal, setOpenAddModal] = useState(false);
  const [openDeleteModal, setOpenDeleteModal] = useState(false);
  const [loader, setLoader] = useState(false);
  const [openImageUploadModal, setOpenImageUploadModal] = useState(false);
  const [openProductViewModal, setOpenProductViewModal] = useState(false);

  const { user} = useSelector((state) => state.auth);
  const isAdmin = user && user?.roles?.includes("ROLE_ADMIN");

  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const params = new URLSearchParams(searchParams);
  const pathname = useLocation().pathname;

  useDashboardProductFilter();

  const tableRecords = products?.map((item) => {
    return {
      id: item.productId,
      productName: item.productName,
      description: item.description,
      discount: item.discount,
      image: item.image,
      price: item.price,
      quantity: item.quantity,
      specialPrice: item.specialPrice
    }
  });

  const handleEdit = (product) => {
    setSelectedProduct(product); 
    setOpenUpdateModal(true);
  }

  const handleDelete = (product) => {
    setSelectedProduct(product);
    setOpenDeleteModal(true);
    console.log("Delete product", product);
  }

  const handleUploadImage = (product) => {
    setSelectedProduct(product);
    setOpenImageUploadModal(true);
  }

  const handleProductView = (product) => {
    setSelectedProduct(product);
    setOpenProductViewModal(true);
  }

  const handlePaginationChange = (paginationModel) => {
    const page = paginationModel.page + 1;
    setCurrentPage(page);
    params.set("page", page.toString());
    navigate(`${pathname}?${params}`)
  }

  const onDeleteHandler = () => {
    dispatch(deleteProduct(setLoader, selectedProduct?.id, toast, setOpenDeleteModal, isAdmin));
  }

  return (
    <div>
      <div className='pt-6 pb-10 flex justify-end'>
        <button 
          onClick={() => setOpenAddModal(true)}
          className='bg-blue-500 hover:bg-blue-800 text-white font-semibold py-2 px-4 flex items-center gap-2 rounded-md shadow-md transition-colors hover:text-slate-300 duration-300'>
          <MdAddShoppingCart className='text-xl' />
          Add Product
        </button>
      </div>

      {
        !emptyProducts && (
          <h1 className='text-slate-800 text-3xl text-center font-bold pb-6 uppercase'>All Products</h1>
        )
      }
      {isLoading ? (
        <Loader />
      ) : (
        <>
          {emptyProducts ? (
            <div className='flex flex-col items-center justify-center text-gray-600 py-10'>
              <FaBoxOpen size={50} className='mb-3' />
              <h2 className='text-2xl font-semibold'>
                No products created yet.

              </h2>
            

            </div>
          ) : (
            <div className='max-w-full '>
              <DataGrid className='w-full'
                rows={tableRecords}
                columns={adminProductTableColumn(
                  handleEdit, 
                  handleDelete, 
                  handleUploadImage,
                  handleProductView
                )}
                paginationMode='server'
                rowCount={pagination?.totalElements || 0}
                initialState={{
                  pagination: {
                    paginationModel: {
                      pageSize: pagination?.totalElements || -1,
                      page: currentPage - 1,
      
                    },
                  },
                }}
                onPaginationModelChange={handlePaginationChange}
                disableRowSeletionOnClick
                disableColumnResize
                pageSizeOptions={[pagination?.pageSize || 10]}
                pagination 
                paginationOptions={{
                  showFirstButton: true,
                  showLastButton: true,
                  hideNextButton: currentPage === pagination?.totalPages,
                }}
              />

            </div>
          )}
        </>
      )}


    <Modal open={openUpdateModal || openAddModal}
          setOpen={openUpdateModal ? setOpenUpdateModal : setOpenAddModal}
          title={openUpdateModal ? "Update Product" : "Add Product"}
          >
      <AddProductForm setOpen={openUpdateModal ? setOpenUpdateModal : setOpenAddModal}
          product={selectedProduct}
          update={openUpdateModal}
           />
    </Modal>

    <Modal open={openImageUploadModal}
          setOpen={setOpenImageUploadModal}
          title="Add Product Image"
          >
      <ImageUploadform setOpen={setOpenImageUploadModal}
          product={selectedProduct}
           />
    </Modal>

    <DeleteModal open={openDeleteModal}
          setOpen={setOpenDeleteModal}
          loader={loader}
          title="Are you sure want to delete?"
          onDeleteHandler={() => {onDeleteHandler}}
    />

    <ProductViewModal 
        open={openProductViewModal}
        setOpen={setOpenProductViewModal}
        product={selectedProduct}
    />

    </div>

  )
}

export default AdminProducts