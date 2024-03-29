package com.bigdata2017.mysite.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigdata2017.mysite.exception.UserDaoException;
import com.bigdata2017.mysite.service.UserService;
import com.bigdata2017.mysite.vo.UserVo;

@Controller
@RequestMapping( "/user" )
public class UserController {
	@Autowired
	private UserService userService;
	
	@RequestMapping( value="/join", method=RequestMethod.GET )
	public String join() {
		return "user/join";
	}

	@RequestMapping( value="/join", method=RequestMethod.POST )
	public String join( @ModelAttribute UserVo userVo ) {
		userService.join(userVo);
		return "redirect:/user/joinsuccess";
	}
	
	@RequestMapping( "/joinsuccess" )
	public String joinSuccess() {
		return "user/joinsuccess";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login() {
		return "user/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(
		HttpSession session,	
		@RequestParam(value="email", required=true, defaultValue="") String email,
		@RequestParam(value="password", required=true, defaultValue="") String password
	) {
		
		UserVo userVo =	userService.getUser(email, password);
		if(userVo == null) {
			return "user/login_fail";
		}
		
		// session 처리
		session.setAttribute( "authUser", userVo );
		return "redirect:/";
	}

	@RequestMapping( "/logout" )
	public String logout( HttpSession session ) {
		session.removeAttribute("authUser");
		session.invalidate();
		return "redirect:/";
	}
	
	//@Auth
	@RequestMapping( value="/modify", method=RequestMethod.GET )
	public String modify(
		HttpSession session
		/*@AuthUser UserVo authUser*/
	) {
		UserVo authUser = 
		(UserVo)session.getAttribute( "authUser");
		
		if( authUser == null ) {
			return "redirect:/user/login";
		}
		
		UserVo userVo = userService.getUser( authUser.getNo() );
		
		return "user/modify";
	}
}
