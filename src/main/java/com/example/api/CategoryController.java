package com.example.api;

import com.example.business.abstracts.ICategoryService;
import com.example.core.config.modelMapper.IModelMapperService;
import com.example.core.result.ResultData;
import com.example.core.utils.ResultHelper;
import com.example.dto.request.category.CategorySaveRequest;
import com.example.dto.response.category.CategoryResponse;
import com.example.entities.Category;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
    private final ICategoryService categoryService;
    private final IModelMapperService modelMapperService;

    public CategoryController(ICategoryService categoryService, IModelMapperService modelMapperService) {
        this.categoryService = categoryService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<CategoryResponse> save(@Valid @RequestBody CategorySaveRequest categorySaveRequest){
        Category category = this.modelMapperService.forRequest().map(categorySaveRequest,Category.class);
        this.categoryService.save(category);
        CategoryResponse categoryResponse = this.modelMapperService.forResponse().map(category,CategoryResponse.class);
        return ResultHelper.created(categoryResponse);
    }
}
