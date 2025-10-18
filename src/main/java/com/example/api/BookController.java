package com.example.api;

import com.example.business.abstracts.IBookService;
import com.example.core.config.modelMapper.IModelMapperService;
import com.example.core.result.ResultData;
import com.example.core.utils.ResultHelper;
import com.example.dto.request.book.BookSaveRequest;
import com.example.dto.request.book.BookUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.book.BookResponse;
import com.example.entities.Book;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/books")
public class BookController {
    private final IBookService bookService;
    private final IModelMapperService modelMapperService;

    public BookController(IBookService bookService, IModelMapperService modelMapperService) {
        this.bookService = bookService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<BookResponse> save(@Valid @RequestBody BookSaveRequest bookSaveRequest){
        Book saved = bookService.save(bookSaveRequest);
        BookResponse response = modelMapperService.forResponse().map(saved, BookResponse.class);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<BookResponse> get(@PathVariable("id") int id){
        Book book = this.bookService.get(id);
        BookResponse bookResponse = this.modelMapperService.forResponse().map(book,BookResponse.class);
        return ResultHelper.success(bookResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<BookResponse>> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "10") int pageSize
    ){
        Page<Book> bookPage = this.bookService.cursor(page,pageSize);

        Page<BookResponse> responsePage = bookPage.map(book ->
                this.modelMapperService.forResponse().map(book,BookResponse.class)
        );
        return ResultHelper.cursor(responsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<BookResponse> update(@Valid @RequestBody BookUpdateRequest bookUpdateRequest){
        Book updated = bookService.update(bookUpdateRequest);
        BookResponse response = modelMapperService.forResponse().map(updated, BookResponse.class);
        return ResultHelper.success(response);
    }
}
