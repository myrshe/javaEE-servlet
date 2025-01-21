package ru.itis.servlets;

import ru.itis.dto.SignUpForm;
import ru.itis.services.SignUpService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/signUp")
public class SignUpServlet extends HttpServlet {

    private SignUpService signUpService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        signUpService = (SignUpService) getServletContext().getAttribute("signUpService");
        if (signUpService == null) {
            throw new ServletException("SignUpService не инициализирован.");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/views/signUp.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (email == null || email.trim().isEmpty() || username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            request.setAttribute("errorMessage", "You haven't entered anything.");
            request.getRequestDispatcher("/views/signUp.jsp").forward(request, response);
            return;
        }

        boolean userExsists = signUpService.isUsernameExists(username);
        boolean emailExsists = signUpService.isEmailExists(username);

        if (userExsists) {
            request.setAttribute("errorMessage", "A user with this username already exists");
            request.getRequestDispatcher("/views/signUp.jsp").forward(request, response);
            return;
        } else if (emailExsists) {
            request.setAttribute("errorMessage", "A user with this email address already exists");
            request.getRequestDispatcher("/views/signUp.jsp").forward(request, response);
            return;
        }

        if (password.length()<4) {
            request.setAttribute("errorMessage", "the password is too short");
            request.getRequestDispatcher("/views/signUp.jsp").forward(request, response);
            return;
        }

        SignUpForm signUpForm = SignUpForm.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();

        try {
            signUpService.signUp(signUpForm);
            response.sendRedirect("/signIn");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
