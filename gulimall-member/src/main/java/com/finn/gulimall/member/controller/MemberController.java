package com.finn.gulimall.member.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.Map;

import com.finn.common.enums.BizCodeEnum;
import com.finn.gulimall.member.feign.CouponFeignService;
import com.finn.gulimall.member.vo.MemberUserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finn.gulimall.member.entity.MemberEntity;
import com.finn.gulimall.member.service.MemberService;
import com.finn.common.utils.PageUtils;
import com.finn.common.utils.R;



/**
 * 会员
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 23:06:00
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponFeignService couponFeignService;

    @PostMapping(value = "/login")
    public R login(@RequestBody MemberUserLoginVO vo) {

        MemberEntity memberEntity = memberService.login(vo);

        if (memberEntity != null) {
            return R.ok().setData(memberEntity);
        } else {
            return R.error(BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getCode(),BizCodeEnum.LOGINACCT_PASSWORD_EXCEPTION.getMessage());
        }
    }

    /*
    * @Description: test
    * @Param: []
    * @return: com.finn.common.utils.R
    * @Author: Finn
    * @Date: 2022/04/26 21:42
    */
    @RequestMapping("/coupons")
    public R test(){
        return R.ok().put("member", MemberEntity.builder().username("zhangsan").build()).put("coupons", couponFeignService.memberCoupons().get("coupons"));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }
}
