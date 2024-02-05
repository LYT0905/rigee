package com.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.R;
import com.pojo.User;
import com.service.UserService;
import com.utils.SMSUtils;
import com.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.Map;

/**
 * @author Mark
 * @date 2024/2/4
 */

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送手机短信验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        String phone = user.getPhone();

        if (StringUtils.hasLength(phone)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code={}", code);
            // 调用阿里云发送短信
            //SMSUtils.sendMessage("瑞吉外卖", "", phone, code);

            session.setAttribute("phone", code);

            return R.success("手机验证码短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 登录校验
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeInSession = session.getAttribute("phone");

        if (codeInSession != null && codeInSession.equals(code)){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);

            User user = userService.getOne(queryWrapper);

            if (user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

    /**
     * 用户退出功能
     * @param session
     * @return
     */
    @PostMapping("/loginout")
    public R<String> loginout(HttpSession session){
        session.removeAttribute("user");
        return R.success("退出成功");
    }
}
