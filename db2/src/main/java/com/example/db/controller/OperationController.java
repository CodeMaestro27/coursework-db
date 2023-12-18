package com.example.db.controller;

import com.example.db.domain.Article;
import com.example.db.repos.ArticleRepository;
import com.example.db.domain.Balance;
import com.example.db.repos.BalanceRepository;
import com.example.db.model.OperationDTO;
import com.example.db.service.OperationService;
import com.example.db.util.CustomCollectors;
import com.example.db.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/operations")
public class OperationController {

    private final OperationService operationService;
    private final BalanceRepository balanceRepository;
    private final ArticleRepository articleRepository;

    public OperationController(final OperationService operationService,
            final BalanceRepository balanceRepository, final ArticleRepository articleRepository) {
        this.operationService = operationService;
        this.balanceRepository = balanceRepository;
        this.articleRepository = articleRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("balanceValues", balanceRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Balance::getId, Balance::getId)));
        model.addAttribute("articleValues", articleRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Article::getId, Article::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("operations", operationService.findAll());
        return "operation/list";
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    public String add(@ModelAttribute("operation") final OperationDTO operationDTO) {
        return "operation/add";
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String add(@ModelAttribute("operation") @Valid final OperationDTO operationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "operation/add";
        }
        operationService.create(operationDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("operation.create.success"));
        return "redirect:/operations";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("operation", operationService.get(id));
        return "operation/edit";
    }

    @PostMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("operation") @Valid final OperationDTO operationDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "operation/edit";
        }
        try {
            operationService.update(id, operationDTO);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("operation.delete.error"));
            return "redirect:/operations";
        }
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("operation.update.success"));
        return "redirect:/operations";
    }

    @PostMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        try {
            operationService.delete(id);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("operation.delete.error"));
            return "redirect:/operations";
        }
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("operation.delete.success"));
        return "redirect:/operations";
    }

}
