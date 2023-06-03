package com.example.minitest1.controller;

import com.example.minitest1.model.Articles;
import com.example.minitest1.service.IArticlesService;
import groovyjarjarpicocli.CommandLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.jws.Oneway;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("list")
public class ArticlesController {
    @Autowired
    private IArticlesService articlesService;


    @GetMapping("")
    private ModelAndView showAll(){
        List<Articles> articles = articlesService.findAll();
        ModelAndView modelAndView = new ModelAndView("view");
        modelAndView.addObject("articles", articles);
        return modelAndView;
    }
    @GetMapping("/{id}")
    private ModelAndView showInfo(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("info");
        Articles article = articlesService.findById(id);
        modelAndView.addObject("article",article);
        return modelAndView;
    }
    @GetMapping("/create")
    private ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("create");
        modelAndView.addObject("article", new Articles());
        return modelAndView;
    }
    @PostMapping("/create")
    private ModelAndView save(@RequestParam String title, @RequestParam String content, @RequestParam String created_date, RedirectAttributes redirectAttributes) {
        if (title != null && !title.isEmpty() && content != null && !content.isEmpty()) {

            int id = (int) (Math.random() * 1000);
            Articles article = new Articles(id, title, content, LocalDate.parse(created_date));
            articlesService.save(article);

            ModelAndView modelAndView = new ModelAndView("redirect:/list");
            redirectAttributes.addFlashAttribute("success", "thêm mới thành công");
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("create");
            redirectAttributes.addFlashAttribute("errorMessage", "thêm mới thất bại");
            return modelAndView;
        }
    }
    @GetMapping("/edit/{id}")
    private ModelAndView edit(@PathVariable int id) {
        ModelAndView modelAndView = new ModelAndView("edit");
        Articles article = articlesService.findById(id);
        modelAndView.addObject("article", article);
        modelAndView.addObject("title", article.getTitle()); // Định nghĩa giá trị cho thuộc tính 'title'
        modelAndView.addObject("content", article.getContent()); // Định nghĩa giá trị cho thuộc tính 'content'
        modelAndView.addObject("created_date", article.getCreated_date().toString()); // Định nghĩa giá trị cho thuộc tính 'created_date'
        return modelAndView;
    }
    @PostMapping("/edit/{id}")
    private ModelAndView update(@PathVariable int id,@RequestParam String title, @RequestParam String content, @RequestParam String created_date,  RedirectAttributes redirectAttributes){
        Articles article = articlesService.findById(id);
        article.setTitle(title);
        article.setContent(content);
        article.setCreated_date(LocalDate.parse(created_date));
        articlesService.edit(id,article);
        ModelAndView modelAndView = new ModelAndView("redirect:/list");
        redirectAttributes.addFlashAttribute("success", "sửa thành công");
        return modelAndView;
    }
    @PostMapping("/delete")
    private ModelAndView delete(@RequestParam int id, RedirectAttributes redirectAttributes) {
        articlesService.remote(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/list");
        redirectAttributes.addFlashAttribute("success", "Xóa thành công");
        return modelAndView;
    }
}
