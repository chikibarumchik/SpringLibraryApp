package LibraryProject.SpringApp.services;

import LibraryProject.SpringApp.models.Book;
import LibraryProject.SpringApp.models.Person;
import LibraryProject.SpringApp.repositories.BooksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        booksRepository.save(updateBook);
    }

    public Book findOne(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return booksRepository.findById(id).orElse(null).getOwner();
    }

    @Transactional
    public void release(int id) {
        Book book = booksRepository.findById(id).get();

        if(book.getOwner().getBooks() == null) {
            book.getOwner().setBooks(new ArrayList<>());
        }

        book.getOwner().getBooks().remove(book);
        book.setOwner(null);
    }

    @Transactional
    public void assign(int id, Person person) {
        Book book = booksRepository.findById(id).get();
        book.setAssignAt(new Date());

        if(person.getBooks() == null) {
            person.setBooks(new ArrayList<>());
        }

        person.getBooks().add(book);
        book.setOwner(person);
    }

    public List<Book> findAll(String page, String booksPerPage) {
        return booksRepository.findAll(PageRequest.of(Integer.parseInt(page), Integer.parseInt(booksPerPage)))
                .getContent();
    }

    public List<Book> findAll(Sort sort) {
        return booksRepository.findAll(sort);
    }

    public List<Book> findAll(String page, String booksPerPage, boolean sort) {
        return booksRepository.findAll(PageRequest.of(Integer.parseInt(page),
                        Integer.parseInt(booksPerPage),
                        Sort.by("year")))
                .getContent();
    }

    public List<Book> findByNameStartingWith(String start) {
        return booksRepository.findByNameStartingWith(start);
    }

}
