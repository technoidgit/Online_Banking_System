package com.cetpa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("canara-bank")
public class HomeController 
{
	@GetMapping("home")
	public String getHomeView(HttpSession ses)
	{
		if(ses.getAttribute("username")==null)
		{
			return "redirect:/canara-bank/user/login";
		}
		return "home/home-view";
	}
}
