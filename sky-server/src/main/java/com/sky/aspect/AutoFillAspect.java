package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.sky.constant.AutoFillConstant.SET_CREATE_TIME;
import static com.sky.constant.AutoFillConstant.SET_UPDATE_TIME;

/**
 * @author xuxunne
 * @description: User defined aspect  which is used to filled public field
 * @since 2024/12/24 17:46
 *
 * Note : @Aspect:
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    //Pointcut expression
    @Pointcut("execution(* com.sky.mapper.*.*(..) ) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){
        // notify
    }

    //前置通知
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("start  public autofill field");

        // get database operation which is  intercepted method currently.
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//get method signature object through joinPoint
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);// get Annotation object which in  method
        OperationType operationType = annotation.value();

        // get current entity which is intercepted method parameter
        Object[] args = joinPoint.getArgs();
        if (args == null ||  args.length == 0) {
            return;
        }

        Object entity = args[0];

        //get assign object field
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //  assign variable for different value according to current operation type
        if (operationType == OperationType.INSERT) {
            // through reflect to assign value
            try {
                Method setCreatTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // assign object value in reflecting
                setCreatTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (operationType == OperationType.UPDATE){
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                // assign object value in reflecting
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }}
