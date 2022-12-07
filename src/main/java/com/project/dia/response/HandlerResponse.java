package com.project.dia.response;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;

public class HandlerResponse {

    private static final String STATUS_SUCCESS = "SUCCESS";
    private static final String STATUS_FAILED = "FAILED";

    private static void responseWriter(HttpServletResponse response, Object data, Integer code){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String finalResponse = objectMapper.writeValueAsString(data);

            response.setStatus(code);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(finalResponse);
            response.getWriter().flush();
        } catch (Exception e) {
            responseInternalServerError(response, e.getMessage().toUpperCase());
            throw new RuntimeException(e);
        }
    }

    public static void responseSuccessOK(HttpServletResponse response, String message){
        if (message.isEmpty()){
            message = "SUCCESS";
        }

        SuccessResponse<String> successResponse = new SuccessResponse<>();
        successResponse.setCode(HttpServletResponse.SC_OK);
        successResponse.setStatus(STATUS_SUCCESS);
        successResponse.setMessage(message);

        responseWriter(response, successResponse, successResponse.getCode());
    }

    public static void responseSuccessWithData(HttpServletResponse response, DataResponse<?> data){
        data.setStatus(STATUS_SUCCESS);
        data.setCode(HttpServletResponse.SC_OK);
        responseWriter(response, data, data.getCode());
    }

    public static void responseSuccessCreated(HttpServletResponse response, String message){
        if (message.isEmpty()){
            message = "CREATED";
        }

        SuccessResponse<String> successResponse = new SuccessResponse<>();
        successResponse.setCode(HttpServletResponse.SC_CREATED);
        successResponse.setStatus(STATUS_SUCCESS);
        successResponse.setMessage(message);

        responseWriter(response, successResponse, successResponse.getCode());
    }

    public static void responseBadRequest(HttpServletResponse response, String errorCode, String error){
        if (error.isEmpty()){
            error = "BAD REQUEST";
        }

        ErrorResponse<String> errorResponse = new ErrorResponse<>();
        errorResponse.setCode(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setStatus(STATUS_FAILED);
        errorResponse.setErrorCode(errorCode);
        errorResponse.setMessage(error);

        responseWriter(response, errorResponse, errorResponse.getCode());
    }

    public static void responseUnauthorized(HttpServletResponse response, String error){
        if (error.isEmpty()){
            error = "UNAUTHORIZED";
        }
        ErrorResponse<String> errorResponse = new ErrorResponse<>();

        errorResponse.setCode(HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.setStatus(STATUS_FAILED);
        errorResponse.setErrorCode("401");
        errorResponse.setMessage(error);

        responseWriter(response, errorResponse, errorResponse.getCode());
    }

    public static void responseInternalServerError(HttpServletResponse response, String error) {
        if (error.isEmpty()){
            error = "INTERNAL SERVER ERROR";
        }

        ErrorResponse<String> errorResponse = new ErrorResponse<>();

        errorResponse.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        errorResponse.setStatus(STATUS_FAILED);
        errorResponse.setMessage(error);

        responseWriter(response, errorResponse, errorResponse.getCode());
    }

    public static void responseInternalServerErrorWithData(HttpServletResponse response, DataResponse<?> data){
        data.setStatus(STATUS_FAILED);
        data.setCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        responseWriter(response, data, data.getCode());
    }

    public static void responseGateway(HttpServletResponse response, String error){
        if (error.isEmpty()){
            error = "BAD GATEWAY";
        }

        ErrorResponse<String> errorResponse = new ErrorResponse<>();

        errorResponse.setCode(HttpServletResponse.SC_BAD_REQUEST);
        errorResponse.setStatus(STATUS_FAILED);
        errorResponse.setMessage(error);

        responseWriter(response, errorResponse, errorResponse.getCode());
    }
}