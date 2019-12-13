package com.parsa.chatapp.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.parsa.chatapp.messaging.ReceiveMessageListener;
import com.parsa.chatapp.model.OnlineUser;
import com.parsa.chatapp.model.User;
import com.parsa.chatapp.repo.OnlineUserRepository;
import com.parsa.chatapp.repo.UserRepository;

@Controller
public class ViewController {

	@Autowired
	UserRepository userRepo;

	@Autowired
	OnlineUserRepository onlineUserRepo;

	@Autowired
	ReceiveMessageListener receiver;

	Logger logger = LoggerFactory.getLogger(MessagingController.class);

	@GetMapping("/")
	public ModelAndView mainPage() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("index.html");
		return mav;
	}

	@GetMapping("/chat")
	public ModelAndView privateChatView(HttpServletRequest request) {
		String username = request.getUserPrincipal().getName();
		request.getSession().setAttribute("username", username);
		ModelAndView mav = new ModelAndView();
		mav.setViewName("chat.html");
		mav.addObject("username", username);

		List<User> users = userRepo.findAll();
		List<String> usernames = new ArrayList<>();
		for (User u : users) {
			if (username.equals(u))
				continue;
			usernames.add(u.getUsername());
		}
		mav.addObject("users", usernames);

		OnlineUser onlineUser = new OnlineUser();
		onlineUser.setUsername(username);
		logger.info(request.getSession().getId() + "................");
		onlineUser.setId(request.getSession().getId());
		onlineUserRepo.save(onlineUser);

		Iterable<OnlineUser> onlineUsersIterable = onlineUserRepo.findAll();
		Iterator onlineUsersIterator = onlineUsersIterable.iterator();
		List<String> onlineUsers = new ArrayList<>();
		while (onlineUsersIterator.hasNext()) {
			OnlineUser temp = (OnlineUser) onlineUsersIterator.next();
			if (temp.getUsername().equals(username))
				continue;
			onlineUsers.add(temp.getUsername());
		}
		mav.addObject("online_users", onlineUsers);

		return mav;
	}

	@GetMapping("/registration")
	public ModelAndView registration() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("registration.html");
		return mav;
	}

	@GetMapping("/disconnect")
	public ModelAndView disconnect(HttpSession session) {
		receiver.disconnectListener(session.getId());
		session.invalidate();

		ModelAndView mav = new ModelAndView();
		mav.setViewName("index.html");
		return mav;
	}

}
