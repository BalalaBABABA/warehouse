package com.abc.warehouse.aspect;

import cn.hutool.json.JSONObject;
import com.abc.warehouse.dto.EncryotResult;
import com.abc.warehouse.dto.Result;
import com.abc.warehouse.annotation.Decrypt;
import com.abc.warehouse.annotation.Encrypt;
import com.abc.warehouse.handler.RepeatedlyReadRequestWrapper;
import com.abc.warehouse.utils.AesUtil;
import com.abc.warehouse.utils.RsaUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
/**
 * AES + RSA 加解密AOP处理
 */
@Aspect
@Component
public class EncryptedAspect {

    /**
     * Pointcut 切入点
     * 匹配com.abc.warehouse.controller包下面的所有方法
     */
    @Pointcut(value = "execution(public * com.abc.warehouse.controller.*.*(..))")
    public void safetyAspect() {
    }

    /**
     * 环绕通知
     */
    @Around(value = "safetyAspect()")
    public Object around(ProceedingJoinPoint pjp) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            assert attributes != null;
            //request对象
            HttpServletRequest request = attributes.getRequest();

            //http请求方法  post get
            String httpMethod = request.getMethod().toLowerCase();

            //method方法
            Method method = ((MethodSignature) pjp.getSignature()).getMethod();

            //method方法上面的注解
            Annotation[] annotations = method.getAnnotations();

            //方法的形参参数
            Object[] args = pjp.getArgs();


            MethodSignature signature = (MethodSignature) pjp.getSignature();

            //获取到请求参数
            Map<String, Object> fieldsName = getFieldsName(pjp);

            //前端公钥
            String publicKey = request.getHeader("PublicKey");

            //是否有@Decrypt
            boolean hasDecrypt = false;
            //是否有@Encrypt
            boolean hasEncrypt = false;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType() == Decrypt.class) {
                    hasDecrypt = true;
                }
                if (annotation.annotationType() == Encrypt.class) {
                    hasEncrypt = true;
                }
            }

            //jackson
            ObjectMapper mapper = new ObjectMapper();
            //jackson 序列化和反序列化 date处理
            mapper.setDateFormat( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            //执行方法之前解密，且只拦截post请求
            if ("post".equals(httpMethod)&&hasDecrypt) {

                StringBuilder buffer = new StringBuilder();
                RepeatedlyReadRequestWrapper requestWrapper = new RepeatedlyReadRequestWrapper(request);
                BufferedReader reader = requestWrapper.getReader();

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String Jsondata = buffer.toString();

                // 使用JSONObject解析JSON字符串
                JSONObject json = new JSONObject(Jsondata);

                // 获取每个键值对
                String data="";
                String aesKey="";

                if("post".equals(httpMethod)){
                    //AES加密后的数据
                    data = json.getStr("data");
                    //后端RSA公钥加密后的AES的key
                    aesKey = json.getStr("aesKey");
                    //前端公钥
                    publicKey = json.getStr("publicKey");
                }else{
                    //AES加密后的数据
                    data = request.getParameter("data");
                    //后端RSA公钥加密后的AES的key
                    aesKey = request.getParameter("aesKey");
                    //前端公钥
                    publicKey = request.getParameter("publicKey");
                }



                System.out.println("前端公钥：" + publicKey);

                //后端私钥解密的到AES的key
//                byte[] plaintext = RsaUtil.decryptByPrivateKey(Base64.decodeBase64(aesKey), RsaUtil.getPrivateKey());
//                byte[] plaintext = RsaUtil.decryptByPrivateKey(aesKey, RsaUtil.keyStore.getPrivateKey());
                aesKey = RsaUtil.decryptByPrivateKey(aesKey, RsaUtil.keyStore.getPrivateKey());
                System.out.println("解密出来的AES的key：" + aesKey);

                //AES解密得到明文data数据
                // TODO 解决，decrypt中断问题
                String decrypt = AesUtil.aesDecrypt(data, aesKey);
                System.out.println("解密出来的data数据：" + decrypt);

                //设置到方法的形参中，目前只能设置只有一个参数的情况
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

                if(args.length > 0){
                    args[0] = mapper.readValue(decrypt, args[0].getClass());
                }
            }

            //执行并替换最新形参参数   PS：这里有一个需要注意的地方，method方法必须是要public修饰的才能设置值，private的设置不了
            Object o = pjp.proceed(args);

            //返回结果之前加密
            if (hasEncrypt) {
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                //每次响应之前随机获取AES的key，加密data数据
                String key = AesUtil.getKey();
                System.out.println("AES的key：" + key);
                String dataString = mapper.writeValueAsString(o);
                System.out.println("需要加密的data数据：" + dataString);
                String data = AesUtil.aesEncrypt(dataString, key);
                System.out.println("加密后的data数据：" + data);
                //用前端的公钥来解密AES的key，并转成Base64
                //用前端的公钥来加密AES的key，并转成Base64
                String aesKey = RsaUtil.encryptByPublicKey(key, publicKey);
                aesKey = aesKey.replaceAll("\\r|\\n", "");
                //转json字符串并转成Object对象，设置到Result中并赋值给返回值o
                EncryotResult result = new EncryotResult();
                result.setSuccess(true);
                result.setData(data);
                result.setAesKey(aesKey);
                o = result;

//                o = Result.ok(mapper.readValue("{\"data\":\"" + data + "\",\"aesKey\":\"" + aesKey + "\"}", Object.class));
            }

            //返回
            return o;

        } catch (Throwable e) {
            System.err.println(pjp.getSignature());
            e.printStackTrace();
            return Result.fail("加解密异常：" + e.getMessage());
        }
    }
    /**
     * 获取参数列表
     *
     * @param joinPoint
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     */
    private static Map<String, Object> getFieldsName(ProceedingJoinPoint joinPoint) {
        // 参数值
        Object[] args = joinPoint.getArgs();
        ParameterNameDiscoverer pnd = new DefaultParameterNameDiscoverer();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String[] parameterNames = pnd.getParameterNames(method);
        Map<String, Object> paramMap = new HashMap<>(32);
        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] != null && isCustomClass(args[i].getClass())) {
                paramMap.putAll(getFieldsAsMap(args[i]));
            } else {
                paramMap.put(parameterNames[i], args[i]);
            }
        }
        return paramMap;
    }

    private static boolean isCustomClass(Class<?> clazz) {
        return !clazz.getName().startsWith("java.lang");
    }

    private static Map<String, Object> getFieldsAsMap(Object obj) {
        Map<String, Object> fieldMap = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                fieldMap.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                // 处理异常
            }
        }
        return fieldMap;
    }

}

