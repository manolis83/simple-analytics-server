package com.example.simpleanalytics.controller;

import com.example.simpleanalytics.persistence.AnalyticsRepository;
import com.example.simpleanalytics.api.Query;
import com.example.simpleanalytics.api.Response;
import com.example.simpleanalytics.api.validation.ValidationResult;
import com.example.simpleanalytics.api.validation.Validator;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class AnalyticsController {
    private final AnalyticsRepository repository;
    private final Validator<Query> validator;

    public AnalyticsController(AnalyticsRepository repository, Validator<Query> validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @RequestMapping(
            value =
                    "analytics"
            ,
            method = RequestMethod.POST,
            produces="application/json",
            consumes="application/json"
    )
    public Response retrieveAnalytics(@RequestBody Query query) {
        try {
            ValidationResult validationResult = validator.validate(query);
            if (!validationResult.isValid()) {
                return Response.withError(validationResult.getError());
            }

            return Response.withData(repository.retrieve(query));
        } catch (DataAccessException e) {
            return Response.withError(e.getCause().getCause().getLocalizedMessage());
        } catch (Exception e) {
            return Response.withError(e.getLocalizedMessage());
        }
    }
}
