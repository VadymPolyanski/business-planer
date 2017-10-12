package com.exec.business.handler.business

import com.exec.business.dao.entity.UserEntity
import com.exec.business.protocol.GetBusinessPlansRequest
import com.exec.business.protocol.GetBusinessPlansResponse
import com.exec.business.util.Mapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.streams.toList

/**
 * Author: Vadym Polyanski;
 * Date: 21.09.17;
 * Time: 8:37.
 */
@Component
open class GetBusinessesHandler : BusinessHandler<GetBusinessPlansRequest, GetBusinessPlansResponse>() {

    @Autowired
    private lateinit var mapper: Mapper

    override fun handle(request: GetBusinessPlansRequest): GetBusinessPlansResponse {
        val user: UserEntity = getUser(request.rotingData.credentials!!.id)

        val businesses = businessService.findByOwner(user.id!!).stream()
                .map { business -> mapper.mapBusiness(business) }.toList()

        return GetBusinessPlansResponse(businesses)
    }
}