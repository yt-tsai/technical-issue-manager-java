package com.example.technicalissuemanager.servlet;

import com.example.technicalissuemanager.model.Issue;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/issues")
public class IssueListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        request.setAttribute("issues", createSampleIssues());
        request.getRequestDispatcher("/WEB-INF/jsp/issue-list.jsp").forward(request, response);
    }

    private List<Issue> createSampleIssues() {
        LocalDateTime now = LocalDateTime.now();

        Issue firstIssue = new Issue();
        firstIssue.setId(1);
        firstIssue.setTitle("Customer login bug");
        firstIssue.setCustomer("ABC Corp");
        firstIssue.setProduct("Web Portal");
        firstIssue.setPriority("High");
        firstIssue.setStatus("In Progress");
        firstIssue.setProgress(40);
        firstIssue.setAssignee("Peter");
        firstIssue.setDueDate(LocalDate.of(2026, 9, 5));
        firstIssue.setDescription("Login fails when using special characters in password.");
        firstIssue.setCreatedAt(now);
        firstIssue.setUpdatedAt(now);

        Issue secondIssue = new Issue();
        secondIssue.setId(2);
        secondIssue.setTitle("Data export request");
        secondIssue.setCustomer("XYZ Ltd");
        secondIssue.setProduct("Admin Dashboard");
        secondIssue.setPriority("Medium");
        secondIssue.setStatus("Open");
        secondIssue.setProgress(0);
        secondIssue.setAssignee("Mika");
        secondIssue.setDueDate(LocalDate.of(2026, 9, 8));
        secondIssue.setDescription("Need CSV export for monthly report.");
        secondIssue.setCreatedAt(now);
        secondIssue.setUpdatedAt(now);

        return List.of(firstIssue, secondIssue);
    }
}
