# Library System REST API

A layered Spring Boot REST API for managing a library system, featuring CRUD operations for Books, Authors, Categories, Publishers, and Book Borrowings. The project demonstrates DTO usage, validation, business rules, pagination, and proper HTTP status handling.


## Project Overview
This API implements common library operations:
- Manage authors, publishers, categories, and books.
- Borrow and return books with stock tracking.
- Enforce specific business rules:
  - A category cannot be deleted if it has books; return a message instead of deleting.
  - On borrow creation, check stock > 0 and decrement stock.
  - On borrow return, set returnDate and increment stock.
  - Publisher GET responses must not expose address.
  - BookBorrowing update must not accept borrower email.

## Architecture
Layered architecture:
- `entities/` JPA entities and relationships
- `dao/` Spring Data JPA repositories
- `business/` service interfaces and implementations
- `api/` REST controllers
- `dto/` request/response DTOs
- `core/` shared configuration, ModelMapper, exception handling, result wrappers

Important classes:
- `core/config/modelMapper/ModelMapperConfig.java`
- `core/config/modelMapper/ModelManagerService.java`
- `core/utils/GlobalExceptionHandler.java`
- `core/utils/ResultHelper.java`, `core/result/*`

## Tech
- Java 21
- Spring Boot (Web, Data JPA, Validation)
- Lombok
- PostgreSQL
- ModelMapper

## Setup and Run
- Prerequisites:
  - JDK 21
  - PostgreSQL running locally
- Configure DB in `src/main/resources/application.properties`:
  - `spring.datasource.url=jdbc:postgresql://localhost:5432/library_system_rest_api`
  - `spring.datasource.username=postgres`
  - `spring.datasource.password=your_password`

- Build and run:
  - Maven Wrapper: `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring-boot:run` (Windows)

## Configuration
ModelMapper:
- Centralized in `ModelMapperConfig`.
- `ModelManagerService` returns the same `ModelMapper` bean for both requests and responses.
- Custom TypeMaps for nested mappings (e.g., `Book -> BookResponse` and `BookBorrowing -> BookBorrowResponse`).

## Entities and Relationships
- Book
  - Fields: id, bookName, publicationYear, stock
  - Relations: ManyToOne Author, ManyToOne Publisher, ManyToMany Categories, OneToMany BookBorrowing
- Author: OneToMany Books
- Publisher: OneToMany Books
- Category: ManyToMany Books
- BookBorrowing: ManyToOne Book

Note: For bidirectional relationships, prefer excluding relationship fields from Lombok equals/hashCode and toString to avoid cycles.

## DTOs and Validation
- Controllers accept Request DTOs and return Response DTOs
- Common validation:
  - `@Notnull` , `@Positive`, `@Min(0)`, `@Email`
- Special requirements satisfied:
  - Publisher GET responses do not contain address.
  - BookBorrowing update DTO does not contain email.
  - Book request DTOs include `authorId`, `publisherId`, `categoryIds`.

## Business Rules
- Category delete:
  - Service method returns `String`.
  - If category has books, do not delete; return message: “Bu kategoriye ait kitap var. Bu kategori silinemedi.”
  - Controller returns 409 Conflict in such case, otherwise 200 OK.
- BookBorrowing:
  - Save (borrow): stock must be > 0, then decrement stock; returnDate is null; borrowingDate is now if omitted; transactional.
  - Update (return): set returnDate; increment stock; transactional; cannot return twice.

## Error Handling
- Global exception handler returns:
  - 400 with validation errors list for `MethodArgumentNotValidException`.
  - 404 for `NotFoundException`.
- Result envelope: `Result`/`ResultData<T>` with status, message, code, data.

## Pagination
- Cursor endpoints accept `page` and `pageSize` query params.
- Controllers map `Page<Entity>` to `Page<ResponseDTO>` and wrap in `CursorResponse`.

## Endpoints (Full Table)

### Authors
- POST `/v1/authors` — 201 Created — Body: `AuthorSaveRequest` → `AuthorResponse`
- GET `/v1/authors/{id}` — 200 OK → `AuthorResponse`
- GET `/v1/authors?page=&pageSize=` — 200 OK → `CursorResponse<AuthorResponse>`
- PUT `/v1/authors` — 200 OK — Body: `AuthorUpdateRequest` → `AuthorResponse`
- DELETE `/v1/authors/{id}` — 200 OK

### Publishers
- POST `/v1/publishers` — 201 Created — Body: `PublisherSaveRequest` → `PublisherResponse` (address excluded)
- GET `/v1/publishers/{id}` — 200 OK → `PublisherResponse` (address excluded)
- GET `/v1/publishers?page=&pageSize=` — 200 OK → `CursorResponse<PublisherResponse>`
- PUT `/v1/publishers` — 200 OK — Body: `PublisherUpdateRequest` → `PublisherResponse`
- DELETE `/v1/publishers/{id}` — 200 OK

### Categories
- POST `/v1/categories` — 201 Created — Body: `CategorySaveRequest` → `CategoryResponse`
- GET `/v1/categories/{id}` — 200 OK → `CategoryResponse`
- GET `/v1/categories?page=&pageSize=` — 200 OK → `CursorResponse<CategoryResponse>`
- PUT `/v1/categories` — 200 OK — Body: `CategoryUpdateRequest` → `CategoryResponse`
- DELETE `/v1/categories/{id}` — 200 OK or 409 Conflict → `ResultData<String>`

### Books
- POST `/v1/books` — 201 Created — Body: `BookSaveRequest` (authorId, publisherId, categoryIds) → `BookResponse`
- GET `/v1/books/{id}` — 200 OK → `BookResponse`
- GET `/v1/books?page=&pageSize=` — 200 OK → `CursorResponse<BookResponse>`
- PUT `/v1/books` — 200 OK — Body: `BookUpdateRequest` → `BookResponse`
- DELETE `/v1/books/{id}` — 200 OK

### Book Borrowing
- POST `/v1/bookborrowing` — 201 Created — Body: `BookBorrowSaveRequest` (borrowerName, borrowerEmail, borrowingDate?, bookId)
  - Business: stock > 0 required; stock--
  - Response: `BookBorrowResponse` (returnDate is null on creation)
- GET `/v1/bookborrowing/{id}` — 200 OK → `BookBorrowResponse`
- GET `/v1/bookborrowing?page=&pageSize=` — 200 OK → `CursorResponse<BookBorrowResponse>`
- PUT `/v1/bookborrowing` — 200 OK — Body: `BookBorrowUpdateRequest` (id, returnDate)
  - Business: cannot return twice; on return, stock++
  - Response: `BookBorrowResponse`
- DELETE `/v1/bookborrowing/{id}` — 200 OK

## Sample Requests

Create Book
```json
POST /v1/books
{
  "bookName": "Les Misérables",
  "publicationYear": 1990,
  "stock": 10,
  "authorId": 3,
  "publisherId": 2,
  "categoryIds": [1, 5]
}
```

Borrow a Book
```json
POST /v1/bookborrowing
{
  "borrowerName": "Bekir",
  "borrowerEmail": "bekir@example.com",
  "borrowingDate": "2025-10-19",
  "bookId": 8
}
```

Return a Book
```json
PUT /v1/bookborrowing
{
  "id": 4,
  "returnDate": "2025-10-20"
}
```

---

# Kütüphane Sistemi REST API

Spring Boot ile geliştirilmiş, Kütüphane Sistemi için CRUD işlemleri yapan katmanlı bir REST API. Kitap, Yazar, Kategori, Yayınevi ve Kitap Ödünç Alma (BookBorrowing) işlemlerini kapsar. DTO kullanımı, validasyon, iş kuralları, sayfalama ve HTTP durum kodları doğru şekilde ele alınır.

## Proje Özeti
- Yazar, yayınevi, kategori ve kitap yönetimi.
- Kitap ödünç alma ve iade; stok takibi.
- Özel iş kuralları:
  - Kategori silmede: kategoriye bağlı kitap varsa silme ve mesaj döndür.
  - Ödünç almada stok > 0 kontrolü; iade işleminde returnDate set + stok artır.
  - Yayınevi GET cevaplarında adres dönme.
  - BookBorrowing update sırasında e‑mail alanı kabul edilmez.

## Mimari
- `entities/` JPA varlıkları
- `dao/` repository’ler (Spring Data JPA)
- `business/` service arayüz/implementasyonları
- `api/` controller’lar
- `dto/` request/response DTO’lar
- `core/` modelmapper, exception handler, result yardımcıları

## Teknolojiler
- Java 21, Spring Boot (Web, JPA, Validation), Lombok, PostgreSQL, ModelMapper

## Kurulum ve Çalıştırma
- `application.properties` içinde PostgreSQL ayarları:
  - `spring.datasource.url=jdbc:postgresql://localhost:5432/library_system_rest_api`
  - `spring.datasource.username=postgres`, `spring.datasource.password=your_password`
- Çalıştırma: `mvnw spring-boot:run` (Windows: `mvnw.cmd spring-boot:run`)

## Entity ve İlişkiler
- Book, Author, Category, Publisher, BookBorrowing
- İlişkiler UML’e uygun:
  - Book–Author (ManyToOne), Book–Publisher (ManyToOne), Book–Category (ManyToMany), Book–BookBorrowing (OneToMany)

## DTO ve Validasyon
- Controller’larda Request/Response DTO kullanımı
- Validasyon: `@Notnull`, `@Positive`, `@Min(0)`, `@Email`
- Özel şartlar:
  - Publisher GET response’unda adres yok
  - BookBorrowing update DTO’sunda email yok
  - Book request DTO’larında `authorId/publisherId/categoryIds`

## İş Kuralları
- Kategori silme:
  - Service: `String delete(int id)`
  - Kitap varsa silme; “Bu kategoriye ait kitap var. Bu kategori silinemedi.” mesajı dön
  - Controller 409/200 döndürür
- BookBorrowing:
  - Ödünç (save): `stock > 0` ve `stock--`, `returnDate = null`, `borrowingDate = now if null`, `@Transactional`
  - İade (update): `returnDate` set, `stock++`, ikinci kez iade engellenir, `@Transactional`

## Hata Yönetimi
- GlobalExceptionHandler:
  - Validasyon hataları -> 400
  - NotFound -> 404
- `Result`/`ResultData<T>` ile standart yanıt yapısı

## Sayfalama
- `page`, `pageSize` parametreleri
- `Page<Entity>` -> `Page<ResponseDTO>` map ve `CursorResponse` ile sarmalama

## Endpointler
### Authors
- POST `/v1/authors` — 201 Created — Body: `AuthorSaveRequest` → `AuthorResponse`
- GET `/v1/authors/{id}` — 200 OK → `AuthorResponse`
- GET `/v1/authors?page=&pageSize=` — 200 OK → `CursorResponse<AuthorResponse>`
- PUT `/v1/authors` — 200 OK — Body: `AuthorUpdateRequest` → `AuthorResponse`
- DELETE `/v1/authors/{id}` — 200 OK

### Publishers
- POST `/v1/publishers` — 201 Created — Body: `PublisherSaveRequest` → `PublisherResponse` (address excluded)
- GET `/v1/publishers/{id}` — 200 OK → `PublisherResponse` (address excluded)
- GET `/v1/publishers?page=&pageSize=` — 200 OK → `CursorResponse<PublisherResponse>`
- PUT `/v1/publishers` — 200 OK — Body: `PublisherUpdateRequest` → `PublisherResponse`
- DELETE `/v1/publishers/{id}` — 200 OK

### Categories
- POST `/v1/categories` — 201 Created — Body: `CategorySaveRequest` → `CategoryResponse`
- GET `/v1/categories/{id}` — 200 OK → `CategoryResponse`
- GET `/v1/categories?page=&pageSize=` — 200 OK → `CursorResponse<CategoryResponse>`
- PUT `/v1/categories` — 200 OK — Body: `CategoryUpdateRequest` → `CategoryResponse`
- DELETE `/v1/categories/{id}` — 200 OK or 409 Conflict → `ResultData<String>`

### Books
- POST `/v1/books` — 201 Created — Body: `BookSaveRequest` (authorId, publisherId, categoryIds) → `BookResponse`
- GET `/v1/books/{id}` — 200 OK → `BookResponse`
- GET `/v1/books?page=&pageSize=` — 200 OK → `CursorResponse<BookResponse>`
- PUT `/v1/books` — 200 OK — Body: `BookUpdateRequest` → `BookResponse`
- DELETE `/v1/books/{id}` — 200 OK

### Book Borrowing
- POST `/v1/bookborrowing` — 201 Created — Body: `BookBorrowSaveRequest` (borrowerName, borrowerEmail, borrowingDate?, bookId)
  - Business: stock > 0 required; stock--
  - Response: `BookBorrowResponse` (returnDate is null on creation)
- GET `/v1/bookborrowing/{id}` — 200 OK → `BookBorrowResponse`
- GET `/v1/bookborrowing?page=&pageSize=` — 200 OK → `CursorResponse<BookBorrowResponse>`
- PUT `/v1/bookborrowing` — 200 OK — Body: `BookBorrowUpdateRequest` (id, returnDate)
  - Business: cannot return twice; on return, stock++
  - Response: `BookBorrowResponse`
- DELETE `/v1/bookborrowing/{id}` — 200 OK

## Örnek İstekler
Create Book
```json
POST /v1/books
{
  "bookName": "Les Misérables",
  "publicationYear": 1990,
  "stock": 10,
  "authorId": 3,
  "publisherId": 2,
  "categoryIds": [1, 5]
}
```

Borrow a Book
```json
POST /v1/bookborrowing
{
  "borrowerName": "Bekir",
  "borrowerEmail": "bekir@example.com",
  "borrowingDate": "2025-10-19",
  "bookId": 8
}
```

Return a Book
```json
PUT /v1/bookborrowing
{
  "id": 4,
  "returnDate": "2025-10-20"
}
```
