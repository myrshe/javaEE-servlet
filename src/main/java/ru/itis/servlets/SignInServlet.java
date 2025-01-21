package ru.itis.servlets;


import ru.itis.dto.SignInForm;
import ru.itis.dto.UserDto;
import ru.itis.models.User;
import ru.itis.repositories.repo.UserRepository;
import ru.itis.services.SignInServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/signIn")
public class SignInServlet extends HttpServlet {

    private SignInServiceImpl signInService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        signInService = (SignInServiceImpl) config.getServletContext().getAttribute("signInService");
        if (signInService == null) {
            throw new ServletException("SignInService не инициализирован.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/signIn.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "The username or password you entered is empty");
            request.getRequestDispatcher("/views/signIn.jsp").forward(request, response);
            return;
        }

        SignInForm signInForm = SignInForm.builder()
                .username(username)
                .password(password)
                .build();
        try {
            UserDto userDto = signInService.singIn(signInForm);
            if (userDto != null) {
                HttpSession httpsession = request.getSession(true);
                httpsession.setAttribute("authenticated", true);
                httpsession.setAttribute("user_id", userDto.getId());
                httpsession.setAttribute("username", userDto.getUsername());
                httpsession.setAttribute("role", userDto.getRole());
                response.sendRedirect("/profile/" + userDto.getId());
            } else {
                request.setAttribute("errorMessage", "the username or password was entered incorrectly");
                request.getRequestDispatcher("views/signIn.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
