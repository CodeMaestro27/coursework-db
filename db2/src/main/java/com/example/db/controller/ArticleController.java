package com.example.db.controller;

import com.example.db.model.ArticleDTO;
import com.example.db.service.ArticleService;
import com.example.db.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(final ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "article/list";
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    public String add(@ModelAttribute("article") final ArticleDTO articleDTO) {
        return "article/add";
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String add(@ModelAttribute("article") @Valid final ArticleDTO articleDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "article/add";
        }
        articleService.create(articleDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("article.create.success"));
        return "redirect:/articles";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("article", articleService.get(id));
        return "article/edit";
    }

    @PostMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("article") @Valid final ArticleDTO articleDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "article/edit";
        }
        articleService.update(id, articleDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("article.update.success"));
        return "redirect:/articles";
    }

    @PostMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = articleService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            articleService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("article.delete.success"));
        }
        return "redirect:/articles";
    }

}
