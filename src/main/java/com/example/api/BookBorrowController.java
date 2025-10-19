package com.example.api;

import com.example.business.abstracts.IBookBorrowService;
import com.example.core.config.modelMapper.IModelMapperService;
import com.example.core.result.ResultData;
import com.example.core.utils.ResultHelper;
import com.example.dto.request.bookborrow.BookBorrowSaveRequest;
import com.example.dto.response.bookborrow.BookBorrowResponse;
import com.example.entities.BookBorrowing;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/bookborrowing")
public class BookBorrowController {
    private final IBookBorrowService bookBorrowService;
    private final IModelMapperService modelMapperService;

    public BookBorrowController(IBookBorrowService bookBorrowService, IModelMapperService modelMapperService) {
        this.bookBorrowService = bookBorrowService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<BookBorrowResponse> save(@Valid @RequestBody BookBorrowSaveRequest request){
        BookBorrowing saved = bookBorrowService.save(request);
        BookBorrowResponse response = modelMapperService.forResponse().map(saved, BookBorrowResponse.class);
        return ResultHelper.created(response);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<BookBorrowResponse> get(@PathVariable("id") int id){
        BookBorrowing bookBorrowing = this.bookBorrowService.get(id);
        BookBorrowResponse response = this.modelMapperService.forResponse().map(bookBorrowing,BookBorrowResponse.class);
        return ResultHelper.success(response);
    }
}
