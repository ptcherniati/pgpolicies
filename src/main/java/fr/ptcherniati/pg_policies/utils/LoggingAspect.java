package fr.ptcherniati.pg_policies.utils;

import fr.ptcherniati.pg_policies.dao.PgPoliciesDAO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Autowired
    private PgPoliciesDAO pgPoliciesDAO;


    /*@Before("execution(org.springframework.data.jpa.repository.JpaRepository)")
    public void changeRoleToUser(JoinPoint joinPoint) throws Throwable {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("changeRoleToUser : Connected user "+user+"; "+joinPoint.getSignature().getName());
        pgPoliciesDAO.setUser(user);
    }*/
}
