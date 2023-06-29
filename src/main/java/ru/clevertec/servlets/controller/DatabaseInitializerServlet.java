package ru.clevertec.servlets.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpServlet;
import ru.clevertec.servlets.util.DBUtil;

public class DatabaseInitializerServlet extends HttpServlet {
    @Override
    public void init(ServletConfig config) {
        DBUtil dbUtil = DBUtil.getInstance();
        dbUtil.migration();
    }
}
