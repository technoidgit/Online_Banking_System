package com.cetpa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cetpa.entities.Account;
import com.cetpa.entities.User;
import com.cetpa.services.AccountService;
import com.cetpa.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("canara-bank/user")
public class UserController 
{
	@Autowired private UserService userService;
	@Autowired private AccountService accountService;
	private HttpSession session;
	public static int acc;
	public UserController(HttpSession session) {
		super();
		this.session = session;
	}
	@RequestMapping("login")
	public String getLoginView()
	{
		return "login/user-login";
	}
	@RequestMapping("create-account")
	public String getCreateAccountView()
	{
		return "login/registration";
	}
	@RequestMapping("registerme")
	public String createUserAccount(User user,Model model)
	{
		System.out.println(user.getUserid());
		String uid=user.getUserid();
		User dbuser=userService.getUser(uid);
		System.out.println(dbuser);
		if(dbuser!=null)
		{
			model.addAttribute("msg","User with id "+uid+" already exists");
			return "login/registration";
		}
		int an=userService.saveUser(user);
		model.addAttribute("accountno",an);
		model.addAttribute("name",user.getName());
		return "login/registration-success";
	}
	@RequestMapping("authenticate")
	public String authenticateUser(String userid,String password,Model model,HttpSession ses)
	{
		User user=userService.getUser(userid);
		if(user==null)
		{
			model.addAttribute("msg","Entered userid does not exist");
			model.addAttribute("uid",userid);
			return "login/user-login";
		}
		String dpassword=user.getPassword();
		if(!password.equals(dpassword))
		{
			model.addAttribute("msg","Entered password is wrong");
			model.addAttribute("uid",userid);
			return "login/user-login";
		}
		Account account=accountService.getAccount(userid);
		ses.setAttribute("username",user.getName());
		ses.setAttribute("accountno",account.getAccountno());
		acc=account.getAccountno();
		return "redirect:/canara-bank/home";
	}
	@RequestMapping("logout")
	public String logoutUser(HttpSession ses,Model model)
	{
		model.addAttribute("name",ses.getAttribute("username"));
		ses.invalidate();
		return "login/logout";
	}
	@RequestMapping("change-password")
    public String showChangePasswordForm() {
        return "transaction/change_password"; 
    }
	@RequestMapping("change")
    public String changePassword(@RequestParam("userId") String userId,@RequestParam("currentPassword") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model) {
        User loggedInUser = getLoggedInUser(userId);
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "New password and confirm password do not match");
            return "redirect:/change_password"; 
        }
        if (!userService.validatePassword(loggedInUser.getUserid(), currentPassword)) {
            model.addAttribute("error", "Current password is incorrect");
            return "redirect:/change_password";
        }
        userService.updatePassword(loggedInUser.getUserid(), newPassword);
        return "transaction/success";
    }
	private User getLoggedInUser(String userId) {
		User userDetails = userService.getUser(userId);
        return userDetails;
	}
}
