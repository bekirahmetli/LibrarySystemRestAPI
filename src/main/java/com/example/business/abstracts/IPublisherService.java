package com.example.business.abstracts;

import com.example.entities.Publisher;
import org.springframework.data.domain.Page;

public interface IPublisherService {
    Publisher save(Publisher publisher);
    Publisher get(int id);
    Publisher update(Publisher publisher);
    boolean delete(int id);
    Page<Publisher> cursor(int page, int pageSize);
}
