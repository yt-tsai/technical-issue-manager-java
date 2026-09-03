package com.example.technicalissuemanager.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Stores a one-time message in the user session after a redirect.
 */
public final class FlashMessage {

    private static final String SUCCESS_MESSAGE_ATTRIBUTE = "successMessage";

    private FlashMessage() {
    }

    public static void setSuccess(HttpServletRequest request, String message) {
        request.getSession().setAttribute(SUCCESS_MESSAGE_ATTRIBUTE, message);
    }

    public static String consumeSuccess(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }

        Object message = session.getAttribute(SUCCESS_MESSAGE_ATTRIBUTE);
        session.removeAttribute(SUCCESS_MESSAGE_ATTRIBUTE);
        return message instanceof String ? (String) message : null;
    }
}
