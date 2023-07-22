package LibraryProject.SpringApp.controllers;


import LibraryProject.SpringApp.models.Book;
import LibraryProject.SpringApp.models.Person;
import LibraryProject.SpringApp.services.BooksService;
import LibraryProject.SpringApp.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;

    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService bookDAO, PeopleService peopleService) {

        this.booksService = bookDAO;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(value = "page" ,required = false) String page,
                        @RequestParam(value = "books_per_page", required = false) String booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) boolean sort,
                        Model model) {
        if (!sort && page != null && booksPerPage != null) {
            model.addAttribute("books", booksService.findAll(page, booksPerPage));
        } else if(sort && page == null && booksPerPage == null) {
            model.addAttribute("books", booksService.findAll(Sort.by("year")));
        } else if (sort && page != null && booksPerPage != null) {
            model.addAttribute("books", booksService.findAll(page, booksPerPage, sort));

        }
        else {
            model.addAttribute("books", booksService.findAll());
        }
        return "books/index";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }

        booksService.save(book);

        return "redirect:/books" ;
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") int id,
                       Model model) {
        model.addAttribute("book", booksService.findOne(id));

        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "books/edit";
        }

        booksService.update(id, book);

        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id,
                       Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));

        Person bookOwner = booksService.getBookOwner(id);

        if (bookOwner != null) {
            model.addAttribute("owner", bookOwner);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }

        return "books/show";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        booksService.release(id);

        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id,
                         @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(id, selectedPerson);

        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String searchBooks() {
        return "books/search";
    }

    @PostMapping("/search")
    public String makeSearch(Model model,
                             @RequestParam("query") String query) {
        model.addAttribute("books", booksService.findByNameStartingWith(query));
        return "books/search";
    }


}
