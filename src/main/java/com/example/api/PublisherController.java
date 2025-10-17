package com.example.api;

import com.example.business.abstracts.IPublisherService;
import com.example.core.config.modelMapper.IModelMapperService;
import com.example.core.result.Result;
import com.example.core.result.ResultData;
import com.example.core.utils.ResultHelper;
import com.example.dto.request.publisher.PublisherSaveRequest;
import com.example.dto.request.publisher.PublisherUpdateRequest;
import com.example.dto.response.CursorResponse;
import com.example.dto.response.publisher.PublisherResponse;
import com.example.entities.Publisher;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/publishers")
public class PublisherController {
    private final IPublisherService publisherService;
    private final IModelMapperService modelMapperService;

    public PublisherController(IPublisherService publisherService, IModelMapperService modelMapperService) {
        this.publisherService = publisherService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResultData<PublisherResponse> save(@Valid @RequestBody PublisherSaveRequest publisherSaveRequest){
        Publisher publisherSave = this.modelMapperService.forRequest().map(publisherSaveRequest,Publisher.class);
        this.publisherService.save(publisherSave);
        PublisherResponse publisherResponse = this.modelMapperService.forResponse().map(publisherSave,PublisherResponse.class);
        return ResultHelper.created(publisherResponse);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResultData<PublisherResponse> get(@PathVariable("id") int id){
        Publisher publisher = this.publisherService.get(id);
        PublisherResponse publisherResponse = this.modelMapperService.forResponse().map(publisher,PublisherResponse.class);
        return ResultHelper.success(publisherResponse);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<CursorResponse<PublisherResponse>> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "2") int pageSize
    ){
        Page<Publisher> publisherPage = this.publisherService.cursor(page,pageSize);

        Page<PublisherResponse> responsePage = publisherPage.map(publisher ->
                this.modelMapperService.forResponse().map(publisher,PublisherResponse.class)
        );
        return ResultHelper.cursor(responsePage);
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResultData<PublisherResponse> update(@Valid @RequestBody PublisherUpdateRequest publisherUpdateRequest){
        Publisher publisher = this.modelMapperService.forRequest().map(publisherUpdateRequest,Publisher.class);
        Publisher publisherUpdate = this.publisherService.update(publisher);
        PublisherResponse publisherResponse = this.modelMapperService.forResponse().map(publisherUpdate,PublisherResponse.class);
        return ResultHelper.success(publisherResponse);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Result delete(@PathVariable("id") int id){
        this.publisherService.delete(id);
        return ResultHelper.ok();
    }
}
