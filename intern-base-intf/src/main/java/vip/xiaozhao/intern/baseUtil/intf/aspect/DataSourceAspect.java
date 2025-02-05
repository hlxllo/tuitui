package vip.xiaozhao.intern.baseUtil.intf.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly;
import vip.xiaozhao.intern.baseUtil.intf.config.DynamicDataSource;
import vip.xiaozhao.intern.baseUtil.intf.constant.SlaveConstant;

import java.lang.reflect.Method;
import java.util.Random;

@Aspect
@Component
public class DataSourceAspect {

    // 针对mapper包中的方法，判断是否有@ReadOnly注解
    //@Before("execution(* vip.xiaozhao.intern.baseUtil.intf.mapper.*.*(..)) && @annotation(vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly)")
    //public void setReadOnlyDataSource() {
    //    // 随机选择一个从库
    //    Random random = new Random();
    //    int randomIndex = random.nextInt(SlaveConstant.slaveNum);
    //    String selectedSlave = "slave" + (randomIndex + 1);
    //
    //    // 设置为从库
    //    System.out.println("Setting to read-only slave: " + selectedSlave);
    //    DynamicDataSource.setDataSourceType(selectedSlave);
    //}
    //
    //// 针对mapper包中的方法，且没有@ReadOnly注解的方法
    //@Before("execution(* vip.xiaozhao.intern.baseUtil.intf.mapper.*.*(..)) && !@annotation(vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly)")
    //public void setWriteDataSource() {
    //    // 设置为主库
    //    System.out.println("Setting to master data source");
    //    DynamicDataSource.setDataSourceType("master");
    //}
    //
    //// 清除数据源配置（针对ReadOnly）
    //@After("execution(* vip.xiaozhao.intern.baseUtil.intf.mapper.*.*(..)) && @annotation(vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly)")
    //public void clearDataSource() {
    //    System.out.println("Clearing data source type after read-only operation");
    //    DynamicDataSource.clearDataSourceType();
    //}
    //
    //// 清除数据源配置（针对非ReadOnly）
    //@After("execution(* vip.xiaozhao.intern.baseUtil.intf.mapper.*.*(..)) && !@annotation(vip.xiaozhao.intern.baseUtil.intf.annotation.ReadOnly)")
    //public void clearDataSourceAfterWrite() {
    //    System.out.println("Clearing data source type after write operation");
    //    DynamicDataSource.clearDataSourceType();
    //}

    @Before("execution(* vip.xiaozhao.intern.baseUtil.intf.mapper.*.*(..))")
    public void setDataSource(JoinPoint joinPoint) {
        // 判断方法是否有 @ReadOnly 注解
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method.isAnnotationPresent(ReadOnly.class)) {
            // 随机选择一个从库
            Random random = new Random();
            int randomIndex = random.nextInt(SlaveConstant.slaveNum);
            String selectedSlave = "slave" + (randomIndex + 1);

            System.out.println("Setting to read-only slave: " + selectedSlave);
            DynamicDataSource.setDataSourceType(selectedSlave);
        } else {
            // 切换到主库
            System.out.println("Setting to master data source");
            DynamicDataSource.setDataSourceType("master");
        }
    }

    @After("execution(* vip.xiaozhao.intern.baseUtil.intf.mapper.*.*(..))")
    public void clearDataSource(JoinPoint joinPoint) {
        DynamicDataSource.clearDataSourceType();
    }

}
