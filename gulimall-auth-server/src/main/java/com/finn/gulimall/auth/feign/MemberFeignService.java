package com.finn.gulimall.auth.feign;

import com.finn.common.utils.R;
import com.finn.gulimall.auth.vo.UserLoginVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-06-27 17:10
 **/
@FeignClient("gulimall-member")
public interface MemberFeignService {


    @PostMapping(value = "/member/member/login")
    R login(@RequestBody UserLoginVO vo);
}
