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
}
