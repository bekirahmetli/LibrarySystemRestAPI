package com.example.core.config.modelMapper;

import com.example.dto.response.book.BookResponse;
import com.example.dto.response.bookborrow.BookBorrowResponse;
import com.example.entities.*;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;
import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper getModelMapper(){
        ModelMapper mapper = new ModelMapper();

        TypeMap<Book, BookResponse> typeMap = mapper.createTypeMap(Book.class, BookResponse.class);

        typeMap.addMappings(m -> m.map(src -> {
            Author author = src.getAuthor();
            return (author != null) ? author.getId() : 0;
        }, BookResponse::setAuthorId));

        typeMap.addMappings(m -> m.map(src -> {
            Publisher publisher = src.getPublisher();
            return (publisher != null) ? publisher.getId() : 0;
        }, BookResponse::setPublisherId));

        typeMap.addMappings(m -> m.skip(BookResponse::setCategoryIds));

        Converter<List<Category>, List<Integer>> catToIds = ctx -> {
            List<Category> categories = ctx.getSource();
            if (categories == null) return Collections.emptyList();
            return categories.stream().map(Category::getId).toList();
        };
        typeMap.addMappings(m -> m.using(catToIds)
                .map(Book::getCategories, BookResponse::setCategoryIds));

        TypeMap<BookBorrowing, BookBorrowResponse> bbMap =
                mapper.createTypeMap(BookBorrowing.class, BookBorrowResponse.class);
        bbMap.addMappings(m -> m.map(src -> {
            Book b = src.getBook();
            return (b != null) ? b.getId() : 0;
        }, BookBorrowResponse::setBookId));

        return mapper;
    }
}
