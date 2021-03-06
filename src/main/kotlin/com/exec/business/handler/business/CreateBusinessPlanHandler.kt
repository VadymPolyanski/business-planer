package com.exec.business.handler.business

import com.exec.business.dao.entity.QuestionEntity
import com.exec.business.dao.entity.UserEntity
import com.exec.business.dao.service.QuestionService
import com.exec.business.protocol.CreateBusinessPlanRequest
import com.exec.business.protocol.CreateBusinessPlanResponse
import com.exec.business.util.DeadLineNotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.streams.toList

/**
 * Author: Vadym Polyanski;
 * Date: 12.10.17;
 * Time: 22:27.
 */
@Component
open class CreateBusinessPlanHandler : BusinessPlanHandler<CreateBusinessPlanRequest, CreateBusinessPlanResponse>() {

    @Autowired
    private lateinit var notificationService: DeadLineNotificationService
    @Autowired
    private lateinit var questionService: QuestionService

    override fun handle(request: CreateBusinessPlanRequest): CreateBusinessPlanResponse {
        val user: UserEntity = getUser(request.rotingData!!.credentials!!.id)

        val businessPlan = mapper.revertBusinessPlan(request.businessPlan)

        businessPlan.owner = user

        businessPlanService.save(businessPlan)

        val questions = businessPlan.questions!!

        questions.stream().forEach { it.businessPlan = businessPlan}
        questions.stream().forEach {questionService::save}

        addQuestionsToDeadlineQueue(questions, user)


        LOG.info("User ${user.lastName}(${user.email}) created new business plan (plan id - ${businessPlan.id}).")
        return CreateBusinessPlanResponse()
    }

    private fun addQuestionsToDeadlineQueue(questions: List<QuestionEntity>, user: UserEntity) {
        for (question in questions) {
            notificationService.addNotificationTask(question, user)
        }
    }

}