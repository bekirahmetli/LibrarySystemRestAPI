package com.example.api;

import com.example.business.abstracts.IAuthorService;
import com.example.entities.Author;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/authors")
public class AuthorController {
    private final IAuthorService authorService;

    public AuthorController(IAuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Author save(@Valid @RequestBody Author author){
        return this.authorService.save(author);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Author get(@PathVariable("id") int id){
        return this.authorService.get(id);
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Page<Author> cursor(
            @RequestParam(name = "page",required = false,defaultValue = "0") int page,
            @RequestParam(name = "pageSize",required = false,defaultValue = "2") int pageSize
    ){
        Page<Author> authorPage = this.authorService.cursor(page,pageSize);
        return authorPage;
    }

    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public Author update(@Valid @RequestBody Author author){
        authorService.get(author.getId());
        return this.authorService.update(author);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean delete(@PathVariable("id") int id){
        Author author = this.authorService.get(id);
        this.authorService.delete(author.getId());
        return true;
    }
}
