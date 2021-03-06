package com.exec.business.controller

import com.exec.business.dao.entity.BusinessPlanEntity
import com.exec.business.dao.entity.util.PlanStep
import com.exec.business.dao.service.BusinessPlanService
import com.exec.business.util.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

/**
 * Author: Vadym Polyanski
 * Date: 25.10.17
 * Time: 11:42
 */

@Controller
@RequestMapping("/business-planer")
class MvcController {

    @Autowired
    private lateinit var businessPlanService: BusinessPlanService
    @Autowired
    private lateinit var mapper: Mapper


    @GetMapping("/main")
    fun mainPage(model: Model): String {
        val planSteps = PlanStep.values()

        model.addAttribute("planSteps", planSteps)

        return "index"
    }

    @GetMapping("/business/{id}")
    fun businessPage(
            @PathVariable id: String,
            model: Model): String {

        var businessPlan: BusinessPlanEntity? = businessPlanService.getById(id) ?: return "redirect: /main"

        model.addAttribute("plan", mapper.mapBusinessPlan(businessPlan!!))

        return "business"
    }

    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }

    @GetMapping("/register")
    fun registerPage(): String {
        return "register"
    }
}
