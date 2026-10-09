package vnt.vn.sb_ecom.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vnt.vn.sb_ecom.model.Category;
import vnt.vn.sb_ecom.model.Product;
import vnt.vn.sb_ecom.model.Users;

@Repository
public interface ProductRepository  extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Page<Product> findByCategoryOrderByPriceAsc(Category category, Pageable pageable);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);


    Page<Product> findByUser(Users user, Pageable pageDetails);
}
