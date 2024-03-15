package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.common.DataNotFoundException;
import com.example.demo.common.FlashData;
import com.example.demo.service.AuthorService;
import com.example.demo.service.BookService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/authors")
public class AuthorsController {
	@Autowired
	AuthorService authorService;

	@Autowired
	BookService bookService;

	@GetMapping
	public String list(Model model) {
		model.addAttribute("authors", authorService.findAll());
		return "authors/list";
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, RedirectAttributes ra) {
    		FlashData flash;
    		try {
        		List<Book> books = bookService.findByCategoryId(id);
        		if (books.isEmpty()) {
            			authorService.findById(id);
            			authorService.delete(id);
            			flash = new FlashData().success("著者の削除が完了しました");
        		} else {
            			flash = new FlashData().danger("書籍に登録されている著者は削除できません");
        		}
    		} catch (DataNotFoundException e) {
        		flash = new FlashData().danger("該当データがありません");
    		} catch (Exception e) {
        		flash = new FlashData().danger("処理中にエラーが発生しました");
    		}
    		ra.addFlashAttribute("flash", flash);
    		return "redirect:/authors";
	}
}
