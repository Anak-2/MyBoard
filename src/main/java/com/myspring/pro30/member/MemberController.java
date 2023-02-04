package com.myspring.pro30.member;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller("memberController")
@RequestMapping(value="member/*")
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberVO memberVO;
	
	@RequestMapping(value="/login.do", method= {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView login(@ModelAttribute("member") MemberVO member,
								RedirectAttributes rAttr,
								HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		String action = request.getParameter("action");
		if(memberVO != null) {
			System.out.println("login success");
			System.out.println(memberVO);
			HttpSession session = request.getSession();
//			MemberVO sessionMember = new MemberVO(memberVO);
//			session.setAttribute("memberInfo", sessionMember);
			session.setAttribute("memberInfo", memberVO);
			session.setAttribute("isLogOn", true);
			if(action != null) {
				mav.setViewName("redirect:"+action);
			}
			else {
				mav.setViewName("redirect:/member/listMembers.do");
			}
		}
		else {
			System.out.println("login failed");
			rAttr.addAttribute("result", "loginFailed");
			rAttr.addAttribute("action", action);
//			또는 rAttr.addFlashAttribute(...)를 사용하면 url에 변수 안 표시하기 가능
			mav.setViewName("redirect:/member/loginForm.do");
		}
		return mav;
	}
	
	@RequestMapping(value="/loginForm.do")
	public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) {
		String action = request.getParameter("action");
		ModelAndView mav = new ModelAndView("member/loginForm");
		if(action != null) {
			mav.addObject("action",action);
		}
		return mav;
	}
	@RequestMapping(value="/memberForm.do")
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("member/memberForm");
		return mav;
	}
	
//	forward to modMember.jsp
	@RequestMapping(value="/modMemberForm.do")
	public ModelAndView modMemberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		MemberVO memberVO = (MemberVO)session.getAttribute("memberInfo");
		ModelAndView mav = new ModelAndView("member/modMember");
		mav.addObject("member",memberVO);
		return mav;
	}
	
//	Modify Member Information
	@RequestMapping(value="/modMember.do")
	public String modMember(@ModelAttribute("member") MemberVO member, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes rAttr) throws Exception{
		if(member.getId() != null) {
			memberService.modMember(member);
			String message = "수정이 완료됐습니다";
			rAttr.addFlashAttribute("message", message);
		}
		else {
			String message = "로그인이 필요합니다";
			rAttr.addFlashAttribute("message", message);
		}
		return "redirect:/member/listMembers.do";
	}
	
	@RequestMapping(value="/listMembers.do")
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mav = new ModelAndView("member/listMembers");
		request.setCharacterEncoding("utf-8");
		response.setContentType("html/text;charset=utf-8");
		List membersList = memberService.listMembers();
		mav.addObject("membersList",membersList);
		return mav;
	}
	
	@RequestMapping(value="/addMember.do")
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member,  HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println(member);
		int result = memberService.addMember(member);
		System.out.println(result);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value="/idDupCheck.do", method = RequestMethod.POST)
	public String idCheck(@RequestBody HashMap<String, String> id) throws IOException{
		System.out.println("Call idDupCheck.do");
		int result = memberService.idDupCheck(id.get("id"));
		if(result == 1) {
			return "unavailable";
		}
		else {
			return "available";			
		}
	}
	
	@RequestMapping(value="/removeMember.do", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView removeMember(@RequestParam("id") String id, HttpServletRequest request, HttpServletResponse response) throws Exception{
		System.out.println("Call removeMember.do");
		request.setCharacterEncoding("utf-8");
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		memberService.removeMember(id);
		return mav;
	}
}
