package com.bme.cloud.auth.client.web;

import com.bme.cloud.auth.client.fegin.CaptchaService;
import com.bme.cloud.auth.client.fegin.UserService;
import com.bme.cloud.auth.client.model.LoginDTO;
import com.bme.cloud.auth.client.token.LocalTokenStorage;
import com.bme.cloud.common.support.CommonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;

@Api(description = "登录服务")
@RequestMapping("/user")
@RestController
public class LoginController {

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private UserService userService;

    @ApiOperation("验证码获取")
    @GetMapping("/captcha")
    public CommonResult getCaptcha() throws IOException {
        return captchaService.getCaptcha();
    }

    @ApiOperation("登录")
    @PostMapping("/login")
    public CommonResult login(@RequestBody @Valid LoginDTO loginDTO) {
        return userService.login(loginDTO);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public CommonResult logout(@RequestHeader("TOKEN") String token) {

        CommonResult result = userService.logout(token);

        //移除本地缓存
        if (result.isSuccess()) {
            LocalTokenStorage.invalidate(token);
        }

        return result;
    }

    @GetMapping("/loginRecord/list")
    public CommonResult getLoginRecordList(@RequestParam(value = "customerId",required = false) Long customerId,
                                           @RequestParam(value = "userId",required = false) Long userId,
                                           @RequestParam(value = "appCode",required = false) String appCode,
                                           @RequestParam(value = "startTime",required = false) String startTime,
                                           @RequestParam(value = "endTime",required = false) String endTime,
                                           @RequestParam(value = "pageNo",required = false) Integer pageNo,
                                           @RequestParam(value = "pageSize",required = false) Integer pageSize) {
        return userService.getLoginRecordList(customerId, userId, appCode, startTime, endTime, pageNo, pageSize);
    }
}
