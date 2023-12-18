package com.example.db.controller;

import com.example.db.model.BalanceDTO;
import com.example.db.service.BalanceService;
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
@RequestMapping("/balances")
public class BalanceController {

    private final BalanceService balanceService;

    public BalanceController(final BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("balances", balanceService.findAll());
        return "balance/list";
    }

    @GetMapping("/add")
    @Secured("ROLE_ADMIN")
    public String add(@ModelAttribute("balance") final BalanceDTO balanceDTO) {
        return "balance/add";
    }

    @PostMapping("/add")
    @Secured("ROLE_ADMIN")
    public String add(@ModelAttribute("balance") @Valid final BalanceDTO balanceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "balance/add";
        }
        try {
            balanceService.create(balanceDTO);
        } catch (Exception e){
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("balance.create.error"));
            return "redirect:/balances";
        }
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("balance.create.success"));
        return "redirect:/balances";
    }

    @GetMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String edit(@PathVariable(name = "id") final Integer id, final Model model) {
        model.addAttribute("balance", balanceService.get(id));
        return "balance/edit";
    }

    @PostMapping("/edit/{id}")
    @Secured("ROLE_ADMIN")
    public String edit(@PathVariable(name = "id") final Integer id,
            @ModelAttribute("balance") @Valid final BalanceDTO balanceDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "balance/edit";
        }
        balanceService.update(id, balanceDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("balance.update.success"));
        return "redirect:/balances";
    }

    @PostMapping("/delete/{id}")
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable(name = "id") final Integer id,
            final RedirectAttributes redirectAttributes) {
        final String referencedWarning = balanceService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            balanceService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("balance.delete.success"));
        }
        return "redirect:/balances";
    }

}
