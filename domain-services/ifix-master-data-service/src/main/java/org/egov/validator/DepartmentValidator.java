package org.egov.validator;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.egov.common.contract.request.RequestHeader;
import org.egov.tracer.model.CustomException;
import org.egov.web.models.DepartmentSearchCriteria;
import org.egov.web.models.DepartmentSearchRequest;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class DepartmentValidator {


    /**
     * Validate the department search attribute(s)
     *
     * @param searchRequest
     */
    public void validateSearchPost(DepartmentSearchRequest searchRequest) {
        log.info("Enter into DepartmentValidator.validateSearchPost()");
        DepartmentSearchCriteria searchCriteria = searchRequest.getCriteria();
        RequestHeader requestHeader = searchRequest.getRequestHeader();
        Map<String, String> errorMap = new HashMap<>();

        //Header validation
        if (requestHeader == null) {
            throw new CustomException("REQUEST_HEADER", "Request header is missing");
        }
        if (requestHeader.getUserInfo() == null || requestHeader.getUserInfo().getUuid() == null) {
            errorMap.put("USER_INFO", "User info is missing");
        }

        if (searchCriteria == null) {
            throw new CustomException("INVALID_SEARCH_CRITERIA", "Search criteria is missing");
        }


        //Tenant id validation
        if (StringUtils.isBlank(searchCriteria.getTenantId())) {
            throw new CustomException("TENANT_ID", "Tenant id is mandatory");
        }
        if (StringUtils.isNotBlank(searchCriteria.getTenantId())
                && (searchCriteria.getTenantId().length() < 2 || searchCriteria.getTenantId().length() > 64))
            errorMap.put("TENANT_ID_LENGTH", "Tenant id's length is invalid");

        //name
        if (StringUtils.isNotBlank(searchCriteria.getName())
                && (searchCriteria.getName().length() < 2 || searchCriteria.getName().length() > 256))
            errorMap.put("DEPARTMENT_NAME", "Department name's length is invalid");

        //code
        if (StringUtils.isNotBlank(searchCriteria.getCode())
                && (searchCriteria.getCode().length() < 2 || searchCriteria.getCode().length() > 64))
            errorMap.put("DEPARTMENT_CODE", "Department code's length is invalid");

        log.info("Exit from DepartmentValidator.validateSearchPost()");
        if (!errorMap.isEmpty())
            throw new CustomException(errorMap);
    }
}