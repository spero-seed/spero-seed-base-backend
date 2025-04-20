package com.speroseed.web.handler;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import com.speroseed.core.exception.BusinessException;
import com.speroseed.core.model.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @description 全局异常拦截
 * @author zfq
 * @date 2025/4/17 23:40
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求方式异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                                   HttpServletRequest request) {
        log.error("请求地址: {}, 不支持{}请求", request.getRequestURI(), e.getMethod(), e);
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 参数绑定异常
     * <p>
     * {@link org.springframework.web.bind.annotation.RequestParam}
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult<Object> missingServletRequestParameterException(MissingServletRequestParameterException e,
                                                              HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>(2);
        errors.put(e.getParameterName(), "is not present");
        log.error("请求地址: {}, 参数绑定异常: {}", request.getRequestURI(), errors, e);
        return AjaxResult.error(e.getParameterName() + "is not present");
    }

    /**
     * 参数验证异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult<Object> bindException(BindException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>(8);
        e.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        log.error("请求地址: {}, 参数验证异常: {}", request.getRequestURI(), errors, e);
        return AjaxResult.error(String.join(",", errors.values()));
    }

    /**
     * 参数验证异常
     * <p>
     * {@link org.springframework.web.bind.annotation.RequestBody}
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult methodArgumentNotValidException(MethodArgumentNotValidException e,
                                                      HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>(8);
        e.getBindingResult().getAllErrors().forEach(error -> {
            FieldError fieldError = (FieldError) error;
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        log.error("请求地址: {}, 参数验证异常: {}", request.getRequestURI(), errors, e);
        return AjaxResult.error(String.join(",", errors.values()));
    }

    /**
     * 参数验证异常
     * <p>
     * {@link org.springframework.web.bind.annotation.PathVariable}
     * {@link org.springframework.web.bind.annotation.RequestParam}
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult<Object> constraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>(8);
        e.getConstraintViolations().forEach(violation -> {
            PathImpl propertyPath = (PathImpl) violation.getPropertyPath();
            String field = propertyPath.getLeafNode().getName();
            errors.put(field, violation.getMessage());
        });
        log.error("请求地址: {}, 参数验证异常: {}", request.getRequestURI(), errors, e);
        return AjaxResult.error(String.join(",", errors.values()));
    }

    /**
     * 业务异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public AjaxResult<Object> businessException(BusinessException e, HttpServletRequest request) {
        Integer code = e.getCode();
        String message = e.getMessage();
        log.error("请求地址: {}, 发生异常: {}", request.getRequestURI(), message, e);
        return code != null ? AjaxResult.error(code, message) : AjaxResult.error(message);
    }

    /**
     * 运行时异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult<Object> runtimeException(RuntimeException e, HttpServletRequest request) {
        log.error("请求地址: {}, 发生异常: {}", request.getRequestURI(), e.getMessage(), e);
        return AjaxResult.error(e.getMessage());
    }


    /**
     * 所有异常
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResult<Object> exception(Exception e, HttpServletRequest request) {
        log.error("请求地址: {}, 发生异常: {}", request.getRequestURI(), e.getMessage(), e);
        return AjaxResult.error(e.getMessage());
    }
}
