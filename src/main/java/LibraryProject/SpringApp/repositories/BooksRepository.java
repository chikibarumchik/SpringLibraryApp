package LibraryProject.SpringApp.repositories;

import LibraryProject.SpringApp.models.Book;
import LibraryProject.SpringApp.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByOwner(Person person);

    List<Book> findByNameStartingWith(String startingWith);
}
