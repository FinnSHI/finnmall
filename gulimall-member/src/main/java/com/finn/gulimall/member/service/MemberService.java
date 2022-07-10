package com.finn.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.finn.common.utils.PageUtils;
import com.finn.gulimall.member.entity.MemberEntity;
import com.finn.gulimall.member.vo.MemberUserLoginVO;

import java.util.Map;

/**
 * 会员
 *
 * @author finn
 * @email shifanfinn@gmail.com
 * @date 2022-04-25 23:06:00
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    MemberEntity login(MemberUserLoginVO vo);
}

