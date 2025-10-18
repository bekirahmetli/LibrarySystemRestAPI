package com.example.api;

import com.example.business.abstracts.ICategoryService;
import com.example.core.config.modelMapper.IModelMapperService;
import com.example.core.result.Result;
import com.example.core.result.ResultData;
import com.example.core.utils.ResultHelper;
import com.example.dto.request.category.CategorySaveRequest;
import com.example.dto.request.category.CategoryUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.category.CategoryResponse;
import com.example.entities.Category;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CategoryResponse> get(@PathVariable("id") int id){
        Category category = this.categoryService.get(id);
        CategoryResponse categoryResponse = this.modelMapperService.forResponse().map(category,CategoryResponse.class);
        return ResultHelper.success(categoryResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<CategoryResponse>> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "2") int pageSize
    ){
        Page<Category> categoryPage = this.categoryService.cursor(page,pageSize);

        Page<CategoryResponse> responsePage = categoryPage.map(category ->
                this.modelMapperService.forResponse().map(category,CategoryResponse.class)
        );
        return ResultHelper.cursor(responsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CategoryResponse> update(@Valid @RequestBody CategoryUpdateRequest categoryUpdateRequest){
        Category category = this.modelMapperService.forRequest().map(categoryUpdateRequest,Category.class);
        Category categoryUpdate = this.categoryService.update(category);
        CategoryResponse categoryResponse = this.modelMapperService.forResponse().map(categoryUpdate,CategoryResponse.class);
        return ResultHelper.success(categoryResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.categoryService.delete(id);
        return ResultHelper.ok();
    }
}
