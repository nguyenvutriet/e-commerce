package vnt.vn.sb_ecom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import vnt.vn.sb_ecom.config.AppConstants;
import vnt.vn.sb_ecom.model.Category;
import vnt.vn.sb_ecom.payload.CategoryDTO;
import vnt.vn.sb_ecom.payload.CategoryResponse;
import vnt.vn.sb_ecom.service.CategoryService;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;



    @GetMapping("/echo")
    public ResponseEntity<String> echoMessage(@RequestParam(name = "message", defaultValue = "Hello world", required = false) String message){
        return new ResponseEntity<>("Echoed message: " + message, HttpStatus.OK);
    }

    @Tag(name ="Category APIs", description = "APIs for managing categories")
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                             @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                             @RequestParam(name ="sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
                                                             @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR) String sortOrder){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }


    @Tag(name ="Category APIs", description = "APIs for managing categories")
    @Operation(summary = "Create category", description = "API to  create a new category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category is created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server error", content = @Content)
    })
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> createCategory(@Parameter(description = "Category that you wish to create") @Valid @RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategoryDTO = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(savedCategoryDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/category/{categoryId}")
    public ResponseEntity<CategoryDTO> deleteCategory(@Parameter(description = "ID of the category that you wish to delete") @PathVariable Long categoryId){

        CategoryDTO categoryDTO = categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
    }

    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO, @PathVariable Long categoryId){
        CategoryDTO status = categoryService.updateCategory(categoryDTO, categoryId);
        return new ResponseEntity<>(status , HttpStatus.OK);
    }

}
