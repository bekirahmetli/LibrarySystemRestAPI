package com.example.api;

import com.example.business.abstracts.IAuthorService;
import com.example.core.config.modelMapper.IModelMapperService;
import com.example.core.result.Result;
import com.example.core.result.ResultData;
import com.example.core.utils.ResultHelper;
import com.example.dto.request.AuthorSaveRequest;
import com.example.dto.request.AuthorUpdateRequest;
import com.example.dto.response.AuthorResponse;
import com.example.dto.response.CursorResponse;
import com.example.entities.Author;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/authors")
public class AuthorController {
    private final IAuthorService authorService;
    private final IModelMapperService modelMapperService;

    public AuthorController(IAuthorService authorService, IModelMapperService modelMapperService) {
        this.authorService = authorService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<AuthorResponse> save(@Valid @RequestBody AuthorSaveRequest authorSaveRequest){
        Author author = this.modelMapperService.forRequest().map(authorSaveRequest,Author.class);
        this.authorService.save(author);
        AuthorResponse authorResponse = this.modelMapperService.forResponse().map(author,AuthorResponse.class);
        return ResultHelper.created(authorResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AuthorResponse> get(@PathVariable("id") int id){
        Author author = this.authorService.get(id);
        AuthorResponse authorResponse = this.modelMapperService.forResponse().map(author,AuthorResponse.class);
        return ResultHelper.success(authorResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<AuthorResponse>> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "2") int pageSize
    ){
        Page<Author> authorPage = this.authorService.cursor(page,pageSize);

        Page<AuthorResponse> responsePage = authorPage.map(author ->
           this.modelMapperService.forResponse().map(author,AuthorResponse.class)
        );
        return ResultHelper.cursor(responsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<AuthorResponse> update(@Valid @RequestBody AuthorUpdateRequest authorUpdateRequest){
        Author author = this.modelMapperService.forRequest().map(authorUpdateRequest,Author.class);
        Author authorUpdate = this.authorService.update(author);
        AuthorResponse authorResponse = this.modelMapperService.forResponse().map(authorUpdate,AuthorResponse.class);
        return ResultHelper.success(authorResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.authorService.delete(id);
        return ResultHelper.ok();
    }
}
